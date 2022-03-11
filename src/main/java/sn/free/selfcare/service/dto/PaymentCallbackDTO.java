package sn.free.selfcare.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Payment} entity.
 */
@ApiModel(description = "Payment entity.\n@author MSF")
public class PaymentCallbackDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private String operationType;

	@NotNull
	private String status;

	@NotNull
	private String fId;

	@NotNull
	private String externalId;

	@NotNull
	private Double amount;



	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getfId() {
		return fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PaymentCallbackDTO)) {
			return false;
		}

		return fId != null && fId.equals(((PaymentCallbackDTO) o).fId);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "PaymentCallbackDTO [operationType=" + operationType + ", status=" + status + ", fId=" + fId
				+ ", externalId=" + externalId + ", amount=" + amount + "]";
	}


}
