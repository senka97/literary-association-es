package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleSearchDTO {

    private String field;

    private String value;

    private Boolean phrase;
}
