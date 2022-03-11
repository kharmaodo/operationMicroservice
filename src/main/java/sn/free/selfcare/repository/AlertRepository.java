package sn.free.selfcare.repository;

import sn.free.selfcare.domain.Alert;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Alert entity.
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
	//TODO : MD : Audit : Database instead of ORM powerful
    @Query("SELECT e FROM Alert e WHERE e.status <> 'ARCHIVED'")
    List<Alert> findAll();
  //TODO : MD : Audit : Database instead of ORM powerful
    @Query("SELECT e FROM Alert e WHERE e.clientId = :clientId AND e.status <> 'ARCHIVED'")
    List<Alert> findAllByClientId(@Param("clientId") Long clientId);
  //TODO : MD : Audit : Database instead of ORM powerful
    @Query("SELECT e FROM Alert e WHERE e.offreId = :offreId AND e.status <> 'ARCHIVED'")
    List<Alert> findAllByOffreId(@Param("offreId") Long offreId);
}
