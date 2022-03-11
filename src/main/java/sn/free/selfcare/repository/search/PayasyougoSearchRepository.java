package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.Payasyougo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Payasyougo} entity.
 */
public interface PayasyougoSearchRepository extends ElasticsearchRepository<Payasyougo, Long> {
}
