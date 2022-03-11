package sn.free.selfcare.repository.search;

import sn.free.selfcare.domain.Paiement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Paiement} entity.
 */
public interface PaiementSearchRepository extends ElasticsearchRepository<Paiement, Long> {
}
