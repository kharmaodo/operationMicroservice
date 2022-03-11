package sn.free.selfcare.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import sn.free.selfcare.domain.enumeration.ActionStatus;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Payasyougo} entity.
 */
public class PayasyougoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String numeroClient;

    @NotNull
    private Double amount;

    private String countryCode;

    @NotNull
    private String invoiceNumber;

    @NotNull
    private String customerMsisdn;

    @NotNull
    private String customerAccount;

    private String currency;

    @NotNull
    private String transactionId;

    private String paymentMethod;

    private Instant datePaiement;

    private Integer trials;

    @NotNull
    private ActionStatus paiementStatus;

    private String paiementMessage;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroClient() {
        return numeroClient;
    }

    public void setNumeroClient(String numeroClient) {
        this.numeroClient = numeroClient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerMsisdn() {
        return customerMsisdn;
    }

    public void setCustomerMsisdn(String customerMsisdn) {
        this.customerMsisdn = customerMsisdn;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getPaiementMessage() {
        return paiementMessage;
    }

    public void setPaiementMessage(String paiementMessage) {
        this.paiementMessage = paiementMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayasyougoDTO)) {
            return false;
        }

        return id != null && id.equals(((PayasyougoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayasyougoDTO{" +
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
