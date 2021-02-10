package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Celeb;
import com.dipassio.myapp.repository.CelebRepository;

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
 * Integration tests for the {@link CelebResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CelebResourceIT {

    private static final String DEFAULT_CELEB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CELEB_NAME = "BBBBBBBBBB";

    @Autowired
    private CelebRepository celebRepository;

    @Mock
    private CelebRepository celebRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCelebMockMvc;

    private Celeb celeb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Celeb createEntity(EntityManager em) {
        Celeb celeb = new Celeb()
            .celebName(DEFAULT_CELEB_NAME);
        return celeb;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Celeb createUpdatedEntity(EntityManager em) {
        Celeb celeb = new Celeb()
            .celebName(UPDATED_CELEB_NAME);
        return celeb;
    }

    @BeforeEach
    public void initTest() {
        celeb = createEntity(em);
    }

    @Test
    @Transactional
    public void createCeleb() throws Exception {
        int databaseSizeBeforeCreate = celebRepository.findAll().size();
        // Create the Celeb
        restCelebMockMvc.perform(post("/api/celebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(celeb)))
            .andExpect(status().isCreated());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeCreate + 1);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(DEFAULT_CELEB_NAME);
    }

    @Test
    @Transactional
    public void createCelebWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = celebRepository.findAll().size();

        // Create the Celeb with an existing ID
        celeb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCelebMockMvc.perform(post("/api/celebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(celeb)))
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCelebNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = celebRepository.findAll().size();
        // set the field null
        celeb.setCelebName(null);

        // Create the Celeb, which fails.


        restCelebMockMvc.perform(post("/api/celebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(celeb)))
            .andExpect(status().isBadRequest());

        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCelebs() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get all the celebList
        restCelebMockMvc.perform(get("/api/celebs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(celeb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCelebsWithEagerRelationshipsIsEnabled() throws Exception {
        when(celebRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCelebMockMvc.perform(get("/api/celebs?eagerload=true"))
            .andExpect(status().isOk());

        verify(celebRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCelebsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(celebRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCelebMockMvc.perform(get("/api/celebs?eagerload=true"))
            .andExpect(status().isOk());

        verify(celebRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        // Get the celeb
        restCelebMockMvc.perform(get("/api/celebs/{id}", celeb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(celeb.getId().intValue()))
            .andExpect(jsonPath("$.celebName").value(DEFAULT_CELEB_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCeleb() throws Exception {
        // Get the celeb
        restCelebMockMvc.perform(get("/api/celebs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeUpdate = celebRepository.findAll().size();

        // Update the celeb
        Celeb updatedCeleb = celebRepository.findById(celeb.getId()).get();
        // Disconnect from session so that the updates on updatedCeleb are not directly saved in db
        em.detach(updatedCeleb);
        updatedCeleb
            .celebName(UPDATED_CELEB_NAME);

        restCelebMockMvc.perform(put("/api/celebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCeleb)))
            .andExpect(status().isOk());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
        Celeb testCeleb = celebList.get(celebList.size() - 1);
        assertThat(testCeleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCeleb() throws Exception {
        int databaseSizeBeforeUpdate = celebRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCelebMockMvc.perform(put("/api/celebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(celeb)))
            .andExpect(status().isBadRequest());

        // Validate the Celeb in the database
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCeleb() throws Exception {
        // Initialize the database
        celebRepository.saveAndFlush(celeb);

        int databaseSizeBeforeDelete = celebRepository.findAll().size();

        // Delete the celeb
        restCelebMockMvc.perform(delete("/api/celebs/{id}", celeb.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Celeb> celebList = celebRepository.findAll();
        assertThat(celebList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
