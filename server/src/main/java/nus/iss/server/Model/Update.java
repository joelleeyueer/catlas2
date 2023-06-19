package nus.iss.server.Model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="updatecol")
public class Update {
    @Indexed
    private String updateId;
    private String type; // i.e. update type like seen or fed
    private String catId;
    private String username;
    private Date datetime;
}
