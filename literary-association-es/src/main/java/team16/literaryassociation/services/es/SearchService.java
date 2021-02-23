package team16.literaryassociation.services.es;

import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class SearchService {

    @Autowired
    private ResultRetriever resultRetriever;

    @Autowired
    private BookService bookService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    public List<SearchResultDTO> simpleSearch(SimpleSearchDTO searchDTO) throws ParseException {
        System.out.println("USAO U SIMPLE SEARCH SERVIS");

        String valid = validateQuery(searchDTO.getField(), searchDTO.getValue());
        if(!valid.equals("")){
            System.out.println(valid);
            return null;
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder().field(searchDTO.getField());
        QueryBuilder query = null;
        if(searchDTO.getPhrase()){ // da li je fraza
            query = QueryBuilders.matchPhraseQuery(searchDTO.getField(), searchDTO.getValue());
        }else {
            query = QueryBuilders.matchQuery(searchDTO.getField(), searchDTO.getValue());
        }
        System.out.println("Napravio query");
        System.out.println(query);

        List<SearchResultDTO> results = resultRetriever.getResults(query, highlightBuilder);

        return results;
    }

    public List<SearchResultDTO> advancedSearch(List<AdvancedSearchDTO> searchDTOList) throws ParseException {

        System.out.println("USAO U ADVANCED SEARCH SERVIS");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        for(AdvancedSearchDTO searchDTO: searchDTOList)
        {
            String valid = validateQuery(searchDTO.getFieldName(), searchDTO.getValue());
            if(!valid.equals("")){
                System.out.println(valid);
                return null;
            }

            if(highlightBuilder.fields().size() == 0){
                highlightBuilder.field(searchDTO.getFieldName());
            }
            else {
                boolean highlightAlreadyExists = false;
                for(HighlightBuilder.Field field : highlightBuilder.fields())
                {
                    if(field.name().equals(searchDTO.getFieldName()))
                    {
                        highlightAlreadyExists = true;
                        break;
                    }
                }
                if(!highlightAlreadyExists)
                {
                    highlightBuilder.field(searchDTO.getFieldName());
                }
            }

            if (searchDTO.getOperation().equals("AND")) {	//koji je operator
                if (searchDTO.getPhrase()) {   //da li je fraza
                    boolQuery.must(QueryBuilders.matchPhraseQuery(searchDTO.getFieldName(), searchDTO.getValue()));
                }else {
                    boolQuery.must(QueryBuilders.matchQuery(searchDTO.getFieldName(), searchDTO.getValue()));
                }
            }else if (searchDTO.getOperation().equals("OR")) {
                if (searchDTO.getPhrase()) {
                    boolQuery.should(QueryBuilders.matchPhraseQuery(searchDTO.getFieldName(), searchDTO.getValue()));
                }else {
                    boolQuery.should(QueryBuilders.matchQuery(searchDTO.getFieldName(), searchDTO.getValue()));

                }
            }else if (searchDTO.getOperation().equals("NOT")) {
                if (searchDTO.getPhrase()) {
                    boolQuery.mustNot(QueryBuilders.matchPhraseQuery(searchDTO.getFieldName(), searchDTO.getValue()));
                }else {
                    boolQuery.mustNot(QueryBuilders.matchQuery(searchDTO.getFieldName(), searchDTO.getValue()));
                }
            }
        }

        System.out.println("Napravio query");
        System.out.println(boolQuery);
        List<SearchResultDTO> results = resultRetriever.getResults(boolQuery, highlightBuilder);

        return results;
    }

    public List<BetaReaderDTO> geoLocationSearch(Long id, String genre)
    {

        Writer writer = (Writer) userService.findById(id);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("genres", genre));
        boolQuery.mustNot(QueryBuilders.geoDistanceQuery("location").distance("100km").point(writer.getLat(),writer.getLon()));

        System.out.println("Napravio query");
        System.out.println(boolQuery);

        List<BetaReaderDTO> betaReadersDTO = resultRetriever.getResultsBetaReaders(boolQuery);
        System.out.println("Pronasao beta readera:" + betaReadersDTO.size());
        return betaReadersDTO;
    }

    private String validateQuery(String field, String value) throws IllegalArgumentException
    {
        String errorMessage = "";
        if(field == null || field.equals("")){
            errorMessage += "Field not specified";
        }
        if(value == null){
            if(!errorMessage.equals("")) errorMessage += "\n";
            errorMessage += "Value not specified";
        }
        return errorMessage;
    }

    public String checkForPlagiarism(MultipartFile file)
    {
        HttpEntity<LoginPlagiatorDTO> requestLogin = new HttpEntity<>(new LoginPlagiatorDTO("soicsenka@gmail.com", "password"));
        HttpEntity<String> responseLogin = null;

        try {
            responseLogin = restTemplate.exchange("http://localhost:8080/api/login",
                    HttpMethod.POST, requestLogin, String.class);
            System.out.println("ULOGOVAO SE");
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        System.out.println("TOKEN");
        System.out.println(responseLogin.getBody());

        String token = responseLogin.getBody().replace("\"", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", new FileSystemResource(convert(file)));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(form, headers);
        ResponseEntity<PaperResultPlagiatorDTO> response = null;

        try {
            response = restTemplate.exchange("http://localhost:8080/api/file/upload/new",
                    HttpMethod.POST, request, PaperResultPlagiatorDTO.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println("ODGOVOR");
        System.out.println(response.getBody());

        // Izvlacenje knjiga iz odgovora
        StringBuilder plagiarismFiles= new StringBuilder();
        for(BookForPlagiatorDTO plagatiorDTO : response.getBody().getSimilarPapers())
        {
            Book bookFound =  bookService.findByFileName(plagatiorDTO.getTitle());
            System.out.println("Pronadjena knjiga");
            plagiarismFiles.append(bookFound.getPdf()).append("|");
        }
        System.out.println("Dobijeni string " + plagiarismFiles);

        return plagiarismFiles.toString();
    }

    public static File convert(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convFile;
    }
}
