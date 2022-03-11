package sn.free.selfcare.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PeriodeEnvoiSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PeriodeEnvoiSearchRepositoryMockConfiguration {

    @MockBean
    private PeriodeEnvoiSearchRepository mockPeriodeEnvoiSearchRepository;

}
