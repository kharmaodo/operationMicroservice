package sn.free.selfcare.service;

import sn.free.selfcare.service.dto.AdjustmentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Adjustment}.
 */
public interface AdjustmentService {

    /**
     * Save a adjustment.
     *
     * @param adjustmentDTO the entity to save.
     * @return the persisted entity.
     */
    AdjustmentDTO save(AdjustmentDTO adjustmentDTO);

    /**
     * Get all the adjustments.
     *
     * @return the list of entities.
     */
    List<AdjustmentDTO> findAll();


    /**
     * Get the "id" adjustment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdjustmentDTO> findOne(Long id);

    /**
     * Delete the "id" adjustment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the adjustment corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<AdjustmentDTO> search(String query);
}
