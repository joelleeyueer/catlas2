package nus.iss.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import nus.iss.server.Services.AdminService;
import nus.iss.server.Services.CatSearchService;
import nus.iss.server.Services.CatService;
import nus.iss.server.Services.FundraiserService;

@RestController
public class AdminController {

    @Autowired
    private FundraiserService fundraiserService;

    @Autowired
    private CatService catService;

    @Autowired
    private CatSearchService catSearchService;

    @Autowired
    private AdminService adminService;


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin/viewCatRequests", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getAllPendingCat() {
        JsonObject catJson = adminService.getAllPendingCat();
        String catJsonString = catJson.toString();

        if (catJsonString.contains("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(catJsonString);
        } else {
            System.out.println(catJsonString);
            return ResponseEntity.status(HttpStatus.OK).body(catJsonString);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin/viewFundraiserRequests", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getAllPendingFundraiser() {
        JsonObject catJson = adminService.getAllPendingFund();
        String catJsonString = catJson.toString();

        if (catJsonString.contains("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(catJsonString);
        } else {
            System.out.println(catJsonString);
            return ResponseEntity.status(HttpStatus.OK).body(catJsonString);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin/cat/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getCatByCatIdAdmin(@PathVariable("id") String id) {
        System.out.println("in getCatByCatIdAdmin");
        Boolean admin = true;
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
    @GetMapping(value = "/admin/cat/{id}/fundraiser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getActiveFundraiserByCatId(@PathVariable("id") String id) {
        Boolean admin = true;
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
    @PostMapping(value = "/admin/approveFundraiser/{fund_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> approveFundraiserRequest(@PathVariable("fund_id") String fundId) {
        Boolean isApproved = fundraiserService.approveFundraiser(fundId);
        return isApproved ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/rejectFundraiser/{fund_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> rejectFundraiserRequest(@PathVariable("fund_id") String fundId) {
        Boolean isRejected = fundraiserService.rejectFundraiser(fundId);
        return isRejected ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/approveCat/{cat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> approveCatRequest(@PathVariable("cat_id") String catId) {
        Boolean isRejected = catService.approveCat(catId);
        return isRejected ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/rejectCat/{cat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> rejectCatRequest(@PathVariable("cat_id") String catId) {
        Boolean isRejected = catService.rejectCat(catId);
        return isRejected ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
