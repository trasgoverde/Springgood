package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Calbum;
import com.dipassio.myapp.domain.Community;
import com.dipassio.myapp.repository.CalbumRepository;

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
 * Integration tests for the {@link CalbumResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CalbumResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CalbumRepository calbumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalbumMockMvc;

    private Calbum calbum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calbum createEntity(EntityManager em) {
        Calbum calbum = new Calbum()
            .creationDate(DEFAULT_CREATION_DATE)
            .title(DEFAULT_TITLE);
        // Add required entity
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            community = CommunityResourceIT.createEntity(em);
            em.persist(community);
            em.flush();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        calbum.getCommunities().add(community);
        return calbum;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calbum createUpdatedEntity(EntityManager em) {
        Calbum calbum = new Calbum()
            .creationDate(UPDATED_CREATION_DATE)
            .title(UPDATED_TITLE);
        // Add required entity
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            community = CommunityResourceIT.createUpdatedEntity(em);
            em.persist(community);
            em.flush();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        calbum.getCommunities().add(community);
        return calbum;
    }

    @BeforeEach
    public void initTest() {
        calbum = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalbum() throws Exception {
        int databaseSizeBeforeCreate = calbumRepository.findAll().size();
        // Create the Calbum
        restCalbumMockMvc.perform(post("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calbum)))
            .andExpect(status().isCreated());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeCreate + 1);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCalbumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calbumRepository.findAll().size();

        // Create the Calbum with an existing ID
        calbum.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalbumMockMvc.perform(post("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calbum)))
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbumRepository.findAll().size();
        // set the field null
        calbum.setCreationDate(null);

        // Create the Calbum, which fails.


        restCalbumMockMvc.perform(post("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calbum)))
            .andExpect(status().isBadRequest());

        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbumRepository.findAll().size();
        // set the field null
        calbum.setTitle(null);

        // Create the Calbum, which fails.


        restCalbumMockMvc.perform(post("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calbum)))
            .andExpect(status().isBadRequest());

        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalbums() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList
        restCalbumMockMvc.perform(get("/api/calbums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
    
    @Test
    @Transactional
    public void getCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get the calbum
        restCalbumMockMvc.perform(get("/api/calbums/{id}", calbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calbum.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }
    @Test
    @Transactional
    public void getNonExistingCalbum() throws Exception {
        // Get the calbum
        restCalbumMockMvc.perform(get("/api/calbums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();

        // Update the calbum
        Calbum updatedCalbum = calbumRepository.findById(calbum.getId()).get();
        // Disconnect from session so that the updates on updatedCalbum are not directly saved in db
        em.detach(updatedCalbum);
        updatedCalbum
            .creationDate(UPDATED_CREATION_DATE)
            .title(UPDATED_TITLE);

        restCalbumMockMvc.perform(put("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalbum)))
            .andExpect(status().isOk());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalbumMockMvc.perform(put("/api/calbums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calbum)))
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeDelete = calbumRepository.findAll().size();

        // Delete the calbum
        restCalbumMockMvc.perform(delete("/api/calbums/{id}", calbum.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
