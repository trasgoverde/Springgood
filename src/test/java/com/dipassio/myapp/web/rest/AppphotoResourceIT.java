package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Appphoto;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.repository.AppphotoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AppphotoResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppphotoResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private AppphotoRepository appphotoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppphotoMockMvc;

    private Appphoto appphoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appphoto createEntity(EntityManager em) {
        Appphoto appphoto = new Appphoto()
            .creationDate(DEFAULT_CREATION_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appphoto.setAppuser(appuser);
        return appphoto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appphoto createUpdatedEntity(EntityManager em) {
        Appphoto appphoto = new Appphoto()
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appphoto.setAppuser(appuser);
        return appphoto;
    }

    @BeforeEach
    public void initTest() {
        appphoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppphoto() throws Exception {
        int databaseSizeBeforeCreate = appphotoRepository.findAll().size();
        // Create the Appphoto
        restAppphotoMockMvc.perform(post("/api/appphotos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appphoto)))
            .andExpect(status().isCreated());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeCreate + 1);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createAppphotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appphotoRepository.findAll().size();

        // Create the Appphoto with an existing ID
        appphoto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppphotoMockMvc.perform(post("/api/appphotos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appphoto)))
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appphotoRepository.findAll().size();
        // set the field null
        appphoto.setCreationDate(null);

        // Create the Appphoto, which fails.


        restAppphotoMockMvc.perform(post("/api/appphotos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appphoto)))
            .andExpect(status().isBadRequest());

        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppphotos() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList
        restAppphotoMockMvc.perform(get("/api/appphotos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appphoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get the appphoto
        restAppphotoMockMvc.perform(get("/api/appphotos/{id}", appphoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appphoto.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }
    @Test
    @Transactional
    public void getNonExistingAppphoto() throws Exception {
        // Get the appphoto
        restAppphotoMockMvc.perform(get("/api/appphotos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();

        // Update the appphoto
        Appphoto updatedAppphoto = appphotoRepository.findById(appphoto.getId()).get();
        // Disconnect from session so that the updates on updatedAppphoto are not directly saved in db
        em.detach(updatedAppphoto);
        updatedAppphoto
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAppphotoMockMvc.perform(put("/api/appphotos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppphoto)))
            .andExpect(status().isOk());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppphotoMockMvc.perform(put("/api/appphotos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appphoto)))
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeDelete = appphotoRepository.findAll().size();

        // Delete the appphoto
        restAppphotoMockMvc.perform(delete("/api/appphotos/{id}", appphoto.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
