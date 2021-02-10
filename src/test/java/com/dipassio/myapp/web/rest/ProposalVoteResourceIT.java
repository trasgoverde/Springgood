package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.ProposalVote;
import com.dipassio.myapp.repository.ProposalVoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProposalVoteResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProposalVoteResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VOTE_POINTS = 1L;
    private static final Long UPDATED_VOTE_POINTS = 2L;

    @Autowired
    private ProposalVoteRepository proposalVoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalVoteMockMvc;

    private ProposalVote proposalVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalVote createEntity(EntityManager em) {
        ProposalVote proposalVote = new ProposalVote()
            .creationDate(DEFAULT_CREATION_DATE)
            .votePoints(DEFAULT_VOTE_POINTS);
        return proposalVote;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalVote createUpdatedEntity(EntityManager em) {
        ProposalVote proposalVote = new ProposalVote()
            .creationDate(UPDATED_CREATION_DATE)
            .votePoints(UPDATED_VOTE_POINTS);
        return proposalVote;
    }

    @BeforeEach
    public void initTest() {
        proposalVote = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposalVote() throws Exception {
        int databaseSizeBeforeCreate = proposalVoteRepository.findAll().size();
        // Create the ProposalVote
        restProposalVoteMockMvc.perform(post("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposalVote)))
            .andExpect(status().isCreated());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeCreate + 1);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(DEFAULT_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void createProposalVoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalVoteRepository.findAll().size();

        // Create the ProposalVote with an existing ID
        proposalVote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalVoteMockMvc.perform(post("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposalVote)))
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalVoteRepository.findAll().size();
        // set the field null
        proposalVote.setCreationDate(null);

        // Create the ProposalVote, which fails.


        restProposalVoteMockMvc.perform(post("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposalVote)))
            .andExpect(status().isBadRequest());

        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVotePointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalVoteRepository.findAll().size();
        // set the field null
        proposalVote.setVotePoints(null);

        // Create the ProposalVote, which fails.


        restProposalVoteMockMvc.perform(post("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposalVote)))
            .andExpect(status().isBadRequest());

        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposalVotes() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList
        restProposalVoteMockMvc.perform(get("/api/proposal-votes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));
    }
    
    @Test
    @Transactional
    public void getProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get the proposalVote
        restProposalVoteMockMvc.perform(get("/api/proposal-votes/{id}", proposalVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposalVote.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.votePoints").value(DEFAULT_VOTE_POINTS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingProposalVote() throws Exception {
        // Get the proposalVote
        restProposalVoteMockMvc.perform(get("/api/proposal-votes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();

        // Update the proposalVote
        ProposalVote updatedProposalVote = proposalVoteRepository.findById(proposalVote.getId()).get();
        // Disconnect from session so that the updates on updatedProposalVote are not directly saved in db
        em.detach(updatedProposalVote);
        updatedProposalVote
            .creationDate(UPDATED_CREATION_DATE)
            .votePoints(UPDATED_VOTE_POINTS);

        restProposalVoteMockMvc.perform(put("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposalVote)))
            .andExpect(status().isOk());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc.perform(put("/api/proposal-votes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposalVote)))
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeDelete = proposalVoteRepository.findAll().size();

        // Delete the proposalVote
        restProposalVoteMockMvc.perform(delete("/api/proposal-votes/{id}", proposalVote.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
