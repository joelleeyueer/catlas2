package nus.iss.server.Repositories;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;

import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Donor;
import nus.iss.server.Model.Fundraiser;

@Repository
public class FundraiserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "fundraisercol";

    //get fundraiser by catId (max only 1 fundraiser per cat)
    public Fundraiser getFundraiserByCatId(String catId, Boolean admin) {

        Criteria criteria = new Criteria();
        if (admin) {
            criteria.andOperator(Criteria.where("catId").is(catId));
        } else {
            criteria.andOperator(Criteria.where("catId").is(catId), Criteria.where("approved").is("approved"));
        }

        Query query = new Query(criteria);
        Fundraiser incomingFundraiser = mongoTemplate.findOne(query, Fundraiser.class, COLLECTION_NAME);
        if (incomingFundraiser == null) {
            System.out.println("incomingFundraiser is null");
            return null;
        }
        return incomingFundraiser;
    }

    public void updateDonorListByFundraiserId(String prodId, Donor donor) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("stripeProductId").is(prodId));
        Update update = new Update();
        update.push("donations", donor);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Fundraiser.class);
        if (result.getModifiedCount() > 0) {
            System.out.println("Updated donations");
        } else {
            System.out.println("Did not update donations");
        }
    } 

    public Fundraiser getFundraiserByFundraiserId(String fundId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fundId").is(fundId));
        
        Fundraiser incomingFundraiser = mongoTemplate.findOne(query, Fundraiser.class, COLLECTION_NAME);
        if (incomingFundraiser == null) {
            System.out.println("incomingFundraiser is null");
            return null;
        }
        return incomingFundraiser;
    }

    public List<Fundraiser> getAllPendingCatIds(){     
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("approved").is("pending"));

        Query query = new Query(criteria);
        List<Fundraiser> fundResults = mongoTemplate.find(query, Fundraiser.class, COLLECTION_NAME);

        if (fundResults.isEmpty()) {
            System.out.println("Fundraiser getAllPendingCatIds is empty");
            return Collections.emptyList();

        }

        return fundResults;
    }

    public Boolean insertPendingFundraiser(Fundraiser fundraiser) {
        Fundraiser fundInsert = mongoTemplate.insert(fundraiser, COLLECTION_NAME);
        if (fundInsert == null) {
            System.out.println("insertPendingFundraiser is null");
            return false;
        }
        return true;

    }

    public void approveFundraiserByFundraiserId(String fundId, String productId, String paymentLinkUrl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fundId").is(fundId));

        Update update = new Update();
        update.set("approved", "approved");
        update.set("active", true);
        update.set("stripeProductId", productId);
        update.set("stripePaymentUrl", paymentLinkUrl);

        mongoTemplate.findAndModify(query, update, Fundraiser.class);
    }

    public void rejectFundraiserByFundraiserId(String fundId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fundId").is(fundId));

        Update update = new Update();
        update.set("approved", "rejected");

        mongoTemplate.findAndModify(query, update, Fundraiser.class);
    }
}
