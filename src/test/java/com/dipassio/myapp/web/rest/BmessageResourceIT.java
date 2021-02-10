package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Bmessage;
import com.dipassio.myapp.repository.BmessageRepository;

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
 * Integration tests for the {@link BmessageResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BmessageResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BMESSAGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BMESSAGE_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELIVERED = false;
    private static final Boolean UPDATED_IS_DELIVERED = true;

    @Autowired
    private BmessageRepository bmessageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBmessageMockMvc;

    private Bmessage bmessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bmessage createEntity(EntityManager em) {
        Bmessage bmessage = new Bmessage()
            .creationDate(DEFAULT_CREATION_DATE)
            .bmessageText(DEFAULT_BMESSAGE_TEXT)
            .isDelivered(DEFAULT_IS_DELIVERED);
        return bmessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bmessage createUpdatedEntity(EntityManager em) {
        Bmessage bmessage = new Bmessage()
            .creationDate(UPDATED_CREATION_DATE)
            .bmessageText(UPDATED_BMESSAGE_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);
        return bmessage;
    }

    @BeforeEach
    public void initTest() {
        bmessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createBmessage() throws Exception {
        int databaseSizeBeforeCreate = bmessageRepository.findAll().size();
        // Create the Bmessage
        restBmessageMockMvc.perform(post("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bmessage)))
            .andExpect(status().isCreated());

        // Validate the Bmessage in the database
        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeCreate + 1);
        Bmessage testBmessage = bmessageList.get(bmessageList.size() - 1);
        assertThat(testBmessage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBmessage.getBmessageText()).isEqualTo(DEFAULT_BMESSAGE_TEXT);
        assertThat(testBmessage.isIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void createBmessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bmessageRepository.findAll().size();

        // Create the Bmessage with an existing ID
        bmessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBmessageMockMvc.perform(post("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bmessage)))
            .andExpect(status().isBadRequest());

        // Validate the Bmessage in the database
        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bmessageRepository.findAll().size();
        // set the field null
        bmessage.setCreationDate(null);

        // Create the Bmessage, which fails.


        restBmessageMockMvc.perform(post("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bmessage)))
            .andExpect(status().isBadRequest());

        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBmessageTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = bmessageRepository.findAll().size();
        // set the field null
        bmessage.setBmessageText(null);

        // Create the Bmessage, which fails.


        restBmessageMockMvc.perform(post("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bmessage)))
            .andExpect(status().isBadRequest());

        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBmessages() throws Exception {
        // Initialize the database
        bmessageRepository.saveAndFlush(bmessage);

        // Get all the bmessageList
        restBmessageMockMvc.perform(get("/api/bmessages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bmessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].bmessageText").value(hasItem(DEFAULT_BMESSAGE_TEXT)))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBmessage() throws Exception {
        // Initialize the database
        bmessageRepository.saveAndFlush(bmessage);

        // Get the bmessage
        restBmessageMockMvc.perform(get("/api/bmessages/{id}", bmessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bmessage.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.bmessageText").value(DEFAULT_BMESSAGE_TEXT))
            .andExpect(jsonPath("$.isDelivered").value(DEFAULT_IS_DELIVERED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBmessage() throws Exception {
        // Get the bmessage
        restBmessageMockMvc.perform(get("/api/bmessages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBmessage() throws Exception {
        // Initialize the database
        bmessageRepository.saveAndFlush(bmessage);

        int databaseSizeBeforeUpdate = bmessageRepository.findAll().size();

        // Update the bmessage
        Bmessage updatedBmessage = bmessageRepository.findById(bmessage.getId()).get();
        // Disconnect from session so that the updates on updatedBmessage are not directly saved in db
        em.detach(updatedBmessage);
        updatedBmessage
            .creationDate(UPDATED_CREATION_DATE)
            .bmessageText(UPDATED_BMESSAGE_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);

        restBmessageMockMvc.perform(put("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBmessage)))
            .andExpect(status().isOk());

        // Validate the Bmessage in the database
        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeUpdate);
        Bmessage testBmessage = bmessageList.get(bmessageList.size() - 1);
        assertThat(testBmessage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBmessage.getBmessageText()).isEqualTo(UPDATED_BMESSAGE_TEXT);
        assertThat(testBmessage.isIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void updateNonExistingBmessage() throws Exception {
        int databaseSizeBeforeUpdate = bmessageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBmessageMockMvc.perform(put("/api/bmessages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bmessage)))
            .andExpect(status().isBadRequest());

        // Validate the Bmessage in the database
        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBmessage() throws Exception {
        // Initialize the database
        bmessageRepository.saveAndFlush(bmessage);

        int databaseSizeBeforeDelete = bmessageRepository.findAll().size();

        // Delete the bmessage
        restBmessageMockMvc.perform(delete("/api/bmessages/{id}", bmessage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bmessage> bmessageList = bmessageRepository.findAll();
        assertThat(bmessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
