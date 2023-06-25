package nus.iss.server.Model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fundraiser {
    private String fundId; // to use Stripe product id
    private String catId;
    private String username; // to use user email
    private String photoUrl;
    private String title;
    private String description;
    private boolean active;
    private double donationGoal;
    private LocalDateTime deadline;
    private List<Donor> donations;
    private String stripePaymentUrl;
    private String stripeProductId;
}
