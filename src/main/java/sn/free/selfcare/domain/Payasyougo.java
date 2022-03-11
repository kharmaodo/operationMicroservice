package sn.free.selfcare.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * A Payasyougo.
 */
@Entity
@Table(name = "payasyougo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "payasyougo")
public class Payasyougo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero_client", nullable = false)
    private String numeroClient;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "country_code")
    private String countryCode;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @NotNull
    @Column(name = "customer_msisdn", nullable = false)
    private String customerMsisdn;

    @NotNull
    @Column(name = "customer_account", nullable = false)
    private String customerAccount;

    @Column(name = "currency")
    private String currency;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "date_paiement")
    private Instant datePaiement;

    @Column(name = "trials")
    private Integer trials;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "paiement_status", nullable = false)
    private ActionStatus paiementStatus;

    @Column(name = "paiement_message")
    private String paiementMessage;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroClient() {
        return numeroClient;
    }

    public Payasyougo numeroClient(String numeroClient) {
        this.numeroClient = numeroClient;
        return this;
    }

    public void setNumeroClient(String numeroClient) {
        this.numeroClient = numeroClient;
    }

    public Double getAmount() {
        return amount;
    }

    public Payasyougo amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Payasyougo countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Payasyougo invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerMsisdn() {
        return customerMsisdn;
    }

    public Payasyougo customerMsisdn(String customerMsisdn) {
        this.customerMsisdn = customerMsisdn;
        return this;
    }

    public void setCustomerMsisdn(String customerMsisdn) {
        this.customerMsisdn = customerMsisdn;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public Payasyougo customerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
        return this;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public Payasyougo currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Payasyougo transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Payasyougo paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public Payasyougo datePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
        return this;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Integer getTrials() {
        return trials;
    }

    public Payasyougo trials(Integer trials) {
        this.trials = trials;
        return this;
    }

    public void setTrials(Integer trials) {
        this.trials = trials;
    }

    public ActionStatus getPaiementStatus() {
        return paiementStatus;
    }

    public Payasyougo paiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
        return this;
    }

    public void setPaiementStatus(ActionStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    public String getPaiementMessage() {
        return paiementMessage;
    }

    public Payasyougo paiementMessage(String paiementMessage) {
        this.paiementMessage = paiementMessage;
        return this;
    }

    public void setPaiementMessage(String paiementMessage) {
        this.paiementMessage = paiementMessage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payasyougo)) {
            return false;
        }
        return id != null && id.equals(((Payasyougo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payasyougo{" +
            "id=" + getId() +
            ", numeroClient='" + getNumeroClient() + "'" +
            ", amount=" + getAmount() +
            ", countryCode='" + getCountryCode() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", customerMsisdn='" + getCustomerMsisdn() + "'" +
            ", customerAccount='" + getCustomerAccount() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", trials=" + getTrials() +
            ", paiementStatus='" + getPaiementStatus() + "'" +
            ", paiementMessage='" + getPaiementMessage() + "'" +
            "}";
    }
}
