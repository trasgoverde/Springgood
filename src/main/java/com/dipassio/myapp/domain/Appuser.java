package com.dipassio.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Appuser.
 */
@Entity
@Table(name = "appuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Appuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "assigned_votes_points")
    private Long assignedVotesPoints;

    @OneToOne(mappedBy = "appuser")
    @JsonIgnore
    private Appprofile appprofile;

    @OneToOne(mappedBy = "appuser")
    @JsonIgnore
    private Appphoto appphoto;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Community community;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Blog blog;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Notification notification;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Album album;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Comment comment;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Post post;

    @ManyToOne
    @JsonIgnoreProperties(value = "senders", allowSetters = true)
    private Cmessage sender;

    @ManyToOne
    @JsonIgnoreProperties(value = "receivers", allowSetters = true)
    private Cmessage receiver;

    @ManyToOne
    @JsonIgnoreProperties(value = "followeds", allowSetters = true)
    private Follow followed;

    @ManyToOne
    @JsonIgnoreProperties(value = "followings", allowSetters = true)
    private Follow following;

    @ManyToOne
    @JsonIgnoreProperties(value = "blockedusers", allowSetters = true)
    private Blockuser blockeduser;

    @ManyToOne
    @JsonIgnoreProperties(value = "blockingusers", allowSetters = true)
    private Blockuser blockinguser;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Vtopic vtopic;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Vquestion vquestion;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Vanswer vanswer;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Vthumb vthumb;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private Proposal proposal;

    @ManyToOne
    @JsonIgnoreProperties(value = "appusers", allowSetters = true)
    private ProposalVote proposalVote;

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Celeb> celebs = new HashSet<>();

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

    public Appuser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public Appuser assignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
        return this;
    }

    public void setAssignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public Appprofile getAppprofile() {
        return appprofile;
    }

    public Appuser appprofile(Appprofile appprofile) {
        this.appprofile = appprofile;
        return this;
    }

    public void setAppprofile(Appprofile appprofile) {
        this.appprofile = appprofile;
    }

    public Appphoto getAppphoto() {
        return appphoto;
    }

    public Appuser appphoto(Appphoto appphoto) {
        this.appphoto = appphoto;
        return this;
    }

    public void setAppphoto(Appphoto appphoto) {
        this.appphoto = appphoto;
    }

    public Community getCommunity() {
        return community;
    }

    public Appuser community(Community community) {
        this.community = community;
        return this;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Blog getBlog() {
        return blog;
    }

    public Appuser blog(Blog blog) {
        this.blog = blog;
        return this;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Notification getNotification() {
        return notification;
    }

    public Appuser notification(Notification notification) {
        this.notification = notification;
        return this;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Album getAlbum() {
        return album;
    }

    public Appuser album(Album album) {
        this.album = album;
        return this;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Comment getComment() {
        return comment;
    }

    public Appuser comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public Appuser post(Post post) {
        this.post = post;
        return this;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Cmessage getSender() {
        return sender;
    }

    public Appuser sender(Cmessage cmessage) {
        this.sender = cmessage;
        return this;
    }

    public void setSender(Cmessage cmessage) {
        this.sender = cmessage;
    }

    public Cmessage getReceiver() {
        return receiver;
    }

    public Appuser receiver(Cmessage cmessage) {
        this.receiver = cmessage;
        return this;
    }

    public void setReceiver(Cmessage cmessage) {
        this.receiver = cmessage;
    }

    public Follow getFollowed() {
        return followed;
    }

    public Appuser followed(Follow follow) {
        this.followed = follow;
        return this;
    }

    public void setFollowed(Follow follow) {
        this.followed = follow;
    }

    public Follow getFollowing() {
        return following;
    }

    public Appuser following(Follow follow) {
        this.following = follow;
        return this;
    }

    public void setFollowing(Follow follow) {
        this.following = follow;
    }

    public Blockuser getBlockeduser() {
        return blockeduser;
    }

    public Appuser blockeduser(Blockuser blockuser) {
        this.blockeduser = blockuser;
        return this;
    }

    public void setBlockeduser(Blockuser blockuser) {
        this.blockeduser = blockuser;
    }

    public Blockuser getBlockinguser() {
        return blockinguser;
    }

    public Appuser blockinguser(Blockuser blockuser) {
        this.blockinguser = blockuser;
        return this;
    }

    public void setBlockinguser(Blockuser blockuser) {
        this.blockinguser = blockuser;
    }

    public Vtopic getVtopic() {
        return vtopic;
    }

    public Appuser vtopic(Vtopic vtopic) {
        this.vtopic = vtopic;
        return this;
    }

    public void setVtopic(Vtopic vtopic) {
        this.vtopic = vtopic;
    }

    public Vquestion getVquestion() {
        return vquestion;
    }

    public Appuser vquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
        return this;
    }

    public void setVquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
    }

    public Vanswer getVanswer() {
        return vanswer;
    }

    public Appuser vanswer(Vanswer vanswer) {
        this.vanswer = vanswer;
        return this;
    }

    public void setVanswer(Vanswer vanswer) {
        this.vanswer = vanswer;
    }

    public Vthumb getVthumb() {
        return vthumb;
    }

    public Appuser vthumb(Vthumb vthumb) {
        this.vthumb = vthumb;
        return this;
    }

    public void setVthumb(Vthumb vthumb) {
        this.vthumb = vthumb;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public Appuser proposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public ProposalVote getProposalVote() {
        return proposalVote;
    }

    public Appuser proposalVote(ProposalVote proposalVote) {
        this.proposalVote = proposalVote;
        return this;
    }

    public void setProposalVote(ProposalVote proposalVote) {
        this.proposalVote = proposalVote;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public Appuser interests(Set<Interest> interests) {
        this.interests = interests;
        return this;
    }

    public Appuser addInterest(Interest interest) {
        this.interests.add(interest);
        interest.getAppusers().add(this);
        return this;
    }

    public Appuser removeInterest(Interest interest) {
        this.interests.remove(interest);
        interest.getAppusers().remove(this);
        return this;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public Appuser activities(Set<Activity> activities) {
        this.activities = activities;
        return this;
    }

    public Appuser addActivity(Activity activity) {
        this.activities.add(activity);
        activity.getAppusers().add(this);
        return this;
    }

    public Appuser removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.getAppusers().remove(this);
        return this;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Celeb> getCelebs() {
        return celebs;
    }

    public Appuser celebs(Set<Celeb> celebs) {
        this.celebs = celebs;
        return this;
    }

    public Appuser addCeleb(Celeb celeb) {
        this.celebs.add(celeb);
        celeb.getAppusers().add(this);
        return this;
    }

    public Appuser removeCeleb(Celeb celeb) {
        this.celebs.remove(celeb);
        celeb.getAppusers().remove(this);
        return this;
    }

    public void setCelebs(Set<Celeb> celebs) {
        this.celebs = celebs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appuser)) {
            return false;
        }
        return id != null && id.equals(((Appuser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appuser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", assignedVotesPoints=" + getAssignedVotesPoints() +
            "}";
    }
}
