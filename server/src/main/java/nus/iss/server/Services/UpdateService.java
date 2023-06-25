package nus.iss.server.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import nus.iss.server.Model.Coordinates;
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.CoordinatesRepository;
import nus.iss.server.Repositories.UpdateRepository;

@Service
public class UpdateService {

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private GoogleGeocodingService googleGeocodingService;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    public int insertCatUpdate(Update update) {
        update.setId(generateUUID("update-"));        
        Update result = updateRepository.insertCatUpdate(update);
        if (result == null) {
            System.out.println("insertCatUpdate, result is null (returning 0)");
            return 0; // failed
        }

        Optional<SearchCoordinates> coordinates = googleGeocodingService.getGeocoding(update.getLocation());
        if (!coordinates.isPresent()) {
            System.out.println("insertCatUpdate, getGeocoding is null");
            return 1; // updated, but coordinates not found
        } else {
            SearchCoordinates searchCoordinates = coordinates.get();
            Coordinates coordinates2 = new Coordinates();
            coordinates2.setCatId(update.getCatId());
            GeoJsonPoint geoJsonPoint = new GeoJsonPoint(searchCoordinates.getLongitude(), searchCoordinates.getLatitude());
            coordinates2.setLocation(geoJsonPoint);
            Boolean cResult = coordinatesRepository.insertCoordinates(coordinates2);
            if (cResult) {
                System.out.println("insertCatUpdate, success (return 2)");
                return 2; // updated and coordinates inserted
            } else {
                System.out.println("insertCatUpdate, insertCoordinates is null");
                return 1; // updated, but coordinates not inserted
            }
        }
    }

    public String generateUUID(String prefix){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = prefix + uuid.toString().substring(0,8);
        return randomUUIDString;
    }
    
}
