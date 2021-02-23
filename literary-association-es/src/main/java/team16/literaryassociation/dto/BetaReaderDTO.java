package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.es.BetaReaderES;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BetaReaderDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String email;

    private String username;

    private boolean betaReader;

    private int penaltyPoints;

    public BetaReaderDTO(BetaReaderES betaReaderES){
        this.id = betaReaderES.getBetaReaderId();
        this.firstName = betaReaderES.getName();
        this.lastName = betaReaderES.getLastName();
    }
}
