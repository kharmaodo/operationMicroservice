package sn.free.selfcare.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.domain.enumeration.AdjustmentType;

/**
 * Adjustment entity.\n@author Ahmadou Diaw
 */
@Entity
@Table(name = "adjustment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adjustment")
public class Adjustment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_id")
	private Long clientId;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "numero_virtuel")
    private String numeroVirtuel;

    @Column(name = "numero_freemoney")
    private String numeroFreemoney;

	@Column(name = "groupe_id")
	private Long groupeId;

	@NotNull
	@Column(name = "target_number", nullable = false)
	private String targetNumber;

	@Column(name = "credit")
	private Double credit;

	@Column(name = "sms")
	private Double sms;

	@Column(name = "min_appel")
	private Double minAppel;

	@Column(name = "go_data")
	private Double goData;

	@Column(name = "price")
	Double price;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type_adjustment", nullable = false)
	private AdjustmentType typeAdjustment;

	@NotNull
	@Column(name = "date_adjustment", nullable = false)
	private Instant dateAdjustment;

	@NotNull
	@Column(name = "trials", nullable = false)
	private Integer trials;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ActionStatus status;

    @OneToOne(mappedBy = "adjustment")
    @JsonIgnore
	private Paiement paiement;

    @Column(name = "adjustment_message")
    private String adjustmentMessage;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public Adjustment clientId(Long clientId) {
		this.clientId = clientId;
		return this;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

    public String getClientCode() {
        return clientCode;
    }

    public Adjustment clientCode(String clientCode) {
        this.clientCode = clientCode;
        return this;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getNumeroVirtuel() {
        return numeroVirtuel;
    }

    public Adjustment numeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
        return this;
    }

    public void setNumeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
    }

    public String getNumeroFreemoney() {
        return numeroFreemoney;
    }

    public Adjustment numeroFreemoney(String numeroFreemoney) {
        this.numeroFreemoney = numeroFreemoney;
        return this;
    }

    public void setNumeroFreemoney(String numeroFreemoney) {
        this.numeroFreemoney = numeroFreemoney;
    }

    public Long getGroupeId() {
		return groupeId;
	}

	public Adjustment groupeId(Long groupeId) {
		this.groupeId = groupeId;
		return this;
	}

	public void setGroupeId(Long groupeId) {
		this.groupeId = groupeId;
	}

	public String getTargetNumber() {
		return targetNumber;
	}

	public Adjustment targetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
		return this;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}

	public Double getCredit() {
		return credit;
	}

	public Adjustment credit(Double credit) {
		this.credit = credit;
		return this;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getSms() {
		return sms;
	}

	public Adjustment sms(Double sms) {
		this.sms = sms;
		return this;
	}

	public void setSms(Double sms) {
		this.sms = sms;
	}

	public Double getMinAppel() {
		return minAppel;
	}

	public Adjustment minAppel(Double minAppel) {
		this.minAppel = minAppel;
		return this;
	}

	public void setMinAppel(Double minAppel) {
		this.minAppel = minAppel;
	}

	public Double getGoData() {
		return goData;
	}

	public Adjustment goData(Double goData) {
		this.goData = goData;
		return this;
	}

	public void setGoData(Double goData) {
		this.goData = goData;
	}

	public AdjustmentType getTypeAdjustment() {
		return typeAdjustment;
	}

	public Adjustment typeAdjustment(AdjustmentType typeAdjustment) {
		this.typeAdjustment = typeAdjustment;
		return this;
	}

	public void setTypeAdjustment(AdjustmentType typeAdjustment) {
		this.typeAdjustment = typeAdjustment;
	}

	public Instant getDateAdjustment() {
		return dateAdjustment;
	}

	public Adjustment dateAdjustment(Instant dateAdjustment) {
		this.dateAdjustment = dateAdjustment;
		return this;
	}

	public void setDateAdjustment(Instant dateAdjustment) {
		this.dateAdjustment = dateAdjustment;
	}

	public Integer getTrials() {
		return trials;
	}

	public Adjustment trials(Integer trials) {
		this.trials = trials;
		return this;
	}

	public void setTrials(Integer trials) {
		this.trials = trials;
	}

	public ActionStatus getStatus() {
		return status;
	}

	public Adjustment status(ActionStatus status) {
		this.status = status;
		return this;
	}

	public void setStatus(ActionStatus status) {
		this.status = status;
	}

	public Paiement getPaiement() {
		return paiement;
	}

	public Adjustment paiement(Paiement paiement) {
		this.paiement = paiement;
		return this;
	}

	public void setPaiement(Paiement paiement) {
		this.paiement = paiement;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

    public String getAdjustmentMessage() {
        return adjustmentMessage;
    }

    public void setAdjustmentMessage(String adjustmentMessage) {
        this.adjustmentMessage = adjustmentMessage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Adjustment)) {
			return false;
		}
		return id != null && id.equals(((Adjustment) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "Adjustment{" +
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
            ", paiement=" + paiement +
            '}';
    }
}
