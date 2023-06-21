package nus.iss.server.Services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
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
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Repositories.CatRepository;
import nus.iss.server.Repositories.CoordinatesRepository;

@Service
public class CatSearchService {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CoordinatesRepository coordinatesRepository;
    
    @Autowired
    private GoogleGeocodingService geocodingService;

    public JsonObject getCatsByLocation(String incomingAddress) {

        //convert address to coordinates
        Optional<SearchCoordinates> coordinates = geocodingService.getGeocoding(incomingAddress);
            System.out.println("Coordinates: " + coordinates);
            if (!coordinates.isPresent()) {
                System.out.println("Coordinates not found");
                return null;
            }

        SearchCoordinates newCoordinates = coordinates.get();

        //get list of cat coordinates from mongodb
        List<Coordinates> incomingCatLocations = coordinatesRepository.getAllCatsWithinRadius(newCoordinates, 1000.0);

        if (incomingCatLocations.isEmpty()) {
            System.out.println("No cats found at " + incomingAddress);
            return null;
        }

        List<String> catIdsList = new LinkedList<>();

        for (Coordinates catLocation : incomingCatLocations) {
            catIdsList.add(catLocation.getCatId());
        }

        List<Cat> catList = catRepository.getAllCatsByCatId(catIdsList);
        return constructJsonObject(newCoordinates, catList, incomingCatLocations);
    }

    
    private JsonObject constructJsonObject(SearchCoordinates searchCoordinates, List<Cat> catList, List<Coordinates> incomingCatLocations) {
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
                JsonArray catLocation = findCatsCoordinate(incomingCatLocations, cat.getCatId());
                JsonObject catJson = Json.createObjectBuilder()
                .add("catId", cat.getCatId())
                .add("name", cat.getName())
                .add("age", convertBirthdayToAge(cat.getBirthday()))
                .add("sterilization", cat.getSterilization())
                .add("personalityTraits", convertListToJsonArray("personalityTraits", cat.getPersonalityTraits()))
                .add("dietLikes", convertListToJsonArray("dietLikes", cat.getDietLikes()))
                .add("dietDislikes", convertListToJsonArray("dietDislikes", cat.getDietDislikes()))
                .add("feedingNotes", convertListToJsonArray("feedingNotes", cat.getFeedingNotes()))
                .add("coordinates", catLocation)
                .build();
                catListJsonArrayBuilder.add(catJson);
            }
            JsonArray catListJson = Json.createArrayBuilder()
            .add(catListJsonArrayBuilder)
            .build();

            resultJsonBuilder.add("catList", catListJson);
            JsonObject resultJson = resultJsonBuilder.build();
            
            return resultJson;
        }
    }

    private JsonArray findCatsCoordinate(List<Coordinates> incomingCatLocations, String catId) {
        for (Coordinates catLocation : incomingCatLocations) {
            if (catLocation.getCatId().equals(catId)) {
                GeoJsonPoint location = catLocation.getLocation();
                JsonObject catLocationJson = Json.createObjectBuilder()
                .add("lat", location.getX())
                .add("lng", location.getY())
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

    private JsonArray convertListToJsonArray(String field, List<String> list) {
        JsonArrayBuilder arrayJson = Json.createArrayBuilder();
        for (String item : list) {
            arrayJson.add(item);
        }

        JsonArray resultJson = arrayJson.build();
        return resultJson;
    }
        
    
}
