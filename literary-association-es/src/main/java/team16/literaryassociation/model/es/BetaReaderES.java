package team16.literaryassociation.model.es;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Reader;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Document(indexName = BetaReaderES.INDEX_NAME, /*type = BetaReaderES.TYPE_NAME,*/ shards = 1, replicas = 0)
public class BetaReaderES {

    public static final String INDEX_NAME = "beta-reader";
    public static final String TYPE_NAME = "beta-reader";
    public static final String ANALYZER_NAME = "serbian";

    @Id
    @Field(type = FieldType.Long, store = true)
    private Long betaReaderId;

    //@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    @Field(type = FieldType.Text, store = true)
    private String name;

    @Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    private List<String> genres;

    @GeoPointField
    private GeoPoint location;

    public BetaReaderES(Reader reader)
    {
        System.out.println("Pravi beta reader ES");
        this.betaReaderId = reader.getId();
        this.name = reader.getFirstName() + " " + reader.getLastName();
        this.genres = new ArrayList<>();
        for(Genre g : reader.getBetaGenres())
        {
            System.out.println("ZANR: " + g.getName());
            this.genres.add(g.getName());
        }
        this.location = new GeoPoint(reader.getLat(), reader.getLon());
    }
}
