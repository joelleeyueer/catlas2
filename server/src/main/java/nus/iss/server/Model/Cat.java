package nus.iss.server.Model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="catcol")
public class Cat {
    @Indexed
    private String id;
    private String catId;
    private String name;
    private Integer age;
    private Boolean sterilization;
    private List<String> personalityTraits;
    private List<String> dietLikes;
    private List<String> dietDislikes;
    private List<String> feedingNotes;
    private List<List<Double>> frequentLocationList;
}
