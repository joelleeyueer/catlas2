package nus.iss.server.Controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.AddCatForm;
import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Update;
import nus.iss.server.Services.CatSearchService;
import nus.iss.server.Services.CatService;
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

    @Autowired
    private CatService catService;

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
        System.out.println("in getCatByCatId");
        Boolean admin = false;
        JsonObject catJson = catSearchService.getSingleCatInfo(id, admin);
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
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
        String typeStr = type;
        if (type.equalsIgnoreCase("fed")){
            typeStr = "feed";
        }
        update.setCatId(catId);
        update.setType(typeStr);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultJson.toString());
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "newcat", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> addNewCatRequest(@ModelAttribute AddCatForm addCatForm,
        @RequestPart(value = "profilePhoto") MultipartFile photo) {

        System.out.println("printing addCatForm "+ addCatForm);

        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
        JsonObject resultJson = null;

        if (photo == null){
            resultJsonBuilder.add("error", "No photo received");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultJson.toString());
        }

        //create cat object
        Cat cat = new Cat();
        String concat = "cat-" + getCurrentDate() + "-";
        cat.setCatId(generateUUID(concat));
        cat.setApproved("pending");
        cat.setName(addCatForm.getName());
        cat.setUsername(addCatForm.getUsername());
        cat.setGender(addCatForm.getGender());
        cat.setBirthday(stringToDate(addCatForm.getBirthday()));
        cat.setSterilization(addCatForm.isSterilization());
        cat.setPersonalityTraits(addCatForm.getPersonalityTraits());
        cat.setDietLikes(addCatForm.getDietLikes());
        cat.setDietDislikes(addCatForm.getDietDislikes());
        cat.setFeedingNotes(addCatForm.getFeedingNotes());

        int insertStatus = catService.insertNewCatRequest(cat, photo, addCatForm.getLocationAddress());
        if (insertStatus == 1) {
            resultJsonBuilder.add("success", "New cat request submitted successfully");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.OK).body(resultJson.toString());
        } else {
            resultJsonBuilder.add("error", "Error submitting cat request, please try again");
            resultJson = resultJsonBuilder.build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultJson.toString());
        }
       
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "cat/{id}/fundraiser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> getActiveFundraiserByCatId(@PathVariable("id") String id) {
        Boolean admin = false;
        JsonObject fundraiserJson = fundraiserService.getFundraiser(id, admin);
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

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private String constructComment(String comments, String foodType, String waterStatus) {
        StringBuilder result = new StringBuilder();
        System.out.println("in constructComment: " + comments + foodType + waterStatus);

        if (comments != null && !comments.isEmpty()) {
            result.append(comments);
            result.append(". ");
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

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private Date stringToDate(String dateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private String generateUUID(String prefix){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = prefix + uuid.toString().substring(0,8);
        return randomUUIDString;
    }

    ///////////////////////////
    /////HELPER FUNCTIONS//////
    ///////////////////////////
    private String getCurrentDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return now.format(formatter);
    }
}
