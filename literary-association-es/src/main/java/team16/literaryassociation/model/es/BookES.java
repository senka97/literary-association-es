package team16.literaryassociation.model.es;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import team16.literaryassociation.model.Book;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@Data
@Document(indexName = BookES.INDEX_NAME, /*type = BookES.TYPE_NAME,*/ shards = 1, replicas = 0)
public class BookES {

    public static final String INDEX_NAME = "book";
    public static final String TYPE_NAME = "book";
    public static final String ANALYZER_NAME = "serbian";

    @Id
    @Field(type = FieldType.Long, store = true)
    private Long bookId;

    @Field(type = FieldType.Text, analyzer = ANALYZER_NAME, store = true)
    private String title;

   // @Field(type = FieldType.Text, store = true)
   // private String downloadUrl;

    @Field(type = FieldType.Text, store = true)
    private String pdf;

    @Field(type = FieldType.Long, store = true)
    private Long writerId;

    @Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    private String writerName;

    @Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    private String writerLastName;

    // store = true ??????
    @Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    private String content;

    @Field(type = FieldType.Boolean, store = true)
    private Boolean openAccess;

    @Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
    private String genre;

    public BookES(Book book, String text){
        this.bookId = book.getId();
        this.title = book.getTitle();
        //this.downloadUrl = book.getDownloadUrl();
        this.writerId = book.getWriter().getId();
        this.writerName = book.getWriter().getFirstName();
        this.writerLastName = book.getWriter().getLastName();
        this.content = text;
        this.openAccess = book.isOpenAccess();
        this.genre = book.getGenre().getName();
        this.pdf = book.getPdf();
    }
}
