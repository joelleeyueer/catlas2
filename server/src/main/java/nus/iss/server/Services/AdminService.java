package nus.iss.server.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import nus.iss.server.Model.Cat;
import nus.iss.server.Model.Fundraiser;
import nus.iss.server.Repositories.CatRepository;
import nus.iss.server.Repositories.CoordinatesRepository;
import nus.iss.server.Repositories.FundraiserRepository;
import nus.iss.server.Repositories.UpdateRepository;

@Service
public class AdminService {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private FundraiserRepository fundraiserRepository;
    
    @Autowired
    private GoogleGeocodingService geocodingService;

    @Autowired
    private FundraiserService fundraiserService;

    public JsonObject getAllPendingCat(){
        try {
        List<Cat> pendingCats = catRepository.getAllPendingCatIds();
        if (pendingCats.isEmpty()){
            System.out.println("No pending cat");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "No pending cat")
                    .build();
                return errorJson;
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Cat cat : pendingCats) {
            JsonObject catJson = Json.createObjectBuilder()
                                     .add("catId", cat.getCatId())
                                     .add("username", cat.getUsername())
                                     .add("approved", cat.getApproved())
                                     .add("name", cat.getName())
                                     .build();
            jsonArrayBuilder.add(catJson);
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("pendingCats", jsonArrayBuilder)
                                    .build();

        return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorJson = Json.createObjectBuilder()
                        .add("error", "No pending cat")
                        .build();
                    return errorJson;
        }
    }

    public JsonObject getAllPendingFund(){
        try {
        List<Fundraiser> pendingFunds = fundraiserRepository.getAllPendingCatIds();
        if (pendingFunds.isEmpty()){
            System.out.println("No pending fundraiser");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "No pending fundraiser")
                    .build();
                return errorJson;
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Fundraiser fund : pendingFunds) {
            JsonObject fundJson = Json.createObjectBuilder()
                                     .add("catId", fund.getCatId())
                                     .add("fundId", fund.getFundId())
                                     .add("approved", fund.getApproved())
                                     .add("username", fund.getUsername())
                                     .add("title", fund.getTitle())
                                     .build();
            jsonArrayBuilder.add(fundJson);
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("pendingFunds", jsonArrayBuilder)
                                    .build();

        return jsonObject;
        
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorJson = Json.createObjectBuilder()
                        .add("error", "No pending fundraiser")
                        .build();
                    return errorJson;
        }
    }
    
}
