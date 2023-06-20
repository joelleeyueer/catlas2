package nus.iss.server.Model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="catcol")
public class Cat {
    private String catId;
    private String name;
    private Date birthday; //days
    private Boolean sterilization;
    private List<String> personalityTraits;
    private List<String> dietLikes;
    private List<String> dietDislikes;
    private List<String> feedingNotes;
}
