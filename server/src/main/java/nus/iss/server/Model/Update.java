package nus.iss.server.Model;

import java.util.Date;

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
    private String type; // i.e. update type like seen or fed
    private String catId;
    private String username;
    private Date datetime;
}
