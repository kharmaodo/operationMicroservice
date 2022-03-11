package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.Alert;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Alert} entity.
 */
public interface AlertSearchRepository extends ElasticsearchRepository<Alert, Long> {
}
