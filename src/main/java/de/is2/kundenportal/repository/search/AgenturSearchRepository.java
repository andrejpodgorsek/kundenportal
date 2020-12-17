package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Agentur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Agentur} entity.
 */
public interface AgenturSearchRepository extends ElasticsearchRepository<Agentur, Long> {
}
