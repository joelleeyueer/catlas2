package nus.iss.server.Services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
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
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.CatRepository;
import nus.iss.server.Repositories.CoordinatesRepository;
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
    private GoogleGeocodingService geocodingService;

    public JsonObject getCatsByLocation(String incomingAddress) {

        //convert address to coordinates
        Optional<SearchCoordinates> coordinates = geocodingService.getGeocoding(incomingAddress);
            System.out.println("Coordinates: " + coordinates);
            if (!coordinates.isPresent()) {
                System.out.println("Coordinates not found");
                JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "address is invalid")
                    .build();
                return errorJson;
            }

        SearchCoordinates newCoordinates = coordinates.get();

        //get list of cat coordinates from mongodb
        List<Coordinates> incomingCatLocations = coordinatesRepository.getAllCatsWithinRadius(newCoordinates, 1000.0);

        if (incomingCatLocations.isEmpty()) {
            System.out.println("No cats found at " + incomingAddress);
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Address is valid, but no cats found at" + incomingAddress)
                    .build();
                return errorJson;
        }

        List<String> catIdsList = new LinkedList<>();

        for (Coordinates catLocation : incomingCatLocations) {
            catIdsList.add(catLocation.getCatId());
        }

        List<Cat> catList = catRepository.getAllCats(catIdsList);
        return constructJsonObjectgetCatsByLocation(newCoordinates, catList, incomingCatLocations);
    }

    private JsonObject constructJsonObjectgetCatsByLocation(SearchCoordinates searchCoordinates, List<Cat> catList, List<Coordinates> incomingCatLocations) {
        JsonObject searchCoordinatesJson = Json.createObjectBuilder()
        .add("lat", searchCoordinates.getLatitude())
        .add("lng", searchCoordinates.getLongitude())
        .build();
        JsonArray arrayJsonCoordinates = Json.createArrayBuilder()
        .add(searchCoordinatesJson)
        .build();
        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
            resultJsonBuilder.add("searchCoordinates", arrayJsonCoordinates);
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
                JsonArray catLocation = findCatsCoordinate(incomingCatLocations, cat.getCatId());
                //get one fed update
                Update fedUpdate = updateRepository.getOneFedUpdate(cat.getCatId());
                JsonObject fedJson = null;
                JsonArrayBuilder fedJsonArrayBuilder = Json.createArrayBuilder();

                if (fedUpdate != null) {
                    JsonObjectBuilder fedBuilder = Json.createObjectBuilder()
                            .add("time",timeElapsed(fedUpdate.getDatetime()))
                                .add("username", fedUpdate.getUsername());
                    fedJson = fedBuilder.build();
                    fedJsonArrayBuilder.add(fedJson);
                }
                JsonArray fedJsonArray = fedJsonArrayBuilder.build();

                //start building Json
                JsonObject catJson = Json.createObjectBuilder()
                .add("catId", cat.getCatId())
                .add("profilePhoto", cat.getProfilePhoto())
                .add("name", cat.getName())
                .add("coordinates", catLocation)
                .add("fed", fedJsonArray)
                .build();
                catListJsonArrayBuilder.add(catJson);
            }

            resultJsonBuilder.add("catList", catListJsonArrayBuilder);
            JsonObject resultJson = resultJsonBuilder.build();
            
            return resultJson;
        }
    }

    //displaying one cat
    public JsonObject getSingleCatInfo(String catId){
        Cat cat = catRepository.getCatByCatId(catId);
        if (cat == null) {
            System.out.println("Cat not found");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Cat not found")
                    .build();
                return errorJson;
        }
        //get one fed update
        Update fedUpdate = updateRepository.getOneFedUpdate(catId);
        JsonArrayBuilder fedJsonArrayBuilder = Json.createArrayBuilder();
        JsonObject fedJson = null;
        if (fedUpdate != null) {
            JsonObjectBuilder fedBuilder = Json.createObjectBuilder();
            fedBuilder.add("location",fedUpdate.getLocation())
                    .add("time",timeElapsed(fedUpdate.getDatetime()))
                    .add("username", fedUpdate.getUsername());
            fedJson = fedBuilder.build();
            fedJsonArrayBuilder.add(fedJson);
        }
        JsonArray fedJsonArray = fedJsonArrayBuilder.build();

            
        //get one seen update
        Update seenUpdate = updateRepository.getOneSeenUpdate(catId);
        JsonArrayBuilder seenJsonArrayBuilder = Json.createArrayBuilder();
        JsonObject seenJson = null;
        if (seenUpdate != null)  {
            JsonObjectBuilder seenBuilder = Json.createObjectBuilder();
            seenBuilder.add("location",seenUpdate.getLocation())
                        .add("time",timeElapsed(seenUpdate.getDatetime()))
                        .add("username", seenUpdate.getUsername());
            seenJson = seenBuilder.build();
            seenJsonArrayBuilder.add(fedJson);
        }
        JsonArray seenJsonArray = seenJsonArrayBuilder.build();



        List<Coordinates> coordinatesList = coordinatesRepository.frequentLocationForOneCat(catId);
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
                        .add("name", cat.getName())
                        .add("age", convertBirthdayToAge(cat.getBirthday()))
                        .add("sterilization", cat.getSterilization())
                        .add("personalityTraits", convertListToJsonArray(cat.getPersonalityTraits()))
                        .add("dietLikes", convertListToJsonArray(cat.getDietLikes()))
                        .add("dietDislikes", convertListToJsonArray(cat.getDietDislikes()))
                        .add("feedingNotes", convertListToJsonArray(cat.getFeedingNotes()))
                        .add("frequentLocations", catLocationJsonArray)
                        .add("fedUpdates", fedJsonArray)
                        .add("seenUpdates", seenJsonArray)
                        .build();
        JsonObject resultJson = resultJsonBuilder.build();
        return resultJson;
    }

    
    
    //returns only one coordinate to be populated on the map
    private JsonArray findCatsCoordinate(List<Coordinates> incomingCatLocations, String catId) {
        for (Coordinates catLocation : incomingCatLocations) {
            if (catLocation.getCatId().equals(catId)) {
                GeoJsonPoint location = catLocation.getLocation();
                JsonObject catLocationJson = Json.createObjectBuilder()
                .add("lat", location.getY())
                .add("lng", location.getX())
                .build();
                JsonArray catLocationJsonArray = Json.createArrayBuilder()
                .add(catLocationJson)
                .build();
                return catLocationJsonArray;
            }
        }
        return null;
    }

    

    private String convertBirthdayToAge(Date birthday) {
        //date to localdate
        LocalDate birthdayLD = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthdayLD, now);
        Integer years = age.getYears();
        Integer months = age.getMonths();
        return String.valueOf(years.toString() + " years " + months.toString() + " months");
    }

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


    private JsonArray convertListToJsonArray(List<String> list) {
        JsonArrayBuilder arrayJson = Json.createArrayBuilder();
        for (String item : list) {
            arrayJson.add(item);
        }

        JsonArray resultJson = arrayJson.build();
        return resultJson;
    }
        
    
}
