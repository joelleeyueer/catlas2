package nus.iss.server.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.Donor;
import nus.iss.server.Model.Fundraiser;
import nus.iss.server.Repositories.FundraiserRepository;

@Service
public class FundraiserService {

    @Autowired
    private FundraiserRepository fundraiserRepository;

    /////////////
    ///API//////
    ///////////
    public JsonObject getFundraiser(String catId){

        Fundraiser fundraiser = fundraiserRepository.getFundraiserByCatId(catId);
        //if empty, return error
        if (fundraiser == null) {
            System.out.println("Fundraiser not found");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Fundraiser not found")
                    .build();
                return errorJson;
        }

        //if not active, return error
        if (!fundraiser.isActive()){
            System.out.println("Fundraiser is not active");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Fundraiser is not active")
                    .build();
                return errorJson;
        }

        //build json object
        JsonObjectBuilder fundraiserJsonBuilder = Json.createObjectBuilder()
            .add("fundId", fundraiser.getFundId())
            .add("catId", fundraiser.getCatId())
            .add("username", fundraiser.getUsername())
            .add("photoUrl", fundraiser.getPhotoUrl())
            .add("title", fundraiser.getTitle())
            .add("description", fundraiser.getDescription())
            .add("donationGoal", fundraiser.getDonationGoal())
            .add("deadline", fundraiser.getDeadline().toString())
            .add("timeRemaining", getTimeRemaining(fundraiser.getDeadline()));

            // Build the JSON array for donations
            JsonArrayBuilder donationsArrayBuilder = Json.createArrayBuilder();
            for (Donor donation : fundraiser.getDonations()) {
                JsonObjectBuilder donationJsonBuilder = Json.createObjectBuilder()
                        .add("username", donation.getUsername())
                        .add("amount", donation.getAmount())
                        .add("donationDate", donation.getDonationDate().toString());
                donationsArrayBuilder.add(donationJsonBuilder);
            }
            fundraiserJsonBuilder.add("donations", donationsArrayBuilder);

            JsonObject fundraiserJson = fundraiserJsonBuilder.build();
            return fundraiserJson;
    
    }

    public String getTimeRemaining(LocalDateTime deadline) {
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(now, deadline);
    
    Period period = Period.between(now.toLocalDate(), deadline.toLocalDate());
    int months = period.getMonths();
    int weeks = period.getDays() / 7;
    int days = period.getDays() % 7;
    int hours = (int) duration.toHours() % 24;
    
    StringBuilder sb = new StringBuilder();
    if (months > 0) {
        sb.append(months).append(months > 1 ? " months" : " month").append(", ");
    }
    if (weeks > 0) {
        sb.append(weeks).append(weeks > 1 ? " weeks" : " week").append(", ");
    }
    if (days > 0) {
        sb.append(days).append(days > 1 ? " days" : " day").append(", ");
    }
    if (hours > 0) {
        sb.append(hours).append(hours > 1 ? " hours" : " hour").append(" ");
    }
    
    String timeRemaining = sb.toString().trim();
    if (timeRemaining.isEmpty()) {
        timeRemaining = "Expired";
    } else {
        timeRemaining += " left";
    }
    
    return timeRemaining;
}
}