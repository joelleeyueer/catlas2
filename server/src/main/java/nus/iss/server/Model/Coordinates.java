package nus.iss.server.Model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="coordinatesncol")
public class Coordinates {
    @Indexed
    // private String id;
    private String catId;
    private GeoJsonPoint location;
}
