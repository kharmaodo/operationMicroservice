package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.PeriodeEnvoi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PeriodeEnvoi} entity.
 */
public interface PeriodeEnvoiSearchRepository extends ElasticsearchRepository<PeriodeEnvoi, Long> {
}
