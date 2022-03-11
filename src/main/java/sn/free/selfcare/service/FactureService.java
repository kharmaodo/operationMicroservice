package sn.free.selfcare.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.free.selfcare.service.dto.AmountPaidBillKpiDTO;
import sn.free.selfcare.service.dto.FactureDTO;
import sn.free.selfcare.service.dto.NumberPaidBillKpiDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Facture}.
 */
public interface FactureService {

    /**
     * Save a facture.
     *
     * @param factureDTO the entity to save.
     * @return the persisted entity.
     */
	FactureDTO save(FactureDTO factureDTO);

    /**
     * Get all the factures.
     *
     * @return the list of entities.
     */
    List<FactureDTO> findAll();


    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactureDTO> findOne(Long id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the facture corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<FactureDTO> search(String query);

    /**
     * Find list factures of a client and filtering by given query
     * @param clientId
     * @param query
     * @param pageable
     * @return
     */
    Page<FactureDTO> findFacturesByClientAndQuery(String clientId, String query, Pageable pageable);

    List<NumberPaidBillKpiDTO> getNumberOfPaidBillsPerMonth(int year);

    List<AmountPaidBillKpiDTO> getAmountOfPaidBillsPerMonth(int year);
}
