package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Cmessage;
import com.dipassio.myapp.repository.CmessageRepository;

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
 * Integration tests for the {@link CmessageResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CmessageResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CMESSAGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CMESSAGE_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELIVERED = false;
    private static final Boolean UPDATED_IS_DELIVERED = true;

    @Autowired
    private CmessageRepository cmessageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCmessageMockMvc;

    private Cmessage cmessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmessage createEntity(EntityManager em) {
        Cmessage cmessage = new Cmessage()
            .creationDate(DEFAULT_CREATION_DATE)
            .cmessageText(DEFAULT_CMESSAGE_TEXT)
            .isDelivered(DEFAULT_IS_DELIVERED);
        return cmessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cmessage createUpdatedEntity(EntityManager em) {
        Cmessage cmessage = new Cmessage()
            .creationDate(UPDATED_CREATION_DATE)
            .cmessageText(UPDATED_CMESSAGE_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);
        return cmessage;
    }

    @BeforeEach
    public void initTest() {
        cmessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createCmessage() throws Exception {
        int databaseSizeBeforeCreate = cmessageRepository.findAll().size();
        // Create the Cmessage
        restCmessageMockMvc.perform(post("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmessage)))
            .andExpect(status().isCreated());

        // Validate the Cmessage in the database
        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeCreate + 1);
        Cmessage testCmessage = cmessageList.get(cmessageList.size() - 1);
        assertThat(testCmessage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCmessage.getCmessageText()).isEqualTo(DEFAULT_CMESSAGE_TEXT);
        assertThat(testCmessage.isIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void createCmessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cmessageRepository.findAll().size();

        // Create the Cmessage with an existing ID
        cmessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCmessageMockMvc.perform(post("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmessage)))
            .andExpect(status().isBadRequest());

        // Validate the Cmessage in the database
        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmessageRepository.findAll().size();
        // set the field null
        cmessage.setCreationDate(null);

        // Create the Cmessage, which fails.


        restCmessageMockMvc.perform(post("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmessage)))
            .andExpect(status().isBadRequest());

        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCmessageTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = cmessageRepository.findAll().size();
        // set the field null
        cmessage.setCmessageText(null);

        // Create the Cmessage, which fails.


        restCmessageMockMvc.perform(post("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmessage)))
            .andExpect(status().isBadRequest());

        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCmessages() throws Exception {
        // Initialize the database
        cmessageRepository.saveAndFlush(cmessage);

        // Get all the cmessageList
        restCmessageMockMvc.perform(get("/api/cmessages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cmessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].cmessageText").value(hasItem(DEFAULT_CMESSAGE_TEXT)))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCmessage() throws Exception {
        // Initialize the database
        cmessageRepository.saveAndFlush(cmessage);

        // Get the cmessage
        restCmessageMockMvc.perform(get("/api/cmessages/{id}", cmessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cmessage.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.cmessageText").value(DEFAULT_CMESSAGE_TEXT))
            .andExpect(jsonPath("$.isDelivered").value(DEFAULT_IS_DELIVERED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCmessage() throws Exception {
        // Get the cmessage
        restCmessageMockMvc.perform(get("/api/cmessages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCmessage() throws Exception {
        // Initialize the database
        cmessageRepository.saveAndFlush(cmessage);

        int databaseSizeBeforeUpdate = cmessageRepository.findAll().size();

        // Update the cmessage
        Cmessage updatedCmessage = cmessageRepository.findById(cmessage.getId()).get();
        // Disconnect from session so that the updates on updatedCmessage are not directly saved in db
        em.detach(updatedCmessage);
        updatedCmessage
            .creationDate(UPDATED_CREATION_DATE)
            .cmessageText(UPDATED_CMESSAGE_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);

        restCmessageMockMvc.perform(put("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCmessage)))
            .andExpect(status().isOk());

        // Validate the Cmessage in the database
        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeUpdate);
        Cmessage testCmessage = cmessageList.get(cmessageList.size() - 1);
        assertThat(testCmessage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCmessage.getCmessageText()).isEqualTo(UPDATED_CMESSAGE_TEXT);
        assertThat(testCmessage.isIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void updateNonExistingCmessage() throws Exception {
        int databaseSizeBeforeUpdate = cmessageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCmessageMockMvc.perform(put("/api/cmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmessage)))
            .andExpect(status().isBadRequest());

        // Validate the Cmessage in the database
        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCmessage() throws Exception {
        // Initialize the database
        cmessageRepository.saveAndFlush(cmessage);

        int databaseSizeBeforeDelete = cmessageRepository.findAll().size();

        // Delete the cmessage
        restCmessageMockMvc.perform(delete("/api/cmessages/{id}", cmessage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cmessage> cmessageList = cmessageRepository.findAll();
        assertThat(cmessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
