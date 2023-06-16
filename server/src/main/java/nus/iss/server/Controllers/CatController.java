package nus.iss.server.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.JsonObject;
import nus.iss.server.Repositories.CatRepository;

@RestController
public class CatController {

    @Autowired
    private CatRepository catRepository;

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
    
}
