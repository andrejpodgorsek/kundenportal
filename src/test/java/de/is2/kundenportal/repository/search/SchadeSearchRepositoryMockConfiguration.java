package de.is2.kundenportal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SchadeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SchadeSearchRepositoryMockConfiguration {

    @MockBean
    private SchadeSearchRepository mockSchadeSearchRepository;

}
