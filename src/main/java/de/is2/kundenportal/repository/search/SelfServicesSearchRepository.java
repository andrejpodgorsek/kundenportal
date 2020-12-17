package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.SelfServices;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link SelfServices} entity.
 */
public interface SelfServicesSearchRepository extends ElasticsearchRepository<SelfServices, Long> {
}
