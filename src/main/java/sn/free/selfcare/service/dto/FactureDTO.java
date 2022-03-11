package sn.free.selfcare.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Facture} entity.
 */
@ApiModel(description = "Facture entity.\n@author Ahmadou Diaw")
public class FactureDTO implements Serializable {

    private Long id;

    private String codeFacture;

    @NotNull
    private String clientId;

    @NotNull
    private Double amount;

    @NotNull
    private Instant dateEmission;

    private Instant datePaiement;

    @NotNull
    private ActionStatus paiementStatus;

	private String path;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeFacture() {
        return codeFacture;
    }

    public void setCodeFacture(String codeFacture) {
        this.codeFacture = codeFacture;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDatePaiement() {
        return datePaiement;
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

    public void setPaiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureDTO)) {
            return false;
        }

        return id != null && id.equals(((FactureDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + getId() +
            ", codeFacture='" + getCodeFacture() + "'" +
            ", clientId=" + getClientId() +
            ", amount=" + getAmount() +
            ", dateEmission='" + getDateEmission() + "'" +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", path='" + getPath() + "'" +
            ", paiementStatus='" + getPaiementStatus() + "'" +
            "}";
    }
}
