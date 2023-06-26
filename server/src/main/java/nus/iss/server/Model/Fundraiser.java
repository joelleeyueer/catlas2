package nus.iss.server.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fundraisercol")
public class Fundraiser {
    private String fundId;
    private String catId;
    private String username; // to use user email
    private String approved;
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
