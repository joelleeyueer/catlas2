package nus.iss.server.Controllers;

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

import jakarta.json.JsonObject;
import nus.iss.server.Services.CatSearchService;
import nus.iss.server.Services.FundraiserService;

@RestController
public class CatController {


    @Autowired
    private CatSearchService catSearchService;

    @Autowired
    private FundraiserService fundraiserService;

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

    // @CrossOrigin(origins = "*")
    // @GetMapping(value = "cat/{id}/updates", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> getLatestUpdateByCatId(@PathVariable("id") String id) {

    //     try {
    //         Update seenUpdate = updateRepository.getSeenUpdateByCatId(id);
    //         Update fedUpdate = updateRepository.getFedUpdateByCatId(id);
    //         // fundraiser

    //         Map<String, Object> combinedJson = new HashMap<>();
    //         combinedJson.put("seen", seenUpdate);
    //         combinedJson.put("fed", fedUpdate);

    //         String updateJson = objectMapper.writeValueAsString(combinedJson);
    //         return ResponseEntity.status(HttpStatus.OK).body(updateJson.toString());

    //     } catch (Exception e) {
    //         System.out.println(e.toString());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching updates for cat");
    //     }
    // }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "cat/{id}/updates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStatusByCatId(@PathVariable("id") String id) {
        return null;
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

    
}
