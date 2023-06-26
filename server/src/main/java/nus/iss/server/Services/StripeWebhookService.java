package nus.iss.server.Services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stripe.exception.StripeException;
import com.stripe.model.LineItem;
import com.stripe.model.LineItemCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;

import nus.iss.server.Model.Donor;
import nus.iss.server.Repositories.FundraiserRepository;

@Service
public class StripeWebhookService {
    
    @Autowired
    FundraiserRepository fundraiserRepository;

    public void processPaymentIntent(PaymentIntent paymentIntent) {
        try {
            Map<String, Object> sessionsParam = new HashMap<>();
            sessionsParam.put("payment_intent", paymentIntent.getId());
            sessionsParam.put("limit", 1);

            SessionCollection sessions = Session.list(sessionsParam);
            String sessionId = sessions.getData().get(0).getId();

            Session session = Session.retrieve(sessionId);
            Map<String, Object> sessionParam = new HashMap<>();
            sessionParam.put("limit", 1);
            LineItemCollection lineItems = session.listLineItems(sessionParam);

            LineItem lineItem = lineItems.getData().get(0);
            String prodId = lineItem.getPrice().getProduct();
            
            Donor donor = new Donor(session.getCustomerEmail(), paymentIntent.getAmount().doubleValue(), LocalDateTime.now());
            fundraiserRepository.updateDonorListByFundraiserId(prodId, donor);

        } catch (StripeException e) {
            System.out.println("Error processing payment for paymentIntent: " + paymentIntent.getId());
            e.printStackTrace();
            
        } catch (Exception e) {
            System.out.println("Error updating donor and payment in database for paymentIntent: " + paymentIntent.getId());
            e.printStackTrace();
        }
    } 
}
