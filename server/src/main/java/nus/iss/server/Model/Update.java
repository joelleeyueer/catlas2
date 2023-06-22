package nus.iss.server.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="updatecol")
public class Update {
    @Indexed
    private String id;
    private String type; // i.e. update type like seen or feed
    private String catId;
    private String username;
    private String location;
    private LocalDateTime datetime;
    private String comments;
    private List<String> photos;
}
