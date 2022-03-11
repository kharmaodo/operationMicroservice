package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.Facture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Facture} entity.
 */
public interface FactureSearchRepository extends ElasticsearchRepository<Facture, Long> {
}
