package sn.free.selfcare.service;

import sn.free.selfcare.service.dto.AlertDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Ligne}.
 */
public interface AlertService {

    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
	AlertDTO save(AlertDTO alertDTO);

    /**
     * Get all the alerts.
     *
     * @return the list of entities.
     */
    List<AlertDTO> findAll();

    /**
     * Get all the alerts.
     *
     * @return the list of entities.
     */
    List<AlertDTO> findAllByClientId(Long clientId);

    /**
     * Get all the alerts.
     *
     * @return the list of entities.
     */
    List<AlertDTO> findAllByOffreId(Long offreId);



    /**
     * Get the "id" alert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertDTO> findOne(Long id);


    /**
     * Delete the "id" alert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the alert corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<AlertDTO> search(String query);

    void deleteClientAlerts(Long clientId);

}
