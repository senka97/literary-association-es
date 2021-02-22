package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvancedSearchDTO {

    private String fieldName;

    private String value;

    private Boolean phrase;

    private String operation;
}
