package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Vertrag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Vertrag} entity.
 */
public interface VertragSearchRepository extends ElasticsearchRepository<Vertrag, Long> {
}
