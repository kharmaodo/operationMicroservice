package sn.free.selfcare.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;

import sn.free.selfcare.domain.enumeration.ObjectStatus;

/**
 * Alert entity.\n@author Modou Sène FAYE
 */
@Entity
@Table(name = "seuil_employe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "alert")
public class SeuilEmploye implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "seuil_credit", nullable = true)
    private Double seuilCredit;

	@Column(name = "seuil_data", nullable = true)
    private Double seuilData;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ObjectStatus status;

	@ManyToOne
	@JsonBackReference
	private Alert alert;

	@Column(name = "employe_id", nullable = true)
	private Long employeId;

	@Column(name = "employe_fullname", nullable = true)
	private String employeFullname;

	@Column(name = "numero", nullable = true)
	private String numero;


	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSeuilCredit() {
		return seuilCredit;
	}

	public SeuilEmploye seuilCredit(Double seuilCredit) {
		this.seuilCredit = seuilCredit;
		return this;
	}

	public void setSeuilCredit(Double seuilCredit) {
		this.seuilCredit = seuilCredit;
	}

	public Double getSeuilData() {
		return seuilData;
	}

	public SeuilEmploye seuilData(Double seuilData) {
		this.seuilData = seuilData;
		return this;
	}

	public void setSeuilData(Double seuilData) {
		this.seuilData = seuilData;
	}

	public ObjectStatus getStatus() {
		return status;
	}

	public SeuilEmploye status(ObjectStatus status) {
		this.status = status;
		return this;
	}

	public void setStatus(ObjectStatus status) {
		this.status = status;
	}

	public Alert getAlert() {
		return alert;
	}

	public SeuilEmploye alert(Alert alert) {
		this.alert = alert;
		return this;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public Long getEmployeId() {
		return employeId;
	}

	public SeuilEmploye employeId(Long employeId) {
		this.employeId = employeId;
		return this;
	}

	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}

	public String getEmployeFullname() {
		return employeFullname;
	}

	public SeuilEmploye employeFullname(String employeFullname) {
		this.employeFullname = employeFullname;
		return this;
	}

	public void setEmployeFullname(String employeFullname) {
		this.employeFullname = employeFullname;
	}

	public String getNumero() {
		return numero;
	}

	public SeuilEmploye numero(String numero) {
		this.numero = numero;
		return this;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SeuilEmploye)) {
			return false;
		}
		return id != null && id.equals(((SeuilEmploye) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "SeuilEmploye [id=" + id + ", seuilCredit=" + seuilCredit + ", seuilData=" + seuilData + ", status="
				+ status + ", alertId=" + alert.getId() + ", employeId=" + employeId + ", numero=" + numero
				+ ", employeFullname=" + employeFullname + "]";
	}

}
