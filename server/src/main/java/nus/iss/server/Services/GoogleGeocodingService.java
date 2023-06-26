package nus.iss.server.Services;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.server.Model.SearchCoordinates;

@Service
public class GoogleGeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<SearchCoordinates> getGeocoding(String address) {
        String googleapi = "https://maps.googleapis.com/maps/api/geocode/json";

        String url = UriComponentsBuilder
                .fromHttpUrl(googleapi)
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        // ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        try {
            RequestEntity<Void> request;
            request = RequestEntity.get(new URI(url)).build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String body = response.getBody();
            // System.out.println(body);
            JsonReader reader = Json.createReader(new StringReader(body));
            JsonObject obj = reader.readObject();
            JsonArray results = obj.getJsonArray("results");

            if (results.isEmpty()) {
                System.out.println("No results found first if");
                return Optional.empty();
            }

            JsonObject locationObj = results.getJsonObject(0)
                                    .getJsonObject("geometry")
                                    .getJsonObject("location");

            Double lat = locationObj.getJsonNumber("lat").doubleValue();
            Double lng = locationObj.getJsonNumber("lng").doubleValue();
            System.out.println("lat: " + lat + " lng: " + lng);

            if (lat == null || lng == null) {
                System.out.println("No results found second if");
                return Optional.empty();
            }

            return Optional.of(new SearchCoordinates(lat, lng));

        } catch (Exception e) {
            System.out.println("in catch");
            return Optional.empty();
        }
        
    }
    
}
