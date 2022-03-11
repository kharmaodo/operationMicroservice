package sn.free.selfcare.service;

import sn.free.selfcare.service.dto.PayasyougoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Payasyougo}.
 */
public interface PayasyougoService {

    /**
     * Save a payasyougo.
     *
     * @param payasyougoDTO the entity to save.
     * @return the persisted entity.
     */
    PayasyougoDTO save(PayasyougoDTO payasyougoDTO);

    /**
     * Get all the payasyougos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayasyougoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" payasyougo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayasyougoDTO> findOne(Long id);

    /**
     * Delete the "id" payasyougo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the payasyougo corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayasyougoDTO> search(String query, Pageable pageable);

    Optional<PayasyougoDTO> findOneByTransactionId(String transactionId);
}
