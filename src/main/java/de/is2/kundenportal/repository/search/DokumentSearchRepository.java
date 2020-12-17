package de.is2.kundenportal.repository.search;

import de.is2.kundenportal.domain.Dokument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Dokument} entity.
 */
public interface DokumentSearchRepository extends ElasticsearchRepository<Dokument, Long> {
}
