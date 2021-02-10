package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Vtopic;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.repository.VtopicRepository;

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
 * Integration tests for the {@link VtopicResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VtopicResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VTOPIC_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_VTOPIC_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VTOPIC_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_VTOPIC_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private VtopicRepository vtopicRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVtopicMockMvc;

    private Vtopic vtopic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vtopic createEntity(EntityManager em) {
        Vtopic vtopic = new Vtopic()
            .creationDate(DEFAULT_CREATION_DATE)
            .vtopicTitle(DEFAULT_VTOPIC_TITLE)
            .vtopicDescription(DEFAULT_VTOPIC_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vtopic.getAppusers().add(appuser);
        return vtopic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vtopic createUpdatedEntity(EntityManager em) {
        Vtopic vtopic = new Vtopic()
            .creationDate(UPDATED_CREATION_DATE)
            .vtopicTitle(UPDATED_VTOPIC_TITLE)
            .vtopicDescription(UPDATED_VTOPIC_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vtopic.getAppusers().add(appuser);
        return vtopic;
    }

    @BeforeEach
    public void initTest() {
        vtopic = createEntity(em);
    }

    @Test
    @Transactional
    public void createVtopic() throws Exception {
        int databaseSizeBeforeCreate = vtopicRepository.findAll().size();
        // Create the Vtopic
        restVtopicMockMvc.perform(post("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vtopic)))
            .andExpect(status().isCreated());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeCreate + 1);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(DEFAULT_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(DEFAULT_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createVtopicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vtopicRepository.findAll().size();

        // Create the Vtopic with an existing ID
        vtopic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVtopicMockMvc.perform(post("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vtopic)))
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vtopicRepository.findAll().size();
        // set the field null
        vtopic.setCreationDate(null);

        // Create the Vtopic, which fails.


        restVtopicMockMvc.perform(post("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vtopic)))
            .andExpect(status().isBadRequest());

        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVtopicTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vtopicRepository.findAll().size();
        // set the field null
        vtopic.setVtopicTitle(null);

        // Create the Vtopic, which fails.


        restVtopicMockMvc.perform(post("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vtopic)))
            .andExpect(status().isBadRequest());

        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVtopics() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList
        restVtopicMockMvc.perform(get("/api/vtopics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vtopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vtopicTitle").value(hasItem(DEFAULT_VTOPIC_TITLE)))
            .andExpect(jsonPath("$.[*].vtopicDescription").value(hasItem(DEFAULT_VTOPIC_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get the vtopic
        restVtopicMockMvc.perform(get("/api/vtopics/{id}", vtopic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vtopic.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vtopicTitle").value(DEFAULT_VTOPIC_TITLE))
            .andExpect(jsonPath("$.vtopicDescription").value(DEFAULT_VTOPIC_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingVtopic() throws Exception {
        // Get the vtopic
        restVtopicMockMvc.perform(get("/api/vtopics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();

        // Update the vtopic
        Vtopic updatedVtopic = vtopicRepository.findById(vtopic.getId()).get();
        // Disconnect from session so that the updates on updatedVtopic are not directly saved in db
        em.detach(updatedVtopic);
        updatedVtopic
            .creationDate(UPDATED_CREATION_DATE)
            .vtopicTitle(UPDATED_VTOPIC_TITLE)
            .vtopicDescription(UPDATED_VTOPIC_DESCRIPTION);

        restVtopicMockMvc.perform(put("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVtopic)))
            .andExpect(status().isOk());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(UPDATED_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVtopicMockMvc.perform(put("/api/vtopics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vtopic)))
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeDelete = vtopicRepository.findAll().size();

        // Delete the vtopic
        restVtopicMockMvc.perform(delete("/api/vtopics/{id}", vtopic.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
