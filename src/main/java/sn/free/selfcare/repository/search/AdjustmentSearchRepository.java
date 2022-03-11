package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.Adjustment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Adjustment} entity.
 */
public interface AdjustmentSearchRepository extends ElasticsearchRepository<Adjustment, Long> {
}
