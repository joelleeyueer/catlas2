package nus.iss.server.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCatForm {
    private MultipartFile profilePhoto;
    private String locationAddress;
    private String name;
    private String username;
    private String gender;
    private String birthday;
    private boolean sterilization;
    private List<String> personalityTraits;
    private List<String> dietLikes;
    private List<String> dietDislikes;
    private List<String> feedingNotes;
}
