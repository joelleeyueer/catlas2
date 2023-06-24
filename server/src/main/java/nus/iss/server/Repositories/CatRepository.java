package nus.iss.server.Repositories;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    public Cat getCatByCatId(String id) {
        System.out.println("in getCatByCatId");

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("catId").is(id), Criteria.where("approved").is("approved"));

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
}
