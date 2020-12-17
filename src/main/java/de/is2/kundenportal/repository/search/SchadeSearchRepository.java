package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Schade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Schade} entity.
 */
public interface SchadeSearchRepository extends ElasticsearchRepository<Schade, Long> {
}
