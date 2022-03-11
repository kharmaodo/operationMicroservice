package sn.free.selfcare.repository;

import sn.free.selfcare.domain.Adjustment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Adjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
}
