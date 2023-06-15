package nus.iss.server.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Value("${mongo.url}")
    private String mongoUrl;

    @Bean
    public MongoTemplate createMongoTemplate(){
        //create a mongo client
        MongoClient mongoClient = MongoClients.create(mongoUrl);

        //name of datebase
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "catDb");
        return mongoTemplate;
        }
    
}
