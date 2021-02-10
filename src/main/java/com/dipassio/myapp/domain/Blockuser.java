package com.dipassio.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Blockuser.
 */
@Entity
@Table(name = "blockuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Blockuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @OneToMany(mappedBy = "blockeduser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> blockedusers = new HashSet<>();

    @OneToMany(mappedBy = "blockinguser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> blockingusers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "cblockedusers", allowSetters = true)
    private Community cblockeduser;

    @ManyToOne
    @JsonIgnoreProperties(value = "cblockingusers", allowSetters = true)
    private Community cblockinguser;

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

    public Blockuser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Appuser> getBlockedusers() {
        return blockedusers;
    }

    public Blockuser blockedusers(Set<Appuser> appusers) {
        this.blockedusers = appusers;
        return this;
    }

    public Blockuser addBlockeduser(Appuser appuser) {
        this.blockedusers.add(appuser);
        appuser.setBlockeduser(this);
        return this;
    }

    public Blockuser removeBlockeduser(Appuser appuser) {
        this.blockedusers.remove(appuser);
        appuser.setBlockeduser(null);
        return this;
    }

    public void setBlockedusers(Set<Appuser> appusers) {
        this.blockedusers = appusers;
    }

    public Set<Appuser> getBlockingusers() {
        return blockingusers;
    }

    public Blockuser blockingusers(Set<Appuser> appusers) {
        this.blockingusers = appusers;
        return this;
    }

    public Blockuser addBlockinguser(Appuser appuser) {
        this.blockingusers.add(appuser);
        appuser.setBlockinguser(this);
        return this;
    }

    public Blockuser removeBlockinguser(Appuser appuser) {
        this.blockingusers.remove(appuser);
        appuser.setBlockinguser(null);
        return this;
    }

    public void setBlockingusers(Set<Appuser> appusers) {
        this.blockingusers = appusers;
    }

    public Community getCblockeduser() {
        return cblockeduser;
    }

    public Blockuser cblockeduser(Community community) {
        this.cblockeduser = community;
        return this;
    }

    public void setCblockeduser(Community community) {
        this.cblockeduser = community;
    }

    public Community getCblockinguser() {
        return cblockinguser;
    }

    public Blockuser cblockinguser(Community community) {
        this.cblockinguser = community;
        return this;
    }

    public void setCblockinguser(Community community) {
        this.cblockinguser = community;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blockuser)) {
            return false;
        }
        return id != null && id.equals(((Blockuser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Blockuser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
