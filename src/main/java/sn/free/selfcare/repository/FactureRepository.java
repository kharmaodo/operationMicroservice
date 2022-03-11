package sn.free.selfcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.free.selfcare.domain.Facture;

/**
 * Spring Data  repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("SELECT f FROM Facture f WHERE f.clientId = :clientId AND (lower(f.codeFacture) LIKE lower(:query))")
    Page<Facture> findFacturesByClientAndQuery(@Param("clientId") String clientId, @Param("query") String query, Pageable pageable);

    @Query("SELECT f FROM Facture f WHERE f.clientId = :clientId")
    Page<Facture> findFacturesByClient(@Param("clientId") String clientId, Pageable pageable);
  //TODO : MD : Audit : Database instead of ORM powerful
    @Query(value = "SELECT COUNT(f.id) FROM Facture f WHERE YEAR(f.datePaiement) = :year AND MONTH(f.datePaiement) = :month AND f.paiementStatus = 'SUCCESS'")
    long getNumberOfPaidBillsPerMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT COALESCE(SUM(f.amount), 0)" +
        " FROM Facture f" +
        " WHERE YEAR(f.datePaiement) = :year" +
        " AND MONTH(f.datePaiement) = :month" +
        " AND f.paiementStatus = 'SUCCESS'")
    long getAmountOfPaidBillsPerMonth(@Param("year") int year, @Param("month") int month);
}
