package team16.literaryassociation.services.es;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.dto.HighlightDTO;
import team16.literaryassociation.dto.SearchResultDTO;
import team16.literaryassociation.model.es.BetaReaderES;
import team16.literaryassociation.model.es.BookES;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ResultRetriever {

    private ElasticsearchRestTemplate template;

    @Autowired
    public ResultRetriever(ElasticsearchRestTemplate template){
        this.template = template;
    }

    public List<SearchResultDTO> getResults(QueryBuilder query, HighlightBuilder highlightBuilder) {
        if (query == null) {
            return null;
        }
        System.out.println("Usao u resault retriever");
        List<SearchResultDTO> results = new ArrayList<SearchResultDTO>();

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withHighlightBuilder(highlightBuilder)
                .build();

        System.out.println("Napravio query");
        SearchHits<BookES> searchHits = template.search(searchQuery,
                    BookES.class,
                    IndexCoordinates.of("book"));

        System.out.println("ESRTemplejt pretrazio");

        System.out.println("----------------------------RESULTS----------------------------");
        for(SearchHit<BookES> hit : searchHits.getSearchHits())
        {
            SearchResultDTO result = new SearchResultDTO(hit.getContent());
            Map<String, List<String>> highlightMap = hit.getHighlightFields();

            for(String field : highlightMap.keySet())
            {
                boolean flagToAdd = false;
                HighlightDTO highlightDTO = new HighlightDTO();
                highlightDTO.setField(field);
                String highlightStr = "";
                if(field.equals("title")){
                    highlightStr+="<b>Title</b>: ";
                    flagToAdd = true;
                }
                else if(field.equals("writerName")){
                    highlightStr+="<b>Writers name</b>: ";
                    flagToAdd = true;
                }
                else if(field.equals("writerLastName")){
                    highlightStr+="<b>Writers LastName</b>: ";
                    flagToAdd = true;
                }
                else if(field.equals("content")){
                    highlightStr+="<b>Content</b>: ";
                    flagToAdd = true;
                }
                else if(field.equals("genre")){
                    highlightStr+="<b>Genre</b>: ";
                    flagToAdd = true;
                }

                List<String> highlights = highlightMap.get(field);
                System.out.println("-------------HIGHLIGHTS " + field + "-------------------");
                for (String s : highlights) {
                    System.out.println("-------------HIGHLIGHT: " + s);
                    highlightStr+=s+" ... ";
                }
                highlightDTO.setHighlight(highlightStr);
                if(flagToAdd) {
                    result.getHighlights().add(highlightDTO);
                }
            }
            results.add(result);
        }
        return results;
    }

    public List<BetaReaderDTO> getResultsBetaReaders(QueryBuilder query) {
        if (query == null) {
            return null;
        }
        System.out.println("Usao u resault retriever");
        List<BetaReaderDTO> results = new ArrayList<>();

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        System.out.println("Napravio query");

        SearchHits<BetaReaderES> searchHits = template.search(searchQuery,
                BetaReaderES.class,
                IndexCoordinates.of("beta-reader"));

        System.out.println("ESRTemplejt pretrazio");
        System.out.println("----------------------------RESULTS----------------------------");
        System.out.println(searchHits.getSearchHits());

        for(SearchHit<BetaReaderES> hit : searchHits.getSearchHits())
        {
            BetaReaderDTO result = new BetaReaderDTO(hit.getContent());
            results.add(result);
        }
        return results;
    }
}
