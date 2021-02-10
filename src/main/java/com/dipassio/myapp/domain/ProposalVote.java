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
 * A ProposalVote.
 */
@Entity
@Table(name = "proposal_vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProposalVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(name = "vote_points", nullable = false)
    private Long votePoints;

    @OneToMany(mappedBy = "proposalVote")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Appuser> appusers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "proposalVotes", allowSetters = true)
    private Proposal proposal;

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

    public ProposalVote creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getVotePoints() {
        return votePoints;
    }

    public ProposalVote votePoints(Long votePoints) {
        this.votePoints = votePoints;
        return this;
    }

    public void setVotePoints(Long votePoints) {
        this.votePoints = votePoints;
    }

    public Set<Appuser> getAppusers() {
        return appusers;
    }

    public ProposalVote appusers(Set<Appuser> appusers) {
        this.appusers = appusers;
        return this;
    }

    public ProposalVote addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.setProposalVote(this);
        return this;
    }

    public ProposalVote removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.setProposalVote(null);
        return this;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public ProposalVote proposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalVote)) {
            return false;
        }
        return id != null && id.equals(((ProposalVote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalVote{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", votePoints=" + getVotePoints() +
            "}";
    }
}
