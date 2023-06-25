package nus.iss.server.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import nus.iss.server.Model.Donor;
import nus.iss.server.Model.Fundraiser;

@Repository
public class FundraiserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "fundraisercol";

    //get fundraiser by catId (max only 1 fundraiser per cat)
    public Fundraiser getFundraiserByCatId(String catId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("catId").is(catId), Criteria.where("approved").is("approved"));

        Query query = new Query(criteria);
        Fundraiser incomingFundraiser = mongoTemplate.findOne(query, Fundraiser.class, COLLECTION_NAME);
        if (incomingFundraiser == null) {
            System.out.println("incomingFundraiser is null");
            return null;
        }
        return incomingFundraiser;
    }

    public void updateDonorListByFundraiserId(String fundId, Donor donor) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("fundId").is(fundId));
        Update update = new Update();
        update.push("donations", donor);
        mongoTemplate.findAndModify(query, update, Fundraiser.class);
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

    public void approveFundraiserByFundraiserId(String fundId, String productId, String paymentLinkUrl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fundId").is(fundId));

        Update update = new Update();
        update.set("approved", "approved");
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
