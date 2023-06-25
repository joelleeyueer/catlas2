package nus.iss.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.server.Services.FundraiserService;

@RestController
public class AdminController {

    @Autowired
    FundraiserService fundraiserService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/approveFundraiser/{fund_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approveFundraiserRequest(@PathVariable("fund_id") String fundId) {
        Boolean isApproved = fundraiserService.approveFundraiser(fundId);
        return isApproved ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/rejectFundraiser/{fund_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rejectFundraiserRequest(@PathVariable("fund_id") String fundId) {
        Boolean isRejected = fundraiserService.rejectFundraiser(fundId);
        return isRejected ? ResponseEntity.status(HttpStatus.OK).body(null) :
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/approveCat/{cat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approveCatRequest(@PathVariable("cat_id") String catId) {
        return null;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/admin/rejectCat/{cat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rejectCatRequest(@PathVariable("cat_id") String catId) {
        return null;
    }
}
