package team16.literaryassociation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class PaperResultPlagiatorDTO {

    private List<BookForPlagiatorDTO> similarPapers = new ArrayList<>();
}
