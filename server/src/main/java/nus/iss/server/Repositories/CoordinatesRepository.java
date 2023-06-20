package nus.iss.server.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import nus.iss.server.Model.Coordinates;

@Repository
public class CoordinatesRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "coordinatescol";
    
    public List<Coordinates> getAllCatsWithinRadius(String catId, Double longitude, Double latitude, Double radius) {
       
        Query query = new Query();
        query.addCriteria(Criteria.where("catId").is(catId));
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(longitude, latitude, radius)));

        List<Coordinates> result = mongoTemplate.find(query, Coordinates.class, COLLECTION_NAME);

        return result;
    }
}
