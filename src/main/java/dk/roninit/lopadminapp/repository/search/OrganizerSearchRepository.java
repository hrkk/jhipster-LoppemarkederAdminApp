package dk.roninit.lopadminapp.repository.search;

import dk.roninit.lopadminapp.domain.Organizer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Organizer entity.
 */
public interface OrganizerSearchRepository extends ElasticsearchRepository<Organizer, Long> {
}
