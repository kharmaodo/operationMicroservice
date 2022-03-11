package sn.free.selfcare.repository;

import sn.free.selfcare.domain.PeriodeEnvoi;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PeriodeEnvoi entity.
 */
@Repository
public interface PeriodeEnvoiRepository extends JpaRepository<PeriodeEnvoi, Long> {
	//TODO : MD : Audit : Database instead of ORM powerful
	@Query("SELECT e FROM PeriodeEnvoi e WHERE e.status <> 'ARCHIVED'")
    List<PeriodeEnvoi> findAll();
	//TODO : MD : Audit : Database instead of ORM powerful
	@Query("SELECT e FROM PeriodeEnvoi e WHERE e.groupeId = :groupeId AND e.status <> 'ARCHIVED'")
    List<PeriodeEnvoi> findAllByGroupeId(@Param("groupeId") Long groupeId);
}
