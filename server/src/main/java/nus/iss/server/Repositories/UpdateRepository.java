package nus.iss.server.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import nus.iss.server.Model.Update;

@Repository
public class UpdateRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "updatecol";

    public List<Update> getFedUpdateByCatId(String id){
        System.out.println("in getFedUpdateByCatId");

        Query latestFedQuery = new Query(Criteria.where("catId").is(id).and("type").is("feed"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        List<Update> fedResult = mongoTemplate.find(latestFedQuery, Update.class, COLLECTION_NAME);

        if (fedResult == null) {
            System.out.println("fedResult is null");
            return null;
        }

        return fedResult;
    }

    public List<Update> getSeenUpdateByCatId(String id){
        System.out.println("in getSeenUpdateByCatId");

        Query latestSeenQuery = new Query(Criteria.where("catId").is(id).and("type").is("seen"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        List<Update> seenResult = mongoTemplate.find(latestSeenQuery, Update.class, COLLECTION_NAME);

        if (seenResult == null) {
            System.out.println("seenResult is null");
            return null;
        }

        return seenResult;
    }

    public Update getOneFedUpdate(String id){
        System.out.println("in getOneFedUpdate, id is " + id);

        try {

            Query latestUpdateQuery = new Query(Criteria.where("catId").is(id).and("type").is("feed"))
                    .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                    .limit(1);
            Update result = mongoTemplate.findOne(latestUpdateQuery, Update.class, COLLECTION_NAME);

            if (result == null) {
                System.out.println("result is null");
                return null;
            }

            return result;

        } catch (Exception e) {
            System.out.println("exception in getOneFedUpdate " + e.getMessage());
            return null;
        }

        
    }

    public Update getOneSeenUpdate(String id){

        try {
            
            System.out.println("in getOneSeenUpdate, id is " + id);

            Query latestUpdateQuery = new Query(Criteria.where("catId").is(id).and("type").is("seen"))
                    .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                    .limit(1);
            Update result = mongoTemplate.findOne(latestUpdateQuery, Update.class, COLLECTION_NAME);

            if (result == null) {
                System.out.println("result is null");
                return null;
            }

            return result;

        } catch (Exception e) {
            System.out.println("exception in getOneSeenUpdate " + e.getMessage());
            return null;
        }
        
    }

    
    
}
