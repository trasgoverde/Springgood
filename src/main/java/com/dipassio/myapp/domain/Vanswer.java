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
 * A Vanswer.
 */
@Entity
@Table(name = "vanswer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vanswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 500)
    @Column(name = "url_vanswer", length = 500, nullable = false)
    private String urlVanswer;

    @Column(name = "accepted")
    private Boolean accepted;

    @OneToMany(mappedBy = "vanswer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> appusers = new HashSet<>();

    @OneToMany(mappedBy = "vanswer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Vthumb> vthumbs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "vanswers", allowSetters = true)
    private Vquestion vquestion;

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

    public Vanswer creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrlVanswer() {
        return urlVanswer;
    }

    public Vanswer urlVanswer(String urlVanswer) {
        this.urlVanswer = urlVanswer;
        return this;
    }

    public void setUrlVanswer(String urlVanswer) {
        this.urlVanswer = urlVanswer;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public Vanswer accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Set<Appuser> getAppusers() {
        return appusers;
    }

    public Vanswer appusers(Set<Appuser> appusers) {
        this.appusers = appusers;
        return this;
    }

    public Vanswer addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.setVanswer(this);
        return this;
    }

    public Vanswer removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.setVanswer(null);
        return this;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }

    public Set<Vthumb> getVthumbs() {
        return vthumbs;
    }

    public Vanswer vthumbs(Set<Vthumb> vthumbs) {
        this.vthumbs = vthumbs;
        return this;
    }

    public Vanswer addVthumb(Vthumb vthumb) {
        this.vthumbs.add(vthumb);
        vthumb.setVanswer(this);
        return this;
    }

    public Vanswer removeVthumb(Vthumb vthumb) {
        this.vthumbs.remove(vthumb);
        vthumb.setVanswer(null);
        return this;
    }

    public void setVthumbs(Set<Vthumb> vthumbs) {
        this.vthumbs = vthumbs;
    }

    public Vquestion getVquestion() {
        return vquestion;
    }

    public Vanswer vquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
        return this;
    }

    public void setVquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vanswer)) {
            return false;
        }
        return id != null && id.equals(((Vanswer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vanswer{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", urlVanswer='" + getUrlVanswer() + "'" +
            ", accepted='" + isAccepted() + "'" +
            "}";
    }
}
