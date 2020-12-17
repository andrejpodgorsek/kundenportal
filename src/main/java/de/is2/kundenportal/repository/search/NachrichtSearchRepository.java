package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Nachricht;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Nachricht} entity.
 */
public interface NachrichtSearchRepository extends ElasticsearchRepository<Nachricht, Long> {
}
