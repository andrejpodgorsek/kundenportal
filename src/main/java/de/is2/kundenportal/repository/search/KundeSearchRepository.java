package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Kunde;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Kunde} entity.
 */
public interface KundeSearchRepository extends ElasticsearchRepository<Kunde, Long> {
}
