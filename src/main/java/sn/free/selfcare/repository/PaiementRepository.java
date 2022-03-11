package sn.free.selfcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.free.selfcare.domain.Paiement;

import java.util.Optional;

/**
 * Spring Data  repository for the Paiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findOneByTransactionId(String transactionid);
  //TODO : MD : Audit : Database instead of ORM powerful
    @Query(value = "SELECT COALESCE(SUM(p.amount), 0)" +
        " FROM Paiement p" +
        " WHERE p.adjustment IS NOT NULL" +
        " AND YEAR(p.datePaiement) = :year AND MONTH(p.datePaiement) = :month" +
        " AND p.paiementStatus = 'SUCCESS'")
    long getCreditValuePurchasedPerMonth(@Param("year") int year, @Param("month") int month);
}
