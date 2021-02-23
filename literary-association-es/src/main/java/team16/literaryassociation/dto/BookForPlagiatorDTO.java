package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookForPlagiatorDTO {

    private Long id;
    private String title;
    private MultipartFile file;
    private String pathForPDF;

}
