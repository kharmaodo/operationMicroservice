package sn.free.selfcare.service;

import sn.free.selfcare.service.dto.SeuilEmployeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.Ligne}.
 */
public interface SeuilEmployeService {

    /**
     * Save a seuilEmploye.
     *
     * @param seuilEmployeDTO the entity to save.
     * @return the persisted entity.
     */
	SeuilEmployeDTO save(SeuilEmployeDTO seuilEmployeDTO);

    /**
     * Get all the seuilEmployes.
     *
     * @return the list of entities.
     */
    List<SeuilEmployeDTO> findAll();

    /**
     * Get all the seuilEmployes.
     *
     * @return the list of entities.
     */
    List<SeuilEmployeDTO> findAllByAlertId(Long alertId);



    /**
     * Get the "id" seuilEmploye.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SeuilEmployeDTO> findOne(Long id);


    /**
     * Delete the "id" seuilEmploye.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

}
