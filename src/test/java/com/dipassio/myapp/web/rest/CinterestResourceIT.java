package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Cinterest;
import com.dipassio.myapp.repository.CinterestRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CinterestResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CinterestResourceIT {

    private static final String DEFAULT_INTEREST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_NAME = "BBBBBBBBBB";

    @Autowired
    private CinterestRepository cinterestRepository;

    @Mock
    private CinterestRepository cinterestRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCinterestMockMvc;

    private Cinterest cinterest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinterest createEntity(EntityManager em) {
        Cinterest cinterest = new Cinterest()
            .interestName(DEFAULT_INTEREST_NAME);
        return cinterest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinterest createUpdatedEntity(EntityManager em) {
        Cinterest cinterest = new Cinterest()
            .interestName(UPDATED_INTEREST_NAME);
        return cinterest;
    }

    @BeforeEach
    public void initTest() {
        cinterest = createEntity(em);
    }

    @Test
    @Transactional
    public void createCinterest() throws Exception {
        int databaseSizeBeforeCreate = cinterestRepository.findAll().size();
        // Create the Cinterest
        restCinterestMockMvc.perform(post("/api/cinterests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cinterest)))
            .andExpect(status().isCreated());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeCreate + 1);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(DEFAULT_INTEREST_NAME);
    }

    @Test
    @Transactional
    public void createCinterestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cinterestRepository.findAll().size();

        // Create the Cinterest with an existing ID
        cinterest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCinterestMockMvc.perform(post("/api/cinterests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cinterest)))
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInterestNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cinterestRepository.findAll().size();
        // set the field null
        cinterest.setInterestName(null);

        // Create the Cinterest, which fails.


        restCinterestMockMvc.perform(post("/api/cinterests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cinterest)))
            .andExpect(status().isBadRequest());

        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCinterests() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get all the cinterestList
        restCinterestMockMvc.perform(get("/api/cinterests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cinterest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestName").value(hasItem(DEFAULT_INTEREST_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCinterestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cinterestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCinterestMockMvc.perform(get("/api/cinterests?eagerload=true"))
            .andExpect(status().isOk());

        verify(cinterestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCinterestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cinterestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCinterestMockMvc.perform(get("/api/cinterests?eagerload=true"))
            .andExpect(status().isOk());

        verify(cinterestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        // Get the cinterest
        restCinterestMockMvc.perform(get("/api/cinterests/{id}", cinterest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cinterest.getId().intValue()))
            .andExpect(jsonPath("$.interestName").value(DEFAULT_INTEREST_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCinterest() throws Exception {
        // Get the cinterest
        restCinterestMockMvc.perform(get("/api/cinterests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();

        // Update the cinterest
        Cinterest updatedCinterest = cinterestRepository.findById(cinterest.getId()).get();
        // Disconnect from session so that the updates on updatedCinterest are not directly saved in db
        em.detach(updatedCinterest);
        updatedCinterest
            .interestName(UPDATED_INTEREST_NAME);

        restCinterestMockMvc.perform(put("/api/cinterests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCinterest)))
            .andExpect(status().isOk());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
        Cinterest testCinterest = cinterestList.get(cinterestList.size() - 1);
        assertThat(testCinterest.getInterestName()).isEqualTo(UPDATED_INTEREST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCinterest() throws Exception {
        int databaseSizeBeforeUpdate = cinterestRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinterestMockMvc.perform(put("/api/cinterests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cinterest)))
            .andExpect(status().isBadRequest());

        // Validate the Cinterest in the database
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCinterest() throws Exception {
        // Initialize the database
        cinterestRepository.saveAndFlush(cinterest);

        int databaseSizeBeforeDelete = cinterestRepository.findAll().size();

        // Delete the cinterest
        restCinterestMockMvc.perform(delete("/api/cinterests/{id}", cinterest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cinterest> cinterestList = cinterestRepository.findAll();
        assertThat(cinterestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
