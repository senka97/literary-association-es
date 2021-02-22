package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HighlightDTO {

    private String field;

    private String highlight;
}
