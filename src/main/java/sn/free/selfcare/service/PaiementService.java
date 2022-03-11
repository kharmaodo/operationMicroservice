package sn.free.selfcare.service;

import sn.free.selfcare.service.dto.CreditValuePurchasedKpiDTO;
import sn.free.selfcare.service.dto.PaiementDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Paiement}.
 */
public interface PaiementService {

    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementDTO save(PaiementDTO paiementDTO);

    /**
     * Get all the paiements.
     *
     * @return the list of entities.
     */
    List<PaiementDTO> findAll();
    /**
     * Get all the PaiementDTO where Adjustment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaiementDTO> findAllWhereAdjustmentIsNull();


    /**
     * Get the "id" paiement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementDTO> findOne(Long id);

    /**
     * Get the "transactionId" paiement.
     *
     * @param transactionId the transactionId of the entity.
     * @return the entity.
     */
    Optional<PaiementDTO> findOneByTransactionId(String transactionId);

    /**
     * Delete the "id" paiement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paiement corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<PaiementDTO> search(String query);

    List<CreditValuePurchasedKpiDTO> getCreditValuePurchasedPerMonth(int year);
}
