package dk.roninit.lopadminapp.repository.search;

import dk.roninit.lopadminapp.domain.Marked;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Marked entity.
 */
public interface MarkedSearchRepository extends ElasticsearchRepository<Marked, Long> {
}
