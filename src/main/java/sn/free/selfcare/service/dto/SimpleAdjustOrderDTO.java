package sn.free.selfcare.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class SimpleAdjustOrderDTO implements Serializable {

	@NotNull
	private String numeroClient;

	@NotNull
	private Double stock;

	private String countryCode;

	public String getNumeroClient() {
		return numeroClient;
	}

	public void setNumeroClient(String numeroClient) {
		this.numeroClient = numeroClient;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

	@Override
	public int hashCode() {
        int result = numeroClient.hashCode();
        result = 31 * result + stock.hashCode();
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleAdjustOrderDTO)) return false;

        SimpleAdjustOrderDTO that = (SimpleAdjustOrderDTO) o;

        if (!numeroClient.equals(that.numeroClient)) return false;
        if (!stock.equals(that.stock)) return false;
        return countryCode != null ? countryCode.equals(that.countryCode) : that.countryCode == null;
	}

	@Override
	public String toString() {
        return "SimpleAdjustOrderDTO{" +
            "numeroClient='" + numeroClient + '\'' +
            ", stock=" + stock +
            ", countryCode='" + countryCode + '\'' +
            '}';
	}
}
