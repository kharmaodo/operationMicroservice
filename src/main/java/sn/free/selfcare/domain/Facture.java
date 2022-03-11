package sn.free.selfcare.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * Facture entity.\n@author Ahmadou Diaw
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_facture")
    private String codeFacture;

    @NotNull
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private Instant dateEmission;

    @Column(name = "date_paiement")
    private Instant datePaiement;

    @Column(name = "path")
    private String path;

	@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "paiement_status", nullable = false)
    private ActionStatus paiementStatus;

    @OneToMany(mappedBy = "facture")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Paiement> paiements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeFacture() {
        return codeFacture;
    }

    public Facture codeFacture(String codeFacture) {
        this.codeFacture = codeFacture;
        return this;
    }

    public void setCodeFacture(String codeFacture) {
        this.codeFacture = codeFacture;
    }

    public String getClientId() {
        return clientId;
    }

    public Facture clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getAmount() {
        return amount;
    }

    public Facture amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public Facture dateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
        return this;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public Facture datePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
        return this;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


    public ActionStatus getPaiementStatus() {
        return paiementStatus;
    }

    public Facture paiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
        return this;
    }

    public void setPaiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    public Set<Paiement> getPaiements() {
        return paiements;
    }

    public Facture paiements(Set<Paiement> paiements) {
        this.paiements = paiements;
        return this;
    }

    public Facture addPaiements(Paiement paiement) {
        this.paiements.add(paiement);
        paiement.setFacture(this);
        return this;
    }

    public Facture removePaiements(Paiement paiement) {
        this.paiements.remove(paiement);
        paiement.setFacture(null);
        return this;
    }

    public void setPaiements(Set<Paiement> paiements) {
        this.paiements = paiements;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", codeFacture='" + getCodeFacture() + "'" +
            ", clientId=" + getClientId() +
            ", amount=" + getAmount() +
            ", dateEmission='" + getDateEmission() + "'" +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", paiementStatus='" + getPaiementStatus() + "'" +
            "}";
    }
}
