package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Vanswer;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.domain.Vquestion;
import com.dipassio.myapp.repository.VanswerRepository;

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
 * Integration tests for the {@link VanswerResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VanswerResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL_VANSWER = "AAAAAAAAAA";
    private static final String UPDATED_URL_VANSWER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    @Autowired
    private VanswerRepository vanswerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVanswerMockMvc;

    private Vanswer vanswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vanswer createEntity(EntityManager em) {
        Vanswer vanswer = new Vanswer()
            .creationDate(DEFAULT_CREATION_DATE)
            .urlVanswer(DEFAULT_URL_VANSWER)
            .accepted(DEFAULT_ACCEPTED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vanswer.getAppusers().add(appuser);
        // Add required entity
        Vquestion vquestion;
        if (TestUtil.findAll(em, Vquestion.class).isEmpty()) {
            vquestion = VquestionResourceIT.createEntity(em);
            em.persist(vquestion);
            em.flush();
        } else {
            vquestion = TestUtil.findAll(em, Vquestion.class).get(0);
        }
        vanswer.setVquestion(vquestion);
        return vanswer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vanswer createUpdatedEntity(EntityManager em) {
        Vanswer vanswer = new Vanswer()
            .creationDate(UPDATED_CREATION_DATE)
            .urlVanswer(UPDATED_URL_VANSWER)
            .accepted(UPDATED_ACCEPTED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vanswer.getAppusers().add(appuser);
        // Add required entity
        Vquestion vquestion;
        if (TestUtil.findAll(em, Vquestion.class).isEmpty()) {
            vquestion = VquestionResourceIT.createUpdatedEntity(em);
            em.persist(vquestion);
            em.flush();
        } else {
            vquestion = TestUtil.findAll(em, Vquestion.class).get(0);
        }
        vanswer.setVquestion(vquestion);
        return vanswer;
    }

    @BeforeEach
    public void initTest() {
        vanswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createVanswer() throws Exception {
        int databaseSizeBeforeCreate = vanswerRepository.findAll().size();
        // Create the Vanswer
        restVanswerMockMvc.perform(post("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vanswer)))
            .andExpect(status().isCreated());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeCreate + 1);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(DEFAULT_URL_VANSWER);
        assertThat(testVanswer.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
    }

    @Test
    @Transactional
    public void createVanswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vanswerRepository.findAll().size();

        // Create the Vanswer with an existing ID
        vanswer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVanswerMockMvc.perform(post("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vanswer)))
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vanswerRepository.findAll().size();
        // set the field null
        vanswer.setCreationDate(null);

        // Create the Vanswer, which fails.


        restVanswerMockMvc.perform(post("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vanswer)))
            .andExpect(status().isBadRequest());

        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlVanswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = vanswerRepository.findAll().size();
        // set the field null
        vanswer.setUrlVanswer(null);

        // Create the Vanswer, which fails.


        restVanswerMockMvc.perform(post("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vanswer)))
            .andExpect(status().isBadRequest());

        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVanswers() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList
        restVanswerMockMvc.perform(get("/api/vanswers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vanswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].urlVanswer").value(hasItem(DEFAULT_URL_VANSWER)))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get the vanswer
        restVanswerMockMvc.perform(get("/api/vanswers/{id}", vanswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vanswer.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.urlVanswer").value(DEFAULT_URL_VANSWER))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingVanswer() throws Exception {
        // Get the vanswer
        restVanswerMockMvc.perform(get("/api/vanswers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();

        // Update the vanswer
        Vanswer updatedVanswer = vanswerRepository.findById(vanswer.getId()).get();
        // Disconnect from session so that the updates on updatedVanswer are not directly saved in db
        em.detach(updatedVanswer);
        updatedVanswer
            .creationDate(UPDATED_CREATION_DATE)
            .urlVanswer(UPDATED_URL_VANSWER)
            .accepted(UPDATED_ACCEPTED);

        restVanswerMockMvc.perform(put("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVanswer)))
            .andExpect(status().isOk());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(UPDATED_URL_VANSWER);
        assertThat(testVanswer.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void updateNonExistingVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVanswerMockMvc.perform(put("/api/vanswers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vanswer)))
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeDelete = vanswerRepository.findAll().size();

        // Delete the vanswer
        restVanswerMockMvc.perform(delete("/api/vanswers/{id}", vanswer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
