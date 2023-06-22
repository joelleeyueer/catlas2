package nus.iss.server.Model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fundraiser {
    private String catId;
    private String userId;
    private Date endDate;
    private Date startDate;
    private String title;
    private String description;
    private Double goalAmount;
    private Double currentAmount;
    private List<String> donorIds;
}
