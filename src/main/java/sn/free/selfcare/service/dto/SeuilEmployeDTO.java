package sn.free.selfcare.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import sn.free.selfcare.domain.enumeration.ObjectStatus;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Alert} entity.
 */
@ApiModel(description = "Alert entity.\n@author Modou Sène FAYE")
public class SeuilEmployeDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Double seuilCredit;

    private Double seuilData;

    private Long alertId;

    private Long employeId;

    private String employeFullname;

    private String numero;

    private ObjectStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Double getSeuilCredit() {
		return seuilCredit;
	}

	public void setSeuilCredit(Double seuilCredit) {
		this.seuilCredit = seuilCredit;
	}

	public Double getSeuilData() {
		return seuilData;
	}

	public void setSeuilData(Double seuilData) {
		this.seuilData = seuilData;
	}

    public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public Long getEmployeId() {
		return employeId;
	}

	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}

	public String getEmployeFullname() {
		return employeFullname;
	}

	public void setEmployeFullname(String employeFullname) {
		this.employeFullname = employeFullname;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public ObjectStatus getStatus() {
        return status;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeuilEmployeDTO)) {
            return false;
        }

        return id != null && id.equals(((SeuilEmployeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "SeuilEmployeDTO [id=" + id + ", seuilCredit=" + seuilCredit + ", seuilData=" + seuilData + ", alertId="
				+ alertId + ", employeId=" + employeId + ", employeFullname=" + employeFullname + ", numero=" + numero
				+ ", status=" + status + "]";
	}

}
