package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.es.BookES;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchResultDTO {

    private Long id;

    private String title;

    private String genre;

    private String writer;

    private String pdf;

    private Boolean openAccess;

    private List<HighlightDTO> highlights;

    public SearchResultDTO(BookES book){
        this.id = book.getBookId();
        this.title = book.getTitle();
        this.genre = book.getGenre();
        this.writer = book.getWriterName() + " " + book.getWriterLastName();
        this.pdf = book.getPdf();
        this.openAccess = book.getOpenAccess();
        this.highlights = new ArrayList<>();
    }
}
