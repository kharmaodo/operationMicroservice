package sn.free.selfcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.free.selfcare.domain.Payasyougo;

import java.util.Optional;

/**
 * Spring Data  repository for the Payasyougo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayasyougoRepository extends JpaRepository<Payasyougo, Long> {
    Optional<Payasyougo> findOneByTransactionId(String transactionId);
}
