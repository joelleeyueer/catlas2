package nus.iss.server.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.server.Model.Donor;
import nus.iss.server.Model.Fundraiser;
import nus.iss.server.Model.Update;
import nus.iss.server.Repositories.FundraiserRepository;
import nus.iss.server.Repositories.UpdateRepository;

@Service
public class FundraiserService {

    @Value("${STRIPE_API_KEY}") // this is also the api key
    private String stripeKey;

    @Autowired
    private FundraiserRepository fundraiserRepository;

    @Autowired
    private UploadToS3Service uploadToS3Service;

    @Autowired
    private UpdateRepository updateRepository;

    public JsonObject getFundraiser(String catId, Boolean admin){

        Fundraiser fundraiser = fundraiserRepository.getFundraiserByCatId(catId, admin);
        //if empty, return error
        if (fundraiser == null) {
            System.out.println("Fundraiser not found");
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Fundraiser not found")
                    .build();
                return errorJson;
        }

        //if not active, return error
        if (!admin){
            if (!fundraiser.isActive()){
                System.out.println("Fundraiser is not active");
                JsonObject errorJson = Json.createObjectBuilder()
                        .add("error", "Fundraiser is not active")
                        .build();
                    return errorJson;
            }
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
            .add("timeRemaining", getTimeRemaining(fundraiser.getDeadline()))
            .add("stripePaymentUrl", fundraiser.getStripePaymentUrl());

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

    public int insertNewFundraiserRequest(Fundraiser fundraiser, MultipartFile photo) {
        Boolean imageUploadSuccess = false; //if this succeeds, return 1
        //send photo to s3, get url, insert url to profilePhoto
        
        try {
            String imageUrl = uploadToS3Service.uploadSingleFile(photo);
            fundraiser.setPhotoUrl(imageUrl);
            imageUploadSuccess = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        //insert cat
        if (imageUploadSuccess) {
            Boolean isInsertCatSuccess = fundraiserRepository.insertPendingFundraiser(fundraiser);

            if (isInsertCatSuccess) {
                return 1; //inserted
            } else {
                return 0; //insertion failed
            }
        }

        return 0;
        
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

    public Boolean approveFundraiser(String fundId) {
        Fundraiser fundToApprove = fundraiserRepository.getFundraiserByFundraiserId(fundId);

        

        try {
            Stripe.apiKey = stripeKey;
            // Create the product in stripe

            // ProductCreateParams productParams =
            //     ProductCreateParams.builder()
            //         .setName(fundToApprove.getTitle())
            //         .setDefaultPriceData(
            //         ProductCreateParams.DefaultPriceData.builder()
            //             .setCurrency("sgd")
            //             .putCurrencyOption("sgd",
            //                 ProductCreateParams.DefaultPriceData.CurrencyOption.builder()
            //                     .setCustomUnitAmount(
            //                         ProductCreateParams.DefaultPriceData.CurrencyOption.CustomUnitAmount.builder()
            //                             .setEnabled(true)
            //                             .build()
            //                     )
            //                     .build()
            //             )
            //             .build()
            //         )
            //         .addExpand("default_price")
            //         .build();

            Map<String, Object> productParams = new HashMap<>();
            productParams.put("name", fundToApprove.getTitle());
            Product product = Product.create(productParams);

            Map<String, Object> customUnitAmountMap = new HashMap<>();
            customUnitAmountMap.put("enabled", true);

            Map<String, Object> priceParams = new HashMap<>();
            priceParams.put("currency", "sgd");
            priceParams.put("product", product.getId());
            priceParams.put("custom_unit_amount", customUnitAmountMap);
            Price price = Price.create(priceParams);

            // Create a payment link for the product that we just created
            List<Object> lineItems = new ArrayList<>();
            Map<String, Object> lineItem1 = new HashMap<>();
            lineItem1.put("price", price.getId());
            lineItem1.put("quantity", 1);
            lineItems.add(lineItem1);

            Map<String, Object> paymentParams = new HashMap<>();
            paymentParams.put("line_items", lineItems);
            PaymentLink paymentLink = PaymentLink.create(paymentParams);

            fundraiserRepository.approveFundraiserByFundraiserId(fundId, product.getId(), paymentLink.getUrl());
            //upon approving fundraiser, need to insert into updatecol
            Update update = new Update();
            update.setType("fundraiser");
            update.setCatId(fundToApprove.getCatId());
            update.setUsername(fundToApprove.getUsername());
            update.setDatetime(fundToApprove.getDeadline());
            update.setComments(fundToApprove.getFundId());

            updateRepository.insertCatUpdate(update);
            return true;
            
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean rejectFundraiser(String fundId) {
        try {
            fundraiserRepository.rejectFundraiserByFundraiserId(fundId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
