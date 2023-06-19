package nus.iss.server.Repositories;

import java.io.StringReader;
import java.util.List;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.*;
import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Update;

@Repository
public class UpdateRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "updatecol";

    public Update getFedUpdateByCatId(String id) throws Exception {
        System.out.println("in getFedUpdateByCatId");

        Query latestFedQuery = new Query(Criteria.where("cat").is(id).and("type").is("feed"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        Update fedResult = mongoTemplate.findOne(latestFedQuery, Update.class, COLLECTION_NAME);

        if (fedResult == null) {
            System.out.println("fedResult is null");
            throw new Exception("Cannot find feed updates for cat in database with the cat id: " + id);
        }

        return fedResult;
    }

    public Update getSeenUpdateByCatId(String id) throws Exception {
        System.out.println("in getSeenUpdateByCatId");

        Query latestSeenQuery = new Query(Criteria.where("cat").is(id).and("type").is("seen"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        Update seenResult = mongoTemplate.findOne(latestSeenQuery, Update.class, COLLECTION_NAME);

        if (seenResult == null) {
            System.out.println("seenResult is null");
            throw new Exception("Cannot find seen updates for cat in database with the cat id: " + id);
        }

        return seenResult;
    }

    
    
}
