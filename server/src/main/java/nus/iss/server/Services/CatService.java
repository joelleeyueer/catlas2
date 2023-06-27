package nus.iss.server.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Coordinates;
import nus.iss.server.Model.SearchCoordinates;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.CatRepository;
import nus.iss.server.Repositories.CoordinatesRepository;

@Service
public class CatService {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private GoogleGeocodingService googleGeocodingService;

    @Autowired
    private UploadToS3Service uploadToS3Service;

    @Autowired
    private UpdateService updateService;

    public Boolean approveCat(String catId) {
        try {
            catRepository.approveCatByCatId(catId);
            

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean rejectCat(String catId) {
        try {
            catRepository.rejectCatByCatId(catId);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int insertNewCatRequest(Cat cat, MultipartFile profilePhoto, String locationAddress) {
        Boolean imageUploadSuccess = false; //if this succeeds, return 1
        Boolean locationAddressSuccess = false; //if this succeeds, return 2
        //send photo to s3, get url, insert url to profilePhoto
        
        try {
            String imageUrl = uploadToS3Service.uploadSingleFile(profilePhoto);
            cat.setProfilePhoto(imageUrl);
            imageUploadSuccess = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        Coordinates coordinates = new Coordinates();
        //send location to google geocoding
        Optional<SearchCoordinates> sCoordinates = googleGeocodingService.getGeocoding(locationAddress);
        if (sCoordinates.isPresent()) {
            locationAddressSuccess = true;
            SearchCoordinates searchCoordinates = sCoordinates.get();
            coordinates.setCatId(cat.getCatId());
            GeoJsonPoint geoJsonPoint = new GeoJsonPoint(searchCoordinates.getLongitude(), searchCoordinates.getLatitude());
            coordinates.setLocation(geoJsonPoint);
        }

        //insert cat
        if (imageUploadSuccess && locationAddressSuccess) {
            Boolean isInsertCatSuccess = catRepository.insertPendingCat(cat);
            Boolean isInsertCoordinatesSuccess = coordinatesRepository.insertCoordinates(coordinates);

            if (isInsertCatSuccess && isInsertCoordinatesSuccess) {
                return 1; //inserted
            } else {
                return 0; //insertion failed
            }
        }

        return 0;
        
    }

    
}
