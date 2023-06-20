package nus.iss.server.Services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import nus.iss.server.Model.SearchCoordinates;

@Service
public class GoogleGeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public SearchCoordinates getGeocoding(String address) {
        String googleapi = "https://maps.googleapis.com/maps/api/geocode/json";

        String url = UriComponentsBuilder
                .fromHttpUrl(googleapi)
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        // ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        RequestEntity<Void> request = RequestEntity.get(new URI(url)).build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // List<String> results = response.getBody().getResults();
        // if (results != null && !results.isEmpty()) {
        //     Double lat = results.get(0).getGeometry().getLocation().getLat();
        //     Double lng = results.get(0).getGeometry().getLocation().getLng();
        //     return new SearchCoordinates(lat, lng);
        // }

        return null;
    }
    
}
