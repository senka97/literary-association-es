package team16.literaryassociation.repository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.es.BetaReaderES;

@Repository
public interface BetaReaderESRepository extends ElasticsearchRepository<BetaReaderES, Long> {
}
