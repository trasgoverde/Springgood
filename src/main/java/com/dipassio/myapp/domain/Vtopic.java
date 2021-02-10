package com.dipassio.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vtopic.
 */
@Entity
@Table(name = "vtopic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vtopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "vtopic_title", length = 50, nullable = false)
    private String vtopicTitle;

    @Size(min = 2, max = 250)
    @Column(name = "vtopic_description", length = 250)
    private String vtopicDescription;

    @OneToMany(mappedBy = "vtopic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> appusers = new HashSet<>();

    @OneToMany(mappedBy = "vtopic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Vquestion> vquestions = new HashSet<>();

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

    public Vtopic creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getVtopicTitle() {
        return vtopicTitle;
    }

    public Vtopic vtopicTitle(String vtopicTitle) {
        this.vtopicTitle = vtopicTitle;
        return this;
    }

    public void setVtopicTitle(String vtopicTitle) {
        this.vtopicTitle = vtopicTitle;
    }

    public String getVtopicDescription() {
        return vtopicDescription;
    }

    public Vtopic vtopicDescription(String vtopicDescription) {
        this.vtopicDescription = vtopicDescription;
        return this;
    }

    public void setVtopicDescription(String vtopicDescription) {
        this.vtopicDescription = vtopicDescription;
    }

    public Set<Appuser> getAppusers() {
        return appusers;
    }

    public Vtopic appusers(Set<Appuser> appusers) {
        this.appusers = appusers;
        return this;
    }

    public Vtopic addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.setVtopic(this);
        return this;
    }

    public Vtopic removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.setVtopic(null);
        return this;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }

    public Set<Vquestion> getVquestions() {
        return vquestions;
    }

    public Vtopic vquestions(Set<Vquestion> vquestions) {
        this.vquestions = vquestions;
        return this;
    }

    public Vtopic addVquestion(Vquestion vquestion) {
        this.vquestions.add(vquestion);
        vquestion.setVtopic(this);
        return this;
    }

    public Vtopic removeVquestion(Vquestion vquestion) {
        this.vquestions.remove(vquestion);
        vquestion.setVtopic(null);
        return this;
    }

    public void setVquestions(Set<Vquestion> vquestions) {
        this.vquestions = vquestions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vtopic)) {
            return false;
        }
        return id != null && id.equals(((Vtopic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vtopic{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vtopicTitle='" + getVtopicTitle() + "'" +
            ", vtopicDescription='" + getVtopicDescription() + "'" +
            "}";
    }
}
