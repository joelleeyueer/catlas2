package nus.iss.server.Model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection="coordinatesncol")
public class Coordinates {
    @Indexed
    private String id;
    private String catId;
    private GeoJsonPoint location;
}
