package sn.free.selfcare.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import sn.free.selfcare.domain.enumeration.ObjectStatus;

/**
 * Alert entity.\n@author Modou Sène FAYE
 */
@Entity
@Table(name = "alert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "alert")
public class Alert implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "seuil_stock", nullable = true)
    private Double seuilStock;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ObjectStatus status;

	@Column(name = "client_id", nullable = true)
	private Long clientId;

	@Column(name = "offre_id", nullable = true)
	private Long offreId;

	@NotBlank
	@Size(min = 9, max = 9)
	@Digits(fraction = 0, integer = 9)
    private String numeroVirtuel;

    @NotBlank
	@Email
    private String gestionnaire;

    @OneToMany(mappedBy = "alert")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Where(clause = "status != 'ARCHIVED'")
    @JsonManagedReference
    private Set<SeuilEmploye> seuilEmployes = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSeuilStock() {
		return seuilStock;
	}

	public Alert seuilStock(Double seuilStock) {
		this.seuilStock = seuilStock;
		return this;
	}

	public void setSeuilStock(Double seuilStock) {
		this.seuilStock = seuilStock;
	}

	public ObjectStatus getStatus() {
		return status;
	}

	public Alert status(ObjectStatus status) {
		this.status = status;
		return this;
	}

	public void setStatus(ObjectStatus status) {
		this.status = status;
	}

	public Long getClientId() {
		return clientId;
	}

	public Alert clientId(Long clientId) {
		this.clientId = clientId;
		return this;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getOffreId() {
		return offreId;
	}

	public Alert offreId(Long offreId) {
		this.offreId = offreId;
		return this;
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

	public Set<SeuilEmploye> getSeuilEmployes() {
		return seuilEmployes;
	}

	public void setSeuilEmployes(Set<SeuilEmploye> seuilEmployes) {
		this.seuilEmployes = seuilEmployes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Alert)) {
			return false;
		}
		return id != null && id.equals(((Alert) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", seuilStock=" + seuilStock + ", status=" + status + ", clientId=" + clientId
				+ ", offreId=" + offreId + ", numeroVirtuel=" + numeroVirtuel + ", gestionnaire=" + gestionnaire
				+ ", seuilEmployes=" + seuilEmployes + "]";
	}

}
