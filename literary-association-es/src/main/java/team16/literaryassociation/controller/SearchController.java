package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.AdvancedSearchDTO;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.dto.SearchResultDTO;
import team16.literaryassociation.dto.SimpleSearchDTO;
import team16.literaryassociation.services.es.SearchService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping(value = "/simple")
    public ResponseEntity<?> simpleSearch(@RequestBody SimpleSearchDTO searchDTO)  {
        List<SearchResultDTO> searchResultsDTO = null;

        try {
            searchResultsDTO = searchService.simpleSearch(searchDTO);
            if(searchResultsDTO == null)
            {
                System.out.println("Search result null");
                return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            System.out.println("Search invalid");
            return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(searchResultsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/advanced")
    public ResponseEntity<?> advancedSearch(@RequestBody List<AdvancedSearchDTO> searchDTO)  {
        List<SearchResultDTO> searchResultsDTO = null;

        try {
            searchResultsDTO = searchService.advancedSearch(searchDTO);
            if(searchResultsDTO == null)
            {
                System.out.println("Search result null");
                return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            System.out.println("Search invalid");
            return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(searchResultsDTO, HttpStatus.OK);
    }

    //MOCKUP ZA UDD
    @GetMapping(value = "/geo-location/{userId}/{genre}")
    public ResponseEntity<?> geoLocationSearch(@PathVariable Long userId, @PathVariable String genre)  {
        List<BetaReaderDTO> betaReadersDTO = null;

        try {
            betaReadersDTO = searchService.geoLocationSearch(userId, genre);
            if(betaReadersDTO == null)
            {
                System.out.println("Search result null");
                return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            System.out.println("Search invalid");
            return new ResponseEntity<>("Search invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(betaReadersDTO, HttpStatus.OK);
    }

}
