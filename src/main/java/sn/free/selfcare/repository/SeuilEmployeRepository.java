package sn.free.selfcare.repository;

import sn.free.selfcare.domain.SeuilEmploye;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SeuilEmploye entity.
 */
@Repository
public interface SeuilEmployeRepository extends JpaRepository<SeuilEmploye, Long> {
	//TODO : MD : Audit : Database instead of ORM powerful
    @Query("SELECT e FROM SeuilEmploye e WHERE e.status <> 'ARCHIVED'")
    List<SeuilEmploye> findAll();
  //TODO : MD : Audit : Database instead of ORM powerful
    @Query("SELECT e FROM SeuilEmploye e WHERE e.alert.id = :alertId AND e.status <> 'ARCHIVED'")
    List<SeuilEmploye> findAllByAlertId(@Param("alertId") Long alertId);
}
