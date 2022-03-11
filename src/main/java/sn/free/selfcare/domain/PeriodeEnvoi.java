package sn.free.selfcare.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import sn.free.selfcare.domain.enumeration.ObjectStatus;

/**
 * PeriodeEnvoi entity.\n@author Ahmadou Diaw
 */
@Entity
@Table(name = "periode_envoi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "periodeenvoi")
public class PeriodeEnvoi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "expression", nullable = false)
    private String expression;

    @NotNull
    @Column(name = "groupe_id", nullable = false)
    private Long groupeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ObjectStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public PeriodeEnvoi expression(String expression) {
        this.expression = expression;
        return this;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Long getGroupeId() {
        return groupeId;
    }

    public PeriodeEnvoi groupeId(Long groupeId) {
        this.groupeId = groupeId;
        return this;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    public ObjectStatus getStatus() {
        return status;
    }

    public PeriodeEnvoi status(ObjectStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodeEnvoi)) {
            return false;
        }
        return id != null && id.equals(((PeriodeEnvoi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeEnvoi{" +
            "id=" + getId() +
            ", expression='" + getExpression() + "'" +
            ", groupeId=" + getGroupeId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
