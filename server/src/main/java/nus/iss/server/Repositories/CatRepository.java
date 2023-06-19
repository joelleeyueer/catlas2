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
public class CatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "catcol";

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
            System.out.println("in here");
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

    public Cat getCatByCatId(String id) throws Exception {
        System.out.println("in getCatByCatId");

        Query query = new Query(Criteria.where("catId").is(id));
        Cat catResult = mongoTemplate.findOne(query, Cat.class, COLLECTION_NAME);

        if (catResult == null) {
            System.out.println("catResult is null");
            throw new Exception("Cannot find cat in database with the id: " + id);
        }

        return catResult;
    }   
}
