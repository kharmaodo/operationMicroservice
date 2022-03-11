package sn.free.selfcare.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import sn.free.selfcare.domain.enumeration.ObjectStatus;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Alert} entity.
 */
@ApiModel(description = "Alert entity.\n@author Modou Sène FAYE")
public class AlertDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Double seuilStock;

    private ObjectStatus status;

    private Long clientId;

    private Long offreId;

    private String numeroVirtuel;

    private String gestionnaire;

    private Set<SeuilEmployeDTO> seuilEmployes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSeuilStock() {
		return seuilStock;
	}

	public void setSeuilStock(Double seuilStock) {
		this.seuilStock = seuilStock;
	}

    public ObjectStatus getStatus() {
        return status;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getOffreId() {
		return offreId;
	}

	public void setOffreId(Long offreId) {
		this.offreId = offreId;
	}

	public String getNumeroVirtuel() {
		return numeroVirtuel;
	}

	public void setNumeroVirtuel(String numeroVirtuel) {
		this.numeroVirtuel = numeroVirtuel;
	}

	public String getGestionnaire() {
		return gestionnaire;
	}

	public void setGestionnaire(String gestionnaire) {
		this.gestionnaire = gestionnaire;
	}

	public Set<SeuilEmployeDTO> getSeuilEmployes() {
		return seuilEmployes;
	}

	public void setSeuilEmployes(Set<SeuilEmployeDTO> seuilEmployes) {
		this.seuilEmployes = seuilEmployes;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDTO)) {
            return false;
        }

        return id != null && id.equals(((AlertDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "AlertDTO [id=" + id + ", seuilStock=" + seuilStock + ", status=" + status + ", clientId=" + clientId
				+ ", numeroVirtuel=" + numeroVirtuel + ", gestionnaire=" + gestionnaire + ", seuilEmployes=" + seuilEmployes + "]";
	}

}
