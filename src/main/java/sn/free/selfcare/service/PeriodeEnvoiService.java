package sn.free.selfcare.service;

import java.util.List;
import java.util.Optional;

import sn.free.selfcare.domain.PeriodeEnvoi;
import sn.free.selfcare.service.dto.PeriodeEnvoiDTO;

/**
 * Service Interface for managing {@link sn.free.selfcare.domain.PeriodeEnvoi}.
 */
public interface PeriodeEnvoiService {

	/**
	 * Save a periodeEnvoi.
	 *
	 * @param periodeEnvoiDTO
	 *            the entity to save.
	 * @return the persisted entity.
	 */
	PeriodeEnvoiDTO save(PeriodeEnvoiDTO periodeEnvoiDTO);

	/**
	 * Get all the periodeEnvois.
	 *
	 * @return the list of entities.
	 */
	List<PeriodeEnvoiDTO> findAll();

	/**
	 * Get the "id" periodeEnvoi.
	 *
	 * @param id
	 *            the id of the entity.
	 * @return the entity.
	 */
	Optional<PeriodeEnvoiDTO> findOne(Long id);

	/**
	 * Delete the "id" periodeEnvoi.
	 *
	 * @param id
	 *            the id of the entity.
	 */
	void delete(Long id);

	/**
	 * Search for the periodeEnvoi corresponding to the query.
	 *
	 * @param query
	 *            the query of the search.
	 *
	 * @return the list of entities.
	 */
	List<PeriodeEnvoiDTO> search(String query);

	List<PeriodeEnvoi> findAllEntities();
}
