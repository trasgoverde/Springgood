package com.dipassio.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Bmessage.
 */
@Entity
@Table(name = "bmessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 8000)
    @Column(name = "bmessage_text", length = 8000, nullable = false)
    private String bmessageText;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Bmessage creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getBmessageText() {
        return bmessageText;
    }

    public Bmessage bmessageText(String bmessageText) {
        this.bmessageText = bmessageText;
        return this;
    }

    public void setBmessageText(String bmessageText) {
        this.bmessageText = bmessageText;
    }

    public Boolean isIsDelivered() {
        return isDelivered;
    }

    public Bmessage isDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
        return this;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bmessage)) {
            return false;
        }
        return id != null && id.equals(((Bmessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bmessage{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", bmessageText='" + getBmessageText() + "'" +
            ", isDelivered='" + isIsDelivered() + "'" +
            "}";
    }
}
