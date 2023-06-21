package nus.iss.server.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Services.GoogleGeocodingService;

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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            SearchCoordinates searchCoordinates = coordinates.get();
            JsonObject searchCoordinatesJson = Json.createObjectBuilder()
            .add("lat", searchCoordinates.getLatitude())
            .add("lng", searchCoordinates.getLongitude())
            .build();

            return ResponseEntity.status(HttpStatus.OK).body(searchCoordinatesJson.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
