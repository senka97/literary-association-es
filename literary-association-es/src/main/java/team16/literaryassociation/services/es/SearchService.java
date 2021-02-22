package team16.literaryassociation.services.es;

import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.AdvancedSearchDTO;
import team16.literaryassociation.dto.SearchResultDTO;
import team16.literaryassociation.dto.SimpleSearchDTO;

import java.util.List;


@Service
public class SearchService {

    @Autowired
    private ResultRetriever resultRetriever;

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
}
