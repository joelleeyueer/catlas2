package nus.iss.server.Controllers;

import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Services.GoogleGeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/geocoding")
public class TestGeocodingController {

    @Autowired
    private GoogleGeocodingService geocodingService;

    @GetMapping("/{address}")
    public ResponseEntity<String> getGeocoding(@PathVariable String address) {
        try {
            Optional<SearchCoordinates> coordinates = geocodingService.getGeocoding(address);
            if (!coordinates.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(coordinates.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }
}
