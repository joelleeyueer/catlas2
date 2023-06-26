package nus.iss.server.Model;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFundraiserForm {
    private MultipartFile photo;
    private String catId;
    private String username; // to use user email
    private String approved;
    private String title;
    private String description;
    private String donationGoal;
    private String deadline;
}
