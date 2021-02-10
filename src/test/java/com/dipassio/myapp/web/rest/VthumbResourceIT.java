package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Vthumb;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.repository.VthumbRepository;

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
 * Integration tests for the {@link VthumbResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VthumbResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_VTHUMB_UP = false;
    private static final Boolean UPDATED_VTHUMB_UP = true;

    private static final Boolean DEFAULT_VTHUMB_DOWN = false;
    private static final Boolean UPDATED_VTHUMB_DOWN = true;

    @Autowired
    private VthumbRepository vthumbRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVthumbMockMvc;

    private Vthumb vthumb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vthumb createEntity(EntityManager em) {
        Vthumb vthumb = new Vthumb()
            .creationDate(DEFAULT_CREATION_DATE)
            .vthumbUp(DEFAULT_VTHUMB_UP)
            .vthumbDown(DEFAULT_VTHUMB_DOWN);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vthumb.getAppusers().add(appuser);
        return vthumb;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vthumb createUpdatedEntity(EntityManager em) {
        Vthumb vthumb = new Vthumb()
            .creationDate(UPDATED_CREATION_DATE)
            .vthumbUp(UPDATED_VTHUMB_UP)
            .vthumbDown(UPDATED_VTHUMB_DOWN);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vthumb.getAppusers().add(appuser);
        return vthumb;
    }

    @BeforeEach
    public void initTest() {
        vthumb = createEntity(em);
    }

    @Test
    @Transactional
    public void createVthumb() throws Exception {
        int databaseSizeBeforeCreate = vthumbRepository.findAll().size();
        // Create the Vthumb
        restVthumbMockMvc.perform(post("/api/vthumbs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vthumb)))
            .andExpect(status().isCreated());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeCreate + 1);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVthumb.isVthumbUp()).isEqualTo(DEFAULT_VTHUMB_UP);
        assertThat(testVthumb.isVthumbDown()).isEqualTo(DEFAULT_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    public void createVthumbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vthumbRepository.findAll().size();

        // Create the Vthumb with an existing ID
        vthumb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVthumbMockMvc.perform(post("/api/vthumbs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vthumb)))
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vthumbRepository.findAll().size();
        // set the field null
        vthumb.setCreationDate(null);

        // Create the Vthumb, which fails.


        restVthumbMockMvc.perform(post("/api/vthumbs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vthumb)))
            .andExpect(status().isBadRequest());

        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVthumbs() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList
        restVthumbMockMvc.perform(get("/api/vthumbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vthumb.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vthumbUp").value(hasItem(DEFAULT_VTHUMB_UP.booleanValue())))
            .andExpect(jsonPath("$.[*].vthumbDown").value(hasItem(DEFAULT_VTHUMB_DOWN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get the vthumb
        restVthumbMockMvc.perform(get("/api/vthumbs/{id}", vthumb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vthumb.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vthumbUp").value(DEFAULT_VTHUMB_UP.booleanValue()))
            .andExpect(jsonPath("$.vthumbDown").value(DEFAULT_VTHUMB_DOWN.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingVthumb() throws Exception {
        // Get the vthumb
        restVthumbMockMvc.perform(get("/api/vthumbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();

        // Update the vthumb
        Vthumb updatedVthumb = vthumbRepository.findById(vthumb.getId()).get();
        // Disconnect from session so that the updates on updatedVthumb are not directly saved in db
        em.detach(updatedVthumb);
        updatedVthumb
            .creationDate(UPDATED_CREATION_DATE)
            .vthumbUp(UPDATED_VTHUMB_UP)
            .vthumbDown(UPDATED_VTHUMB_DOWN);

        restVthumbMockMvc.perform(put("/api/vthumbs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVthumb)))
            .andExpect(status().isOk());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVthumb.isVthumbUp()).isEqualTo(UPDATED_VTHUMB_UP);
        assertThat(testVthumb.isVthumbDown()).isEqualTo(UPDATED_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    public void updateNonExistingVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVthumbMockMvc.perform(put("/api/vthumbs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vthumb)))
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeDelete = vthumbRepository.findAll().size();

        // Delete the vthumb
        restVthumbMockMvc.perform(delete("/api/vthumbs/{id}", vthumb.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
