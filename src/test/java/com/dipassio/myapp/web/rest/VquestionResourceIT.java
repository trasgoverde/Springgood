package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Vquestion;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.domain.Vtopic;
import com.dipassio.myapp.repository.VquestionRepository;

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
 * Integration tests for the {@link VquestionResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VquestionResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VQUESTION = "AAAAAAAAAA";
    private static final String UPDATED_VQUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_VQUESTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_VQUESTION_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private VquestionRepository vquestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVquestionMockMvc;

    private Vquestion vquestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vquestion createEntity(EntityManager em) {
        Vquestion vquestion = new Vquestion()
            .creationDate(DEFAULT_CREATION_DATE)
            .vquestion(DEFAULT_VQUESTION)
            .vquestionDescription(DEFAULT_VQUESTION_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vquestion.getAppusers().add(appuser);
        // Add required entity
        Vtopic vtopic;
        if (TestUtil.findAll(em, Vtopic.class).isEmpty()) {
            vtopic = VtopicResourceIT.createEntity(em);
            em.persist(vtopic);
            em.flush();
        } else {
            vtopic = TestUtil.findAll(em, Vtopic.class).get(0);
        }
        vquestion.setVtopic(vtopic);
        return vquestion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vquestion createUpdatedEntity(EntityManager em) {
        Vquestion vquestion = new Vquestion()
            .creationDate(UPDATED_CREATION_DATE)
            .vquestion(UPDATED_VQUESTION)
            .vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vquestion.getAppusers().add(appuser);
        // Add required entity
        Vtopic vtopic;
        if (TestUtil.findAll(em, Vtopic.class).isEmpty()) {
            vtopic = VtopicResourceIT.createUpdatedEntity(em);
            em.persist(vtopic);
            em.flush();
        } else {
            vtopic = TestUtil.findAll(em, Vtopic.class).get(0);
        }
        vquestion.setVtopic(vtopic);
        return vquestion;
    }

    @BeforeEach
    public void initTest() {
        vquestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createVquestion() throws Exception {
        int databaseSizeBeforeCreate = vquestionRepository.findAll().size();
        // Create the Vquestion
        restVquestionMockMvc.perform(post("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vquestion)))
            .andExpect(status().isCreated());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeCreate + 1);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(DEFAULT_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(DEFAULT_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createVquestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vquestionRepository.findAll().size();

        // Create the Vquestion with an existing ID
        vquestion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVquestionMockMvc.perform(post("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vquestion)))
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vquestionRepository.findAll().size();
        // set the field null
        vquestion.setCreationDate(null);

        // Create the Vquestion, which fails.


        restVquestionMockMvc.perform(post("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vquestion)))
            .andExpect(status().isBadRequest());

        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVquestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vquestionRepository.findAll().size();
        // set the field null
        vquestion.setVquestion(null);

        // Create the Vquestion, which fails.


        restVquestionMockMvc.perform(post("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vquestion)))
            .andExpect(status().isBadRequest());

        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVquestions() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList
        restVquestionMockMvc.perform(get("/api/vquestions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vquestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vquestion").value(hasItem(DEFAULT_VQUESTION)))
            .andExpect(jsonPath("$.[*].vquestionDescription").value(hasItem(DEFAULT_VQUESTION_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get the vquestion
        restVquestionMockMvc.perform(get("/api/vquestions/{id}", vquestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vquestion.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vquestion").value(DEFAULT_VQUESTION))
            .andExpect(jsonPath("$.vquestionDescription").value(DEFAULT_VQUESTION_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingVquestion() throws Exception {
        // Get the vquestion
        restVquestionMockMvc.perform(get("/api/vquestions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();

        // Update the vquestion
        Vquestion updatedVquestion = vquestionRepository.findById(vquestion.getId()).get();
        // Disconnect from session so that the updates on updatedVquestion are not directly saved in db
        em.detach(updatedVquestion);
        updatedVquestion
            .creationDate(UPDATED_CREATION_DATE)
            .vquestion(UPDATED_VQUESTION)
            .vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);

        restVquestionMockMvc.perform(put("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVquestion)))
            .andExpect(status().isOk());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(UPDATED_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVquestionMockMvc.perform(put("/api/vquestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vquestion)))
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeDelete = vquestionRepository.findAll().size();

        // Delete the vquestion
        restVquestionMockMvc.perform(delete("/api/vquestions/{id}", vquestion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
