package nus.iss.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

import nus.iss.server.Services.StripeWebhookService;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.ApiResource;

import com.stripe.model.PaymentIntent;

@RestController
public class StripeWebhookController {

    @Value("sk_test_51NJvHJDboqCw1KwjZvyqCIFPaRh80UdDztTtl5uhlA8IlmSegsNtFuWxQ67dfdCVDP24btyB1hFFJ6bOgaBUUfGB00rnx82QKR")
    private String apiKey;
    
    @Value("whsec_159735ce366b4ee1c047ddb1d97344d4cebc17bfe6c3c861f1d33f8cdab728f1")
    private String endpointSecret;

    @Autowired
    StripeWebhookService stripeWebhookService;

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<String> handleWebhook(@RequestHeader("Stripe-Signature") String sigHeader, 
        @RequestBody String payload) {
        //System.out.println("Received webhook payload: " + payload);

        Event event = null;

        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
            if (endpointSecret != null && sigHeader != null) {
                // Only verify the event if you have an endpoint secret defined.
                // Otherwise use the basic event deserialized with GSON.
                event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
                );
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error while parsing basic request.");

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error while validating signature.");
        }
 
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error while validating signature.");
        }
        
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                stripeWebhookService.processPaymentIntent(paymentIntent);
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
            break;
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
