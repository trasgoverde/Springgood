package com.dipassio.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cmessage.
 */
@Entity
@Table(name = "cmessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 8000)
    @Column(name = "cmessage_text", length = 8000, nullable = false)
    private String cmessageText;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> senders = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> receivers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "csenders", allowSetters = true)
    private Community csender;

    @ManyToOne
    @JsonIgnoreProperties(value = "creceivers", allowSetters = true)
    private Community creceiver;

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

    public Cmessage creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCmessageText() {
        return cmessageText;
    }

    public Cmessage cmessageText(String cmessageText) {
        this.cmessageText = cmessageText;
        return this;
    }

    public void setCmessageText(String cmessageText) {
        this.cmessageText = cmessageText;
    }

    public Boolean isIsDelivered() {
        return isDelivered;
    }

    public Cmessage isDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
        return this;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Set<Appuser> getSenders() {
        return senders;
    }

    public Cmessage senders(Set<Appuser> appusers) {
        this.senders = appusers;
        return this;
    }

    public Cmessage addSender(Appuser appuser) {
        this.senders.add(appuser);
        appuser.setSender(this);
        return this;
    }

    public Cmessage removeSender(Appuser appuser) {
        this.senders.remove(appuser);
        appuser.setSender(null);
        return this;
    }

    public void setSenders(Set<Appuser> appusers) {
        this.senders = appusers;
    }

    public Set<Appuser> getReceivers() {
        return receivers;
    }

    public Cmessage receivers(Set<Appuser> appusers) {
        this.receivers = appusers;
        return this;
    }

    public Cmessage addReceiver(Appuser appuser) {
        this.receivers.add(appuser);
        appuser.setReceiver(this);
        return this;
    }

    public Cmessage removeReceiver(Appuser appuser) {
        this.receivers.remove(appuser);
        appuser.setReceiver(null);
        return this;
    }

    public void setReceivers(Set<Appuser> appusers) {
        this.receivers = appusers;
    }

    public Community getCsender() {
        return csender;
    }

    public Cmessage csender(Community community) {
        this.csender = community;
        return this;
    }

    public void setCsender(Community community) {
        this.csender = community;
    }

    public Community getCreceiver() {
        return creceiver;
    }

    public Cmessage creceiver(Community community) {
        this.creceiver = community;
        return this;
    }

    public void setCreceiver(Community community) {
        this.creceiver = community;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cmessage)) {
            return false;
        }
        return id != null && id.equals(((Cmessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cmessage{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", cmessageText='" + getCmessageText() + "'" +
            ", isDelivered='" + isIsDelivered() + "'" +
            "}";
    }
}
