package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Proposal;
import com.dipassio.myapp.repository.ProposalRepository;

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

import com.dipassio.myapp.domain.enumeration.ProposalType;
import com.dipassio.myapp.domain.enumeration.ProposalRole;
/**
 * Integration tests for the {@link ProposalResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProposalResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PROPOSAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPOSAL_NAME = "BBBBBBBBBB";

    private static final ProposalType DEFAULT_PROPOSAL_TYPE = ProposalType.STUDY;
    private static final ProposalType UPDATED_PROPOSAL_TYPE = ProposalType.APPROVED;

    private static final ProposalRole DEFAULT_PROPOSAL_ROLE = ProposalRole.APPUSER;
    private static final ProposalRole UPDATED_PROPOSAL_ROLE = ProposalRole.ORGANIZER;

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_OPEN = false;
    private static final Boolean UPDATED_IS_OPEN = true;

    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .creationDate(DEFAULT_CREATION_DATE)
            .proposalName(DEFAULT_PROPOSAL_NAME)
            .proposalType(DEFAULT_PROPOSAL_TYPE)
            .proposalRole(DEFAULT_PROPOSAL_ROLE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .isOpen(DEFAULT_IS_OPEN)
            .isAccepted(DEFAULT_IS_ACCEPTED)
            .isPaid(DEFAULT_IS_PAID);
        return proposal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createUpdatedEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .creationDate(UPDATED_CREATION_DATE)
            .proposalName(UPDATED_PROPOSAL_NAME)
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isOpen(UPDATED_IS_OPEN)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);
        return proposal;
    }

    @BeforeEach
    public void initTest() {
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();
        // Create the Proposal
        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(DEFAULT_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(DEFAULT_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(DEFAULT_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testProposal.isIsOpen()).isEqualTo(DEFAULT_IS_OPEN);
        assertThat(testProposal.isIsAccepted()).isEqualTo(DEFAULT_IS_ACCEPTED);
        assertThat(testProposal.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void createProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // Create the Proposal with an existing ID
        proposal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setCreationDate(null);

        // Create the Proposal, which fails.


        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProposalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalName(null);

        // Create the Proposal, which fails.


        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProposalTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalType(null);

        // Create the Proposal, which fails.


        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProposalRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalRole(null);

        // Create the Proposal, which fails.


        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc.perform(get("/api/proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].proposalName").value(hasItem(DEFAULT_PROPOSAL_NAME)))
            .andExpect(jsonPath("$.[*].proposalType").value(hasItem(DEFAULT_PROPOSAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].proposalRole").value(hasItem(DEFAULT_PROPOSAL_ROLE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOpen").value(hasItem(DEFAULT_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.proposalName").value(DEFAULT_PROPOSAL_NAME))
            .andExpect(jsonPath("$.proposalType").value(DEFAULT_PROPOSAL_TYPE.toString()))
            .andExpect(jsonPath("$.proposalRole").value(DEFAULT_PROPOSAL_ROLE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.isOpen").value(DEFAULT_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findById(proposal.getId()).get();
        // Disconnect from session so that the updates on updatedProposal are not directly saved in db
        em.detach(updatedProposal);
        updatedProposal
            .creationDate(UPDATED_CREATION_DATE)
            .proposalName(UPDATED_PROPOSAL_NAME)
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isOpen(UPDATED_IS_OPEN)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);

        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposal)))
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(UPDATED_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(UPDATED_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(UPDATED_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProposal.isIsOpen()).isEqualTo(UPDATED_IS_OPEN);
        assertThat(testProposal.isIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testProposal.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Delete the proposal
        restProposalMockMvc.perform(delete("/api/proposals/{id}", proposal.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
