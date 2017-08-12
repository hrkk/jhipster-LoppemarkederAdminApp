package dk.roninit.lopadminapp.repository.search;

import dk.roninit.lopadminapp.domain.DateInterval;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DateInterval entity.
 */
public interface DateIntervalSearchRepository extends ElasticsearchRepository<DateInterval, Long> {
}
