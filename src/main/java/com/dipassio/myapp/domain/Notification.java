package com.dipassio.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.dipassio.myapp.domain.enumeration.NotificationReason;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "notification_date")
    private Instant notificationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_reason", nullable = false)
    private NotificationReason notificationReason;

    @Size(min = 2, max = 100)
    @Column(name = "notification_text", length = 100)
    private String notificationText;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @OneToMany(mappedBy = "notification")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> appusers = new HashSet<>();

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

    public Notification creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getNotificationDate() {
        return notificationDate;
    }

    public Notification notificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
        return this;
    }

    public void setNotificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
    }

    public NotificationReason getNotificationReason() {
        return notificationReason;
    }

    public Notification notificationReason(NotificationReason notificationReason) {
        this.notificationReason = notificationReason;
        return this;
    }

    public void setNotificationReason(NotificationReason notificationReason) {
        this.notificationReason = notificationReason;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public Notification notificationText(String notificationText) {
        this.notificationText = notificationText;
        return this;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public Boolean isIsDelivered() {
        return isDelivered;
    }

    public Notification isDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
        return this;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Set<Appuser> getAppusers() {
        return appusers;
    }

    public Notification appusers(Set<Appuser> appusers) {
        this.appusers = appusers;
        return this;
    }

    public Notification addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.setNotification(this);
        return this;
    }

    public Notification removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.setNotification(null);
        return this;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", notificationReason='" + getNotificationReason() + "'" +
            ", notificationText='" + getNotificationText() + "'" +
            ", isDelivered='" + isIsDelivered() + "'" +
            "}";
    }
}
