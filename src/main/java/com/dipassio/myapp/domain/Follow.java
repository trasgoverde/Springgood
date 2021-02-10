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
 * A Follow.
 */
@Entity
@Table(name = "follow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @OneToMany(mappedBy = "followed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> followeds = new HashSet<>();

    @OneToMany(mappedBy = "following")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> followings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "cfolloweds", allowSetters = true)
    private Community cfollowed;

    @ManyToOne
    @JsonIgnoreProperties(value = "cfollowings", allowSetters = true)
    private Community cfollowing;

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

    public Follow creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Appuser> getFolloweds() {
        return followeds;
    }

    public Follow followeds(Set<Appuser> appusers) {
        this.followeds = appusers;
        return this;
    }

    public Follow addFollowed(Appuser appuser) {
        this.followeds.add(appuser);
        appuser.setFollowed(this);
        return this;
    }

    public Follow removeFollowed(Appuser appuser) {
        this.followeds.remove(appuser);
        appuser.setFollowed(null);
        return this;
    }

    public void setFolloweds(Set<Appuser> appusers) {
        this.followeds = appusers;
    }

    public Set<Appuser> getFollowings() {
        return followings;
    }

    public Follow followings(Set<Appuser> appusers) {
        this.followings = appusers;
        return this;
    }

    public Follow addFollowing(Appuser appuser) {
        this.followings.add(appuser);
        appuser.setFollowing(this);
        return this;
    }

    public Follow removeFollowing(Appuser appuser) {
        this.followings.remove(appuser);
        appuser.setFollowing(null);
        return this;
    }

    public void setFollowings(Set<Appuser> appusers) {
        this.followings = appusers;
    }

    public Community getCfollowed() {
        return cfollowed;
    }

    public Follow cfollowed(Community community) {
        this.cfollowed = community;
        return this;
    }

    public void setCfollowed(Community community) {
        this.cfollowed = community;
    }

    public Community getCfollowing() {
        return cfollowing;
    }

    public Follow cfollowing(Community community) {
        this.cfollowing = community;
        return this;
    }

    public void setCfollowing(Community community) {
        this.cfollowing = community;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Follow)) {
            return false;
        }
        return id != null && id.equals(((Follow) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Follow{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
