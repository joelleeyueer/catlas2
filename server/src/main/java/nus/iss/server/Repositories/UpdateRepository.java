package nus.iss.server.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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

    public List<String> getPhotoUrlsByCatId(String catId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("catId").is(catId));

        // Fetch all matching documents
        List<Document> documents = mongoTemplate.find(query, Document.class, COLLECTION_NAME);

        List<String> photoUrls = new ArrayList<>();
        for (Document document : documents) {
            // Check if the document contains the 'photos' field
            if (document.containsKey("photos")) {
                List<String> urls = (List<String>) document.get("photos");
                photoUrls.addAll(urls);
            }
        }

        return photoUrls;
    }

    public List<Update> getFedUpdateByCatId(String id){
        System.out.println("in getFedUpdateByCatId, id is " + id);

        Query latestFedQuery = new Query(Criteria.where("catId").is(id).and("type").is("feed"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<Update> fedResult = mongoTemplate.find(latestFedQuery, Update.class, COLLECTION_NAME);

        if (fedResult == null) {
            System.out.println("fedResult is null");
            return null;
        }

        return fedResult;
    }

    public List<Update> getSeenUpdateByCatId(String id){
        System.out.println("in getSeenUpdateByCatId, id is " + id);

        Query latestSeenQuery = new Query(Criteria.where("catId").is(id).and("type").is("seen"))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<Update> seenResult = mongoTemplate.find(latestSeenQuery, Update.class, COLLECTION_NAME);

        if (seenResult == null) {
            System.out.println("seenResult is null");
            return null;
        }

        System.out.println("seenResult is " + seenResult.toString());

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
                System.out.println("fedResult is null");
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
                System.out.println("getOneSeenUpdate is null");
                return null;
            }



            return result;

        } catch (Exception e) {
            System.out.println("exception in getOneSeenUpdate " + e.getMessage());
            return null;
        }
        
    }

    public Update getOneFundraiserUpdate(String id){

        try {
            
            System.out.println("in getOneFundraiserUpdate, id is " + id);

            Query latestUpdateQuery = new Query(Criteria.where("catId").is(id).and("type").is("fundraiser"))
                    .limit(1);
            Update result = mongoTemplate.findOne(latestUpdateQuery, Update.class, COLLECTION_NAME);

            if (result == null) {
                System.out.println("getOneFundraiserUpdate is null");
                return null;
            }



            return result;

        } catch (Exception e) {
            System.out.println("exception in getOneFundraiserUpdate " + e.getMessage());
            return null;
        }
        
    }

    
    
}
