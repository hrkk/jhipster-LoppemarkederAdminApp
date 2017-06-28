package dk.roninit.lop.admin.repository.search;

import dk.roninit.lop.admin.domain.MarkedItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MarkedItem entity.
 */
public interface MarkedItemSearchRepository extends ElasticsearchRepository<MarkedItem, Long> {
}
