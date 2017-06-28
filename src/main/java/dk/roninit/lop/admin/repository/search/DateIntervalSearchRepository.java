package dk.roninit.lop.admin.repository.search;

import dk.roninit.lop.admin.domain.DateInterval;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DateInterval entity.
 */
public interface DateIntervalSearchRepository extends ElasticsearchRepository<DateInterval, Long> {
}
