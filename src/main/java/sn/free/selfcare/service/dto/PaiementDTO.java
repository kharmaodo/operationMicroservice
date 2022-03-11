package sn.free.selfcare.service.dto;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Paiement} entity.
 */
@ApiModel(description = "Paiement entity.\n@author Ahmadou Diaw")
public class PaiementDTO implements Serializable {

	private Long id;

	@NotNull
	private String transactionId;

    private String externalTransactionId;

    @NotNull
    private Long clientId;

    private String clientCode;

    private String numeroVirtuel;

    private String numeroFreemoney;

    private Long factureId;

	private Long adjustmentId;

	@NotNull
	private Double amount;

	private Instant datePaiement;

	private Integer trials;

	@NotNull
	private ActionStatus paiementStatus;

	private String paymentMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getNumeroVirtuel() {
        return numeroVirtuel;
    }

    public void setNumeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
    }

    public String getNumeroFreemoney() {
        return numeroFreemoney;
    }

    public void setNumeroFreemoney(String numeroFreemoney) {
        this.numeroFreemoney = numeroFreemoney;
    }

    public Long getFactureId() {
        return factureId;
    }

    public void setFactureId(Long factureId) {
        this.factureId = factureId;
    }

    public Long getAdjustmentId() {
        return adjustmentId;
    }

    public void setAdjustmentId(Long adjustmentId) {
        this.adjustmentId = adjustmentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Integer getTrials() {
        return trials;
    }

    public void setTrials(Integer trials) {
        this.trials = trials;
    }

    public ActionStatus getPaiementStatus() {
        return paiementStatus;
    }

    public void setPaiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PaiementDTO)) {
			return false;
		}

		return id != null && id.equals(((PaiementDTO) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + id +
            ", transactionId='" + transactionId + '\'' +
            ", externalTransactionId='" + externalTransactionId + '\'' +
            ", clientId=" + clientId +
            ", clientCode='" + clientCode + '\'' +
            ", numeroVirtuel='" + numeroVirtuel + '\'' +
            ", numeroFreemoney='" + numeroFreemoney + '\'' +
            ", factureId=" + factureId +
            ", adjustmentId=" + adjustmentId +
            ", amount=" + amount +
            ", datePaiement=" + datePaiement +
            ", trials=" + trials +
            ", paiementStatus=" + paiementStatus +
            ", paymentMessage='" + paymentMessage + '\'' +
            '}';
    }
}
