package sn.free.selfcare.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;

/**
 * A DTO for the {@link sn.free.selfcare.domain.Payment} entity.
 */
@ApiModel(description = "Payment entity.\n@author MSF")
public class PaymentRequestDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private String invoiceNumber;

	private String b2bmsisdn;

	@NotNull
	private String customermsisdn;//Numero freemoney

    @NotNull
	private Long clientId;

	private String numeroVirtuel;

	@NotNull
	private String customerAccount;//Client code

	private String currency;

	@NotNull
	private Double amount;

	@NotNull
	private String externaltransactionid;

	private String paymentMethod;

	private String b2buserName;

	private String b2bPassword;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getB2bmsisdn() {
        return b2bmsisdn;
    }

    public void setB2bmsisdn(String b2bmsisdn) {
        this.b2bmsisdn = b2bmsisdn;
    }

    public String getCustomermsisdn() {
        return customermsisdn;
    }

    public void setCustomermsisdn(String customermsisdn) {
        this.customermsisdn = customermsisdn;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNumeroVirtuel() {
        return numeroVirtuel;
    }

    public void setNumeroVirtuel(String numeroVirtuel) {
        this.numeroVirtuel = numeroVirtuel;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getExternaltransactionid() {
        return externaltransactionid;
    }

    public void setExternaltransactionid(String externaltransactionid) {
        this.externaltransactionid = externaltransactionid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getB2buserName() {
        return b2buserName;
    }

    public void setB2buserName(String b2buserName) {
        this.b2buserName = b2buserName;
    }

    public String getB2bPassword() {
        return b2bPassword;
    }

    public void setB2bPassword(String b2bPassword) {
        this.b2bPassword = b2bPassword;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PaymentRequestDTO)) {
			return false;
		}

		return externaltransactionid != null && externaltransactionid.equals(((PaymentRequestDTO) o).externaltransactionid);
	}

	@Override
	public int hashCode() {
		return 31;
	}

    @Override
    public String toString() {
        return "PaymentRequestDTO{" +
            "invoiceNumber='" + invoiceNumber + '\'' +
            ", b2bmsisdn='" + b2bmsisdn + '\'' +
            ", customermsisdn='" + customermsisdn + '\'' +
            ", clientId=" + clientId +
            ", numeroVirtuel='" + numeroVirtuel + '\'' +
            ", customerAccount='" + customerAccount + '\'' +
            ", currency='" + currency + '\'' +
            ", amount=" + amount +
            ", externaltransactionid='" + externaltransactionid + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", b2buserName='" + b2buserName + '\'' +
            ", b2bPassword='" + b2bPassword + '\'' +
            '}';
    }
}
