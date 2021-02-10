package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Cceleb;
import com.dipassio.myapp.repository.CcelebRepository;

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
 * Integration tests for the {@link CcelebResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CcelebResourceIT {

    private static final String DEFAULT_CELEB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CELEB_NAME = "BBBBBBBBBB";

    @Autowired
    private CcelebRepository ccelebRepository;

    @Mock
    private CcelebRepository ccelebRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCcelebMockMvc;

    private Cceleb cceleb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cceleb createEntity(EntityManager em) {
        Cceleb cceleb = new Cceleb()
            .celebName(DEFAULT_CELEB_NAME);
        return cceleb;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cceleb createUpdatedEntity(EntityManager em) {
        Cceleb cceleb = new Cceleb()
            .celebName(UPDATED_CELEB_NAME);
        return cceleb;
    }

    @BeforeEach
    public void initTest() {
        cceleb = createEntity(em);
    }

    @Test
    @Transactional
    public void createCceleb() throws Exception {
        int databaseSizeBeforeCreate = ccelebRepository.findAll().size();
        // Create the Cceleb
        restCcelebMockMvc.perform(post("/api/ccelebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cceleb)))
            .andExpect(status().isCreated());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeCreate + 1);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(DEFAULT_CELEB_NAME);
    }

    @Test
    @Transactional
    public void createCcelebWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ccelebRepository.findAll().size();

        // Create the Cceleb with an existing ID
        cceleb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCcelebMockMvc.perform(post("/api/ccelebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cceleb)))
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCelebNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ccelebRepository.findAll().size();
        // set the field null
        cceleb.setCelebName(null);

        // Create the Cceleb, which fails.


        restCcelebMockMvc.perform(post("/api/ccelebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cceleb)))
            .andExpect(status().isBadRequest());

        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCcelebs() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get all the ccelebList
        restCcelebMockMvc.perform(get("/api/ccelebs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cceleb.getId().intValue())))
            .andExpect(jsonPath("$.[*].celebName").value(hasItem(DEFAULT_CELEB_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCcelebsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ccelebRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCcelebMockMvc.perform(get("/api/ccelebs?eagerload=true"))
            .andExpect(status().isOk());

        verify(ccelebRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCcelebsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ccelebRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCcelebMockMvc.perform(get("/api/ccelebs?eagerload=true"))
            .andExpect(status().isOk());

        verify(ccelebRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        // Get the cceleb
        restCcelebMockMvc.perform(get("/api/ccelebs/{id}", cceleb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cceleb.getId().intValue()))
            .andExpect(jsonPath("$.celebName").value(DEFAULT_CELEB_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCceleb() throws Exception {
        // Get the cceleb
        restCcelebMockMvc.perform(get("/api/ccelebs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();

        // Update the cceleb
        Cceleb updatedCceleb = ccelebRepository.findById(cceleb.getId()).get();
        // Disconnect from session so that the updates on updatedCceleb are not directly saved in db
        em.detach(updatedCceleb);
        updatedCceleb
            .celebName(UPDATED_CELEB_NAME);

        restCcelebMockMvc.perform(put("/api/ccelebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCceleb)))
            .andExpect(status().isOk());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
        Cceleb testCceleb = ccelebList.get(ccelebList.size() - 1);
        assertThat(testCceleb.getCelebName()).isEqualTo(UPDATED_CELEB_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCceleb() throws Exception {
        int databaseSizeBeforeUpdate = ccelebRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCcelebMockMvc.perform(put("/api/ccelebs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cceleb)))
            .andExpect(status().isBadRequest());

        // Validate the Cceleb in the database
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCceleb() throws Exception {
        // Initialize the database
        ccelebRepository.saveAndFlush(cceleb);

        int databaseSizeBeforeDelete = ccelebRepository.findAll().size();

        // Delete the cceleb
        restCcelebMockMvc.perform(delete("/api/ccelebs/{id}", cceleb.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cceleb> ccelebList = ccelebRepository.findAll();
        assertThat(ccelebList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
