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
 * A Calbum.
 */
@Entity
@Table(name = "calbum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Calbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @OneToMany(mappedBy = "calbum")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Community> communities = new HashSet<>();

    @OneToMany(mappedBy = "calbum")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Photo> photos = new HashSet<>();

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

    public Calbum creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public Calbum title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Community> getCommunities() {
        return communities;
    }

    public Calbum communities(Set<Community> communities) {
        this.communities = communities;
        return this;
    }

    public Calbum addCommunity(Community community) {
        this.communities.add(community);
        community.setCalbum(this);
        return this;
    }

    public Calbum removeCommunity(Community community) {
        this.communities.remove(community);
        community.setCalbum(null);
        return this;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public Calbum photos(Set<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Calbum addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setCalbum(this);
        return this;
    }

    public Calbum removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setCalbum(null);
        return this;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calbum)) {
            return false;
        }
        return id != null && id.equals(((Calbum) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calbum{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
