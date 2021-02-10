package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Cactivity;
import com.dipassio.myapp.repository.CactivityRepository;

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
 * Integration tests for the {@link CactivityResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CactivityResourceIT {

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    @Autowired
    private CactivityRepository cactivityRepository;

    @Mock
    private CactivityRepository cactivityRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCactivityMockMvc;

    private Cactivity cactivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cactivity createEntity(EntityManager em) {
        Cactivity cactivity = new Cactivity()
            .activityName(DEFAULT_ACTIVITY_NAME);
        return cactivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cactivity createUpdatedEntity(EntityManager em) {
        Cactivity cactivity = new Cactivity()
            .activityName(UPDATED_ACTIVITY_NAME);
        return cactivity;
    }

    @BeforeEach
    public void initTest() {
        cactivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createCactivity() throws Exception {
        int databaseSizeBeforeCreate = cactivityRepository.findAll().size();
        // Create the Cactivity
        restCactivityMockMvc.perform(post("/api/cactivities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cactivity)))
            .andExpect(status().isCreated());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeCreate + 1);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(DEFAULT_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    public void createCactivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cactivityRepository.findAll().size();

        // Create the Cactivity with an existing ID
        cactivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCactivityMockMvc.perform(post("/api/cactivities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cactivity)))
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkActivityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cactivityRepository.findAll().size();
        // set the field null
        cactivity.setActivityName(null);

        // Create the Cactivity, which fails.


        restCactivityMockMvc.perform(post("/api/cactivities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cactivity)))
            .andExpect(status().isBadRequest());

        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCactivities() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get all the cactivityList
        restCactivityMockMvc.perform(get("/api/cactivities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cactivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCactivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cactivityRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCactivityMockMvc.perform(get("/api/cactivities?eagerload=true"))
            .andExpect(status().isOk());

        verify(cactivityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCactivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cactivityRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCactivityMockMvc.perform(get("/api/cactivities?eagerload=true"))
            .andExpect(status().isOk());

        verify(cactivityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        // Get the cactivity
        restCactivityMockMvc.perform(get("/api/cactivities/{id}", cactivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cactivity.getId().intValue()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCactivity() throws Exception {
        // Get the cactivity
        restCactivityMockMvc.perform(get("/api/cactivities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();

        // Update the cactivity
        Cactivity updatedCactivity = cactivityRepository.findById(cactivity.getId()).get();
        // Disconnect from session so that the updates on updatedCactivity are not directly saved in db
        em.detach(updatedCactivity);
        updatedCactivity
            .activityName(UPDATED_ACTIVITY_NAME);

        restCactivityMockMvc.perform(put("/api/cactivities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCactivity)))
            .andExpect(status().isOk());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
        Cactivity testCactivity = cactivityList.get(cactivityList.size() - 1);
        assertThat(testCactivity.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCactivity() throws Exception {
        int databaseSizeBeforeUpdate = cactivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCactivityMockMvc.perform(put("/api/cactivities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cactivity)))
            .andExpect(status().isBadRequest());

        // Validate the Cactivity in the database
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCactivity() throws Exception {
        // Initialize the database
        cactivityRepository.saveAndFlush(cactivity);

        int databaseSizeBeforeDelete = cactivityRepository.findAll().size();

        // Delete the cactivity
        restCactivityMockMvc.perform(delete("/api/cactivities/{id}", cactivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cactivity> cactivityList = cactivityRepository.findAll();
        assertThat(cactivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
