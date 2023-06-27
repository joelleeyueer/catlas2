package nus.iss.server.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Coordinates;
import nus.iss.server.Model.Fundraiser;
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.CatRepository;
import nus.iss.server.Repositories.CoordinatesRepository;
import nus.iss.server.Repositories.FundraiserRepository;
import nus.iss.server.Repositories.UpdateRepository;

@Service
public class CatSearchService {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private FundraiserRepository fundraiserRepository;
    
    @Autowired
    private GoogleGeocodingService geocodingService;

    @Autowired
    private FundraiserService fundraiserService;

    /////////////
    ///API//////
    ///////////
    //get searchCoordinates and catList
    public JsonObject getCatsByLocation(String incomingAddress) {

        //convert address to coordinates
        Optional<SearchCoordinates> coordinates = geocodingService.getGeocoding(incomingAddress);
            System.out.println("Coordinates: " + coordinates);
            if (!coordinates.isPresent()) {
                System.out.println("Coordinates not found");
                JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Address is invalid. Please try another location. For better results, use a postal code.")
                    .build();
                return errorJson;
            }

        SearchCoordinates newCoordinates = coordinates.get();

        //get list of cat coordinates from mongodb
        List<Coordinates> incomingCatLocations = coordinatesRepository.getAllCatsWithinRadius(newCoordinates, 1000.0);

        if (incomingCatLocations.isEmpty()) {
            System.out.println("No cats found at " + incomingAddress);
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "No cats found in " + incomingAddress + ". Please try another location.")
                    .build();
                return errorJson;
        }

        List<String> catIdsList = new LinkedList<>();

        for (Coordinates catLocation : incomingCatLocations) {
            catIdsList.add(catLocation.getCatId());
        }

        List<Cat> catList = catRepository.getAllCats(catIdsList);
        JsonObject searchCoordinatesJson = Json.createObjectBuilder()
        .add("lat", newCoordinates.getLatitude())
        .add("lng", newCoordinates.getLongitude())
        .build();
        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
            resultJsonBuilder.add("searchCoordinates", searchCoordinatesJson);
        if (catList.isEmpty()) {
            JsonObject emptyCatListJson = Json.createObjectBuilder()
            .add("searchCoordinates", resultJsonBuilder)
            .add("catList", Json.createArrayBuilder().build())
            .build();
            return emptyCatListJson;
        } else {
            JsonArrayBuilder catListJsonArrayBuilder = Json.createArrayBuilder();
            for (Cat cat : catList) {
                //get one location
                JsonObject catLocation = findCatsCoordinate(incomingCatLocations, cat.getCatId());
                //get one fed update
                Update fedUpdate = updateRepository.getOneFedUpdate(cat.getCatId());
                JsonObject fedJson = null;

                if (fedUpdate != null) {
                    JsonObjectBuilder fedBuilder = Json.createObjectBuilder()
                            .add("time",timeElapsed(fedUpdate.getDatetime()))
                                .add("username", fedUpdate.getUsername());
                    fedJson = fedBuilder.build();
                } else {
                    fedJson = Json.createObjectBuilder()
                            .addNull("time")
                            .addNull("username")
                            .build();
                }

                //start building Json
                JsonObject catJson = Json.createObjectBuilder()
                .add("catId", cat.getCatId())
                .add("profilePhoto", cat.getProfilePhoto())
                .add("name", cat.getName())
                .add("coordinates", catLocation)
                .add("fed", fedJson)
                .build();
                catListJsonArrayBuilder.add(catJson);
            }

            resultJsonBuilder.add("catList", catListJsonArrayBuilder);
            JsonObject resultJson = resultJsonBuilder.build();
            
            return resultJson;
        }
    }

    /////////////
    ///API//////
    ///////////
    // displaying one cat
    public JsonObject getSingleCatInfo(String catId, Boolean admin){
        Cat cat = catRepository.getCatByCatId(catId, admin);
        if (cat == null) {
            System.out.println("Cat not found");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Cat not found")
                    .build();
                return errorJson;
        }
        //get one fed update
        Update fedUpdate = updateRepository.getOneFedUpdate(catId);
        // System.out.println("fedUpdate: " + fedUpdate);
        JsonArrayBuilder fedJsonArrayBuilder = Json.createArrayBuilder();
        JsonObject fedJson = null;
        if (fedUpdate != null) {
            JsonObjectBuilder fedBuilder = Json.createObjectBuilder();
            fedBuilder.add("location",fedUpdate.getLocation())
                    .add("time",timeElapsed(fedUpdate.getDatetime()))
                    .add("username", fedUpdate.getUsername())
                    .add("comments", fedUpdate.getComments());
            if (fedUpdate.getPhotos().size() > 0) {
                    fedBuilder.add("photoUrls", fedUpdate.getPhotos().get(0));
            }
            fedJson = fedBuilder.build();
            fedJsonArrayBuilder.add(fedJson);
        }
        JsonArray fedJsonArray = fedJsonArrayBuilder.build();

            
        //get one seen update
        Update seenUpdate = updateRepository.getOneSeenUpdate(catId);
        // System.out.println("seenUpdate: " + seenUpdate);
        JsonArrayBuilder seenJsonArrayBuilder = Json.createArrayBuilder();
        JsonObject seenJson = null;
        if (seenUpdate != null)  {
            JsonObjectBuilder seenBuilder = Json.createObjectBuilder();
            seenBuilder.add("location",seenUpdate.getLocation())
                        .add("time",timeElapsed(seenUpdate.getDatetime()))
                        .add("username", seenUpdate.getUsername())
                        .add("comments", seenUpdate.getComments());
            if (seenUpdate.getPhotos().size() > 0) {
                    seenBuilder.add("photoUrls", seenUpdate.getPhotos().get(0));
            }
            seenJson = seenBuilder.build();
            seenJsonArrayBuilder.add(seenJson);
        }
        JsonArray seenJsonArray = seenJsonArrayBuilder.build();

        //get one fundraiser update
        Update fundraiserUpdate = updateRepository.getOneFundraiserUpdate(catId);
        
        JsonObject fundJson = null;
        if (fundraiserUpdate != null)  {
            //get title
            Fundraiser fundraiser = fundraiserRepository.getFundraiserByCatId(catId, admin);
            if (fundraiser!=null){
                JsonObjectBuilder fundBuilder = Json.createObjectBuilder();
                fundBuilder.add("title", fundraiser.getTitle())
                .add("timeLeft", fundraiserService.getTimeRemaining(fundraiserUpdate.getDatetime()));
                fundJson = fundBuilder.build();
            }
        }


        //construct url list
        List<String> catPhotoUrls = updateRepository.getPhotoUrlsByCatId(catId);
        JsonArrayBuilder catPhotoUrlsJsonArrayBuilder = Json.createArrayBuilder();
        catPhotoUrlsJsonArrayBuilder.add(cat.getProfilePhoto());
        if (catPhotoUrls != null) {
            for (String url : catPhotoUrls){
                catPhotoUrlsJsonArrayBuilder.add(url);
            }
        }
        JsonArray catPhotoUrlsArray = catPhotoUrlsJsonArrayBuilder.build();



        //get frequent locations for cat
        List<Coordinates> coordinatesList = coordinatesRepository.frequentLocationForOneCat(catId);
        // System.out.println("coordinatesList: " + coordinatesList);

        //construct the Json Payload
        JsonArrayBuilder catLocationJsonArrayBuilder = Json.createArrayBuilder();
        for (Coordinates coordinates : coordinatesList){
            GeoJsonPoint location = coordinates.getLocation();
            JsonObject catLocationJson = Json.createObjectBuilder()
            .add("lat", location.getY())
            .add("lng", location.getX())
            .build();
            catLocationJsonArrayBuilder.add(catLocationJson);
        }
        JsonArray catLocationJsonArray = catLocationJsonArrayBuilder.build();

        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
        resultJsonBuilder.add("catId", cat.getCatId())
                        .add("photoUrls", catPhotoUrlsArray)
                        .add("name", cat.getName())
                        .add("gender", cat.getGender())
                        .add("age", convertBirthdayToAge(cat.getBirthday()))
                        .add("sterilization", cat.getSterilization())
                        .add("personalityTraits", convertListToJsonArray(cat.getPersonalityTraits()))
                        .add("dietLikes", convertListToJsonArray(cat.getDietLikes()))
                        .add("dietDislikes", convertListToJsonArray(cat.getDietDislikes()))
                        .add("feedingNotes", convertListToJsonArray(cat.getFeedingNotes()))
                        .add("frequentLocations", catLocationJsonArray)
                        .add("fedUpdates", fedJsonArray)
                        .add("seenUpdates", seenJsonArray);

        if (fundJson != null){
            resultJsonBuilder.add("fundraiserUpdates", fundJson);
        }

        JsonObject resultJson = resultJsonBuilder.build();
        // System.out.println("resultJson: " + resultJson);
        return resultJson;
    }

    /////////////
    ///API//////
    ///////////
    // public JsonObject getFundraiser(String catId){

    //     Fundraiser fundraiser = fundraiserRepository.getFundraiserByCatId(catId);
    //     //if empty, return error
    //     if (fundraiser == null) {
    //         System.out.println("Fundraiser not found");
    //         JsonObject errorJson = Json.createObjectBuilder()
    //                 .add("error", "Fundraiser not found")
    //                 .build();
    //             return errorJson;
    //     }

    //     //if not active, return error
    //     if (!fundraiser.isActive()){
    //         System.out.println("Fundraiser is not active");
    //         JsonObject errorJson = Json.createObjectBuilder()
    //                 .add("error", "Fundraiser is not active")
    //                 .build();
    //             return errorJson;
    //     }

    //     JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
    //     resultJsonBuilder.add("fundId", fundraiser.getId())
    //                     .add("catId", fundraiser.getCatId())
    //                     .add("username", fundraiser.getUsername())
    //                     .add("photoUrl", fundraiser.getPhotoUrl())
    //                     .add("title", fundraiser.getTitle())
    //                     .add("description", fundraiser.getDescription())
    //                     .add("active", fundraiser.isActive())
    //                     .add("donationGoal", fundraiser.getDonationGoal())
    //                     .add("deadline", )




    // }


    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    //returns only one coordinate to be populated on the map (for cat-map)
    private JsonObject findCatsCoordinate(List<Coordinates> incomingCatLocations, String catId) {
        for (Coordinates catLocation : incomingCatLocations) {
            if (catLocation.getCatId().equals(catId)) {
                GeoJsonPoint location = catLocation.getLocation();
                JsonObject catLocationJson = Json.createObjectBuilder()
                .add("lat", location.getY())
                .add("lng", location.getX())
                .build();
                return catLocationJson;
            }
        }
        return null;
    }

    

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private String convertBirthdayToAge(Date birthday) {
        //date to localdate
        LocalDate birthdayLD = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthdayLD, now);
        Integer years = age.getYears();
        Integer months = age.getMonths();
        return String.valueOf(years.toString() + " years " + months.toString() + " months");
    }

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private String timeElapsed(LocalDateTime incomingDateTime){
        LocalDateTime currentTime = LocalDateTime.now();

        long hours = ChronoUnit.HOURS.between(incomingDateTime, currentTime);
        long days = ChronoUnit.DAYS.between(incomingDateTime.toLocalDate(), currentTime.toLocalDate());
        hours = hours % 24;

        String result;
        if (days > 0) {
            result = days + " day(s) " + hours + " hour(s) ago";
        } else {
            result = hours + " hour(s) ago";
        }
        return result;
    }

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private JsonArray convertListToJsonArray(List<String> list) {
        JsonArrayBuilder arrayJson = Json.createArrayBuilder();
        for (String item : list) {
            arrayJson.add(item);
        }

        JsonArray resultJson = arrayJson.build();
        return resultJson;
    }

    
        
    
}
