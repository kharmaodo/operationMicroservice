package sn.free.selfcare.service.dto;

import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.domain.enumeration.AdjustmentType;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Adjustment} entity.
 */
@ApiModel(description = "Adjustment entity.\n@author Ahmadou Diaw")
public class AdjustmentDTO implements Serializable {

	private Long id;

	private Long clientId;

    private String clientCode;

    @NotNull
    private String numeroVirtuel;

    private String numeroFreemoney;

    private Long groupeId;

	@NotNull
	private String targetNumber;

	private Double credit;

	private Double sms;

	private Double minAppel;

	private Double goData;

	private Double price;

	@NotNull
	private AdjustmentType typeAdjustment;

	@NotNull
	private Instant dateAdjustment;

	@NotNull
	private Integer trials;

	@NotNull
	private ActionStatus status;

	private String adjustmentMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getSms() {
        return sms;
    }

    public void setSms(Double sms) {
        this.sms = sms;
    }

    public Double getMinAppel() {
        return minAppel;
    }

    public void setMinAppel(Double minAppel) {
        this.minAppel = minAppel;
    }

    public Double getGoData() {
        return goData;
    }

    public void setGoData(Double goData) {
        this.goData = goData;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public AdjustmentType getTypeAdjustment() {
        return typeAdjustment;
    }

    public void setTypeAdjustment(AdjustmentType typeAdjustment) {
        this.typeAdjustment = typeAdjustment;
    }

    public Instant getDateAdjustment() {
        return dateAdjustment;
    }

    public void setDateAdjustment(Instant dateAdjustment) {
        this.dateAdjustment = dateAdjustment;
    }

    public Integer getTrials() {
        return trials;
    }

    public void setTrials(Integer trials) {
        this.trials = trials;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public String getAdjustmentMessage() {
        return adjustmentMessage;
    }

    public void setAdjustmentMessage(String adjustmentMessage) {
        this.adjustmentMessage = adjustmentMessage;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AdjustmentDTO)) {
			return false;
		}

		return id != null && id.equals(((AdjustmentDTO) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

    @Override
    public String toString() {
        return "AdjustmentDTO{" +
            "id=" + id +
            ", clientId=" + clientId +
            ", clientCode='" + clientCode + '\'' +
            ", numeroVirtuel='" + numeroVirtuel + '\'' +
            ", numeroFreemoney='" + numeroFreemoney + '\'' +
            ", groupeId=" + groupeId +
            ", targetNumber='" + targetNumber + '\'' +
            ", credit=" + credit +
            ", sms=" + sms +
            ", minAppel=" + minAppel +
            ", goData=" + goData +
            ", price=" + price +
            ", typeAdjustment=" + typeAdjustment +
            ", dateAdjustment=" + dateAdjustment +
            ", trials=" + trials +
            ", status=" + status +
            '}';
    }
}
