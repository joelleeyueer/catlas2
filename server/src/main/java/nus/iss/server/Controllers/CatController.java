package nus.iss.server.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.CatRepository;

@RestController
public class CatController {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCats(@RequestParam("long") Double longitude, @RequestParam("lat") Double latitude) {
        System.out.println("longitude is " + longitude);
        System.out.println("latitude is " + latitude);

        JsonObject catJson = catRepository.getCats();

        if (catJson != null) {
            return ResponseEntity.status(HttpStatus.OK).body(catJson.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cats found");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/cats/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCatByCatId(@PathVariable("id") String id) {
        try {
            Cat cat = catRepository.getCatByCatId(id);
            String catJson = objectMapper.writeValueAsString(cat);
            return ResponseEntity.status(HttpStatus.OK).body(catJson);

        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding cat");
        }        
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/updates/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUpdateByCatId(@PathVariable("id") String id) {

        try {
            Update seenUpdate = catRepository.getSeenUpdateByCatId(id);
            Update fedUpdate = catRepository.getFedUpdateByCatId(id);

            Map<String, Object> combinedJson = new HashMap<>();
            combinedJson.put("seen", seenUpdate);
            combinedJson.put("fed", fedUpdate);

            String updateJson = objectMapper.writeValueAsString(combinedJson);
            return ResponseEntity.status(HttpStatus.OK).body(updateJson.toString());

        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching updates for cat");
        }
    }
}
