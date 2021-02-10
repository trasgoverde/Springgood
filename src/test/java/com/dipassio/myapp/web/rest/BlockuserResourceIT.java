package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Blockuser;
import com.dipassio.myapp.repository.BlockuserRepository;

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
 * Integration tests for the {@link BlockuserResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BlockuserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BlockuserRepository blockuserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockuserMockMvc;

    private Blockuser blockuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blockuser createEntity(EntityManager em) {
        Blockuser blockuser = new Blockuser()
            .creationDate(DEFAULT_CREATION_DATE);
        return blockuser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blockuser createUpdatedEntity(EntityManager em) {
        Blockuser blockuser = new Blockuser()
            .creationDate(UPDATED_CREATION_DATE);
        return blockuser;
    }

    @BeforeEach
    public void initTest() {
        blockuser = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlockuser() throws Exception {
        int databaseSizeBeforeCreate = blockuserRepository.findAll().size();
        // Create the Blockuser
        restBlockuserMockMvc.perform(post("/api/blockusers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockuser)))
            .andExpect(status().isCreated());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeCreate + 1);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createBlockuserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blockuserRepository.findAll().size();

        // Create the Blockuser with an existing ID
        blockuser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockuserMockMvc.perform(post("/api/blockusers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockuser)))
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBlockusers() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get all the blockuserList
        restBlockuserMockMvc.perform(get("/api/blockusers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        // Get the blockuser
        restBlockuserMockMvc.perform(get("/api/blockusers/{id}", blockuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blockuser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBlockuser() throws Exception {
        // Get the blockuser
        restBlockuserMockMvc.perform(get("/api/blockusers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();

        // Update the blockuser
        Blockuser updatedBlockuser = blockuserRepository.findById(blockuser.getId()).get();
        // Disconnect from session so that the updates on updatedBlockuser are not directly saved in db
        em.detach(updatedBlockuser);
        updatedBlockuser
            .creationDate(UPDATED_CREATION_DATE);

        restBlockuserMockMvc.perform(put("/api/blockusers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlockuser)))
            .andExpect(status().isOk());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
        Blockuser testBlockuser = blockuserList.get(blockuserList.size() - 1);
        assertThat(testBlockuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBlockuser() throws Exception {
        int databaseSizeBeforeUpdate = blockuserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockuserMockMvc.perform(put("/api/blockusers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockuser)))
            .andExpect(status().isBadRequest());

        // Validate the Blockuser in the database
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBlockuser() throws Exception {
        // Initialize the database
        blockuserRepository.saveAndFlush(blockuser);

        int databaseSizeBeforeDelete = blockuserRepository.findAll().size();

        // Delete the blockuser
        restBlockuserMockMvc.perform(delete("/api/blockusers/{id}", blockuser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Blockuser> blockuserList = blockuserRepository.findAll();
        assertThat(blockuserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
