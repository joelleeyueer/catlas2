package nus.iss.server.Repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import nus.iss.server.Model.Coordinates;
import nus.iss.server.Model.SearchCoordinates;

@Repository
public class CoordinatesRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "locationcol";
    
    public List<Coordinates> getAllCatsWithinRadius(SearchCoordinates coordinates, Double radiusMetres) {
       
        Point coordinatePoint = new Point(coordinates.getLongitude().doubleValue(), coordinates.getLatitude().doubleValue());
        Criteria criteria = Criteria.where("location")
                                    .nearSphere(coordinatePoint)
                                    .maxDistance(radiusMetres / 6378100);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Coordinates.class, COLLECTION_NAME);
    }

    public List<Coordinates> frequentLocationForOneCat(String catId) {
        System.out.println("in frequentLocationForOneCat");
        Query query = new Query(Criteria.where("catId").is(catId));
        List<Coordinates> incomingCoordinates = mongoTemplate.find(query, Coordinates.class, COLLECTION_NAME);
        if (incomingCoordinates == null) {
            System.out.println("incomingCoordinates is null");
            return null;
        }
        return incomingCoordinates;
    }
}
