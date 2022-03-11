package sn.free.selfcare.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class AdjustOrderDTO implements Serializable {

	@NotNull
	private String destinataire;


	private Long clientId;

    private String clientCode;

	@NotNull
	private String numeroClient;

	@NotNull
    private String emailAdminClient;

	@NotNull
	private Double prix;

	private Double credit;

	private Double sms;

	private Double minAppel;

	private Double data;

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getNumeroClient() {
		return numeroClient;
	}

	public void setNumeroClient(String numeroClient) {
		this.numeroClient = numeroClient;
	}

    public String getEmailAdminClient() {
        return emailAdminClient;
    }

    public void setEmailAdminClient(String emailAdminClient) {
        this.emailAdminClient = emailAdminClient;
    }

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getSms() {
		return sms;
	}

	public void setSms(Double sms) {
		this.sms = sms;
	}

	public Double getMinAppel() {
		return minAppel;
	}

	public void setMinAppel(Double minAppel) {
		this.minAppel = minAppel;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((destinataire == null) ? 0 : destinataire.hashCode());
		result = prime * result + ((minAppel == null) ? 0 : minAppel.hashCode());
		result = prime * result + ((numeroClient == null) ? 0 : numeroClient.hashCode());
		result = prime * result + ((prix == null) ? 0 : prix.hashCode());
		result = prime * result + ((sms == null) ? 0 : sms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AdjustOrderDTO other = (AdjustOrderDTO) obj;
		if (credit == null) {
			if (other.credit != null) return false;
		}
		else if (!credit.equals(other.credit)) return false;
		if (data == null) {
			if (other.data != null) return false;
		}
		else if (!data.equals(other.data)) return false;
		if (destinataire == null) {
			if (other.destinataire != null) return false;
		}
		else if (!destinataire.equals(other.destinataire)) return false;
		if (minAppel == null) {
			if (other.minAppel != null) return false;
		}
		else if (!minAppel.equals(other.minAppel)) return false;
		if (numeroClient == null) {
			if (other.numeroClient != null) return false;
		}
		else if (!numeroClient.equals(other.numeroClient)) return false;
		if (prix == null) {
			if (other.prix != null) return false;
		}
		else if (!prix.equals(other.prix)) return false;
		if (sms == null) {
			if (other.sms != null) return false;
		}
		else if (!sms.equals(other.sms)) return false;
		return true;
	}

    @Override
    public String toString() {
        return "AdjustOrderDTO{" +
            "destinataire='" + destinataire + '\'' +
            ", clientId=" + clientId +
            ", clientCode='" + clientCode + '\'' +
            ", numeroClient='" + numeroClient + '\'' +
            ", emailAdminClient='" + emailAdminClient + '\'' +
            ", prix=" + prix +
            ", credit=" + credit +
            ", sms=" + sms +
            ", minAppel=" + minAppel +
            ", data=" + data +
            '}';
    }
}
