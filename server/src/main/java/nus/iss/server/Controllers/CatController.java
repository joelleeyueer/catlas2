package nus.iss.server.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.Update;
import nus.iss.server.Services.CatSearchService;
import nus.iss.server.Services.FundraiserService;
import nus.iss.server.Services.UpdateService;
import nus.iss.server.Services.UploadToS3Service;

@RestController
public class CatController {

    @Autowired
    private CatSearchService catSearchService;

    @Autowired
    private FundraiserService fundraiserService;

    @Autowired
    private UploadToS3Service uploadToS3Service;

    @Autowired
    private UpdateService updateService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCats(@RequestParam("address") String incomingAddress) {
        System.out.println("Incoming address: " + incomingAddress);
        // Optional<SearchCoordinates> coordinates = geocodingService.getGeocoding(incomingAddress);
        //             System.out.println("Coordinates: " + coordinates);
        //             if (!coordinates.isPresent()) {
        //                 System.out.println("Coordinates not found");
        //                 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        //             }
        JsonObject catJson = catSearchService.getCatsByLocation(incomingAddress);
        String catJsonString = catJson.toString();

        if (catJsonString.contains("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(catJsonString);
        } else if (catJsonString.contains("No cats found")) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(catJsonString);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(catJsonString);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/cat/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCatByCatId(@PathVariable("id") String id) {
        JsonObject catJson = catSearchService.getSingleCatInfo(id);
        String catJsonString = catJson.toString();
        // System.out.println("printing catJsonString "+ catJsonString);
        
        if (catJsonString.contains("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(catJsonString);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(catJsonString);
        }       
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "cat/{id}/updated", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addStatusByCatId(
                            @RequestParam("type") String type,
                            @RequestParam("catId") String catId,
                            @RequestParam("location") String location,
                            @RequestParam("datetime") String datetime,
                            @RequestParam("comments") String comments,
                            @RequestParam(value = "foodType", required = false) String foodType,
                            @RequestParam(value = "waterStatus", required = false) String waterStatus,
                            @RequestParam(value = "photo", required = false) MultipartFile file) {
        
        //create update object
        Update update = new Update();
        update.setCatId(catId);
        update.setType(type);
        update.setUsername("unknown");
        update.setLocation(location);
        update.setDatetime(LocalDateTime.parse(datetime));
        update.setComments(constructComment(comments, foodType, waterStatus));

        if (file != null){
            //take jpg and upload to s3
            List<String> imageUrls = new ArrayList<>();
            try {
                String imageUrl = uploadToS3Service.uploadSingleFile(file);
                imageUrls.add(imageUrl);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            update.setPhotos(imageUrls);
        }

        int updateSuccess = updateService.insertCatUpdate(update);

        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
        JsonObject resultJson = null;


        if (updateSuccess == 2) {
            resultJsonBuilder.add("success", "Update successfully added, and location added to frequent location");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.OK).body(resultJson.toString());
        } else if (updateSuccess == 1) {
            resultJsonBuilder.add("success", "Update successfully added, and location added to frequent location");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.OK).body(resultJson.toString());
        } else {
            resultJsonBuilder.add("error", "Error adding update");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultJson.toString());
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "cat/{id}/fundraiser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getActiveFundraiserByCatId(@PathVariable("id") String id) {

        JsonObject fundraiserJson = fundraiserService.getFundraiser(id);
        String fundraiserJsonString = fundraiserJson.toString();
        if (fundraiserJsonString.contains("error")) {
            if (fundraiserJsonString.contains("Fundraiser not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fundraiserJsonString);
            } else if (fundraiserJsonString.contains("Fundraiser is not active")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(fundraiserJsonString);
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(fundraiserJsonString);
        }
        return ResponseEntity.status(HttpStatus.OK).body(fundraiserJsonString);

    }
    

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/cat/{cat_id}/fundraiser/{fundraiser_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFundraiserDetailsByFundraiserId(@PathVariable("id") String id) {
        return null;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cat/{cat_id}/fundraiser/{fundraiser_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateFundraiserByFundraiserId(@PathVariable("id") String id) {
        return null;
    }

    private String constructComment(String comments, String foodType, String waterStatus) {
        StringBuilder result = new StringBuilder();

        if (comments != null && !comments.isEmpty()) {
            result.append(comments);
        }

        if (foodType != null && !foodType.isEmpty()) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append("I've fed the cat with ");
            
            String[] foodTypes = foodType.split(" ");
            
            if (foodTypes.length == 1) {
                result.append(foodType).append(" food.");
            } else {
                boolean isFirstType = true;
                for (String type : foodTypes) {
                    if (!isFirstType) {
                        result.append(", ");
                    }
                    result.append(type);
                    isFirstType = false;
                }
                result.append(" food.");
            }
        }
        
        if (waterStatus != null && !waterStatus.isEmpty()) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append("Water bowl ").append(waterStatus).append(".");
        }

        return result.toString();
    }



    
}
