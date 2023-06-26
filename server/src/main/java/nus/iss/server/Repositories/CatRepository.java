package nus.iss.server.Repositories;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import nus.iss.server.Model.Cat;

@Repository
public class CatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "catcol";

    //old method for testing http request
    public JsonObject getCats() {

        System.out.println("in getCats");

        // Query query = new Query();
        // query.fields().exclude("_id");
    
        List<Document> catlist = mongoTemplate.findAll( Document.class, COLLECTION_NAME);

        if (catlist == null) {
            System.out.println("outgoingDocument is null");
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Document currentCat : catlist) {
            currentCat.remove("_id");
            // System.out.println(currentCat.toString());
            jsonArrayBuilder.add(Json.createReader(new StringReader(currentCat.toJson())).readObject());

        }

        JsonArray jsonArray = jsonArrayBuilder.build();
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("cats", jsonArray)
                .build();

        return jsonObject;
    }

    public Cat getCatByCatId(String id, Boolean admin) {
        System.out.println("in getCatByCatId. id is " + id + ", admin is " + admin);

        Criteria criteria = new Criteria();
        if (admin) {
            criteria.andOperator(Criteria.where("catId").is(id));
        } else {
            criteria.andOperator(Criteria.where("catId").is(id), Criteria.where("approved").is("approved"));
        }

        Query query = new Query(criteria);
        Cat catResult = mongoTemplate.findOne(query, Cat.class, COLLECTION_NAME);

        if (catResult == null) {
            System.out.println("catResult is null");
            return null;
        }

        return catResult;
    }

    public List<Cat> getAllCats(List<String> catIdsList){     
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("catId").in(catIdsList), Criteria.where("approved").is("approved"));

        Query query = new Query(criteria);
        List<Cat> catResults = mongoTemplate.find(query, Cat.class, COLLECTION_NAME);

        if (catResults.isEmpty()) {
            System.out.println("catResults is empty");
            return null;
        }

        return catResults;
    }

    public List<Cat> getAllPendingCatIds(){     
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("approved").is("pending"));

        Query query = new Query(criteria);
        List<Cat> catResults = mongoTemplate.find(query, Cat.class, COLLECTION_NAME);

        if (catResults.isEmpty()) {
            System.out.println("Fundraiser getAllPendingCats is empty");
            return Collections.emptyList();

        }

        return catResults;
    }

    public void approveCatByCatId(String catId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("catId").is(catId));

        Update update = new Update();
        update.set("approved", "approved");

        mongoTemplate.findAndModify(query, update, Cat.class);
    }

    public void rejectCatByCatId(String catId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("catId").is(catId));

        Update update = new Update();
        update.set("approved", "rejected");

        mongoTemplate.findAndModify(query, update, Cat.class);
    }

    public Boolean insertPendingCat(Cat cat) {
        Cat catInsert = mongoTemplate.insert(cat, COLLECTION_NAME);
        if (catInsert == null) {
            System.out.println("insertPendingCat is null");
            return false;
        }
        return true;

    }
}
