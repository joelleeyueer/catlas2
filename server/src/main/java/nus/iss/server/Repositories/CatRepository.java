package nus.iss.server.Repositories;

import java.io.StringReader;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.*;

@Repository
public class CatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "catcol";

    public JsonObject getCats() {

        System.out.println("in getCats");
    
        List<Document> outgoingDocument = mongoTemplate.findAll(Document.class, COLLECTION_NAME);

        if (outgoingDocument == null) {
            System.out.println("outgoingDocument is null");
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Document document : outgoingDocument) {
            System.out.println("in here");
            System.out.println(document.toString());
            jsonArrayBuilder.add(document.toJson());
        }

        JsonArray jsonArray = jsonArrayBuilder.build();

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("cats", jsonArray)
                .build();


        return jsonObject;
    }
    
}
