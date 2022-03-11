package sn.free.selfcare.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * Paiement entity.\n@author Ahmadou Diaw
 */
@Entity
@Table(name = "paiement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paiement")
public class Paiement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "transaction_id", nullable = false)
	private String transactionId;

	@NotNull
	@Column(name = "client_id")
	private Long clientId;

    @Column(name = "client_code", nullable = false)
    private String clientCode;

    @Column(name = "numero_virtuel")
    private String numeroVirtuel;

    @Column(name = "numero_freemoney")
    private String numeroFreemoney;

	@NotNull
	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "date_paiement")
	private Instant datePaiement;

	@Column(name = "trials")
	private Integer trials;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "paiement_status", nullable = false)
	private ActionStatus paiementStatus;

    @OneToOne
	@JsonIgnoreProperties(value = "paiement", allowSetters = true)
	private Adjustment adjustment;

	@ManyToOne
	@JsonIgnoreProperties(value = "paiements", allowSetters = true)
	private Facture facture;

    @Column(name = "external_transaction_id")
    private String externalTransactionId;

    @Column(name = "payment_message")
    private String paymentMessage;

	// jhipster-needle-entity-add-field - JHipster will add fields here
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

	public Paiement transactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	public Long getClientId() {
		return clientId;
	}

	public Paiement clientId(Long clientId) {
		this.clientId = clientId;
		return this;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

    public String getClientCode() {
        return clientCode;
    }

    public Paiement clientCode(String clientCode) {
        this.clientCode = clientCode;
        return this;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getNumeroVirtuel() {
        return numeroVirtuel;
    }

    public Paiement numeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
        return this;
    }

    public void setNumeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
    }

    public String getNumeroFreemoney() {
        return numeroFreemoney;
    }

    public Paiement numeroFreemoney(String numeroFreemoney) {
        this.numeroFreemoney = numeroFreemoney;
        return this;
    }

    public void setNumeroFreemoney(String numeroFreemoney) {
        this.numeroFreemoney = numeroFreemoney;
    }

    public Double getAmount() {
		return amount;
	}

	public Paiement amount(Double amount) {
		this.amount = amount;
		return this;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Instant getDatePaiement() {
		return datePaiement;
	}

	public Paiement datePaiement(Instant datePaiement) {
		this.datePaiement = datePaiement;
		return this;
	}

	public void setDatePaiement(Instant datePaiement) {
		this.datePaiement = datePaiement;
	}

	public Integer getTrials() {
		return trials;
	}

	public Paiement trials(Integer trials) {
		this.trials = trials;
		return this;
	}

	public void setTrials(Integer trials) {
		this.trials = trials;
	}

	public ActionStatus getPaiementStatus() {
		return paiementStatus;
	}

	public Paiement paiementStatus(ActionStatus paiementStatus) {
		this.paiementStatus = paiementStatus;
		return this;
	}

	public void setPaiementStatus(ActionStatus paiementStatus) {
		this.paiementStatus = paiementStatus;
	}

	public Adjustment getAdjustment() {
		return adjustment;
	}

	public Paiement adjustment(Adjustment adjustment) {
		this.adjustment = adjustment;
		return this;
	}

	public void setAdjustment(Adjustment adjustment) {
		this.adjustment = adjustment;
	}

	public Facture getFacture() {
		return facture;
	}

	public Paiement facture(Facture facture) {
		this.facture = facture;
		return this;
	}

	public void setFacture(Facture facture) {
		this.facture = facture;
	}

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public Paiement externalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
        return this;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public Paiement paymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
        return this;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Paiement)) {
			return false;
		}
		return id != null && id.equals(((Paiement) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + id +
            ", transactionId='" + transactionId + '\'' +
            ", clientId=" + clientId +
            ", clientCode='" + clientCode + '\'' +
            ", numeroVirtuel='" + numeroVirtuel + '\'' +
            ", numeroFreemoney='" + numeroFreemoney + '\'' +
            ", amount=" + amount +
            ", datePaiement=" + datePaiement +
            ", trials=" + trials +
            ", paiementStatus=" + paiementStatus +
            ", adjustment=" + adjustment +
            ", facture=" + facture +
            ", externalTransactionId='" + externalTransactionId + '\'' +
            ", paymentMessage='" + paymentMessage + '\'' +
            '}';
    }
}
