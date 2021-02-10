package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Follow;
import com.dipassio.myapp.repository.FollowRepository;

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
 * Integration tests for the {@link FollowResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FollowResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFollowMockMvc;

    private Follow follow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createEntity(EntityManager em) {
        Follow follow = new Follow()
            .creationDate(DEFAULT_CREATION_DATE);
        return follow;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createUpdatedEntity(EntityManager em) {
        Follow follow = new Follow()
            .creationDate(UPDATED_CREATION_DATE);
        return follow;
    }

    @BeforeEach
    public void initTest() {
        follow = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollow() throws Exception {
        int databaseSizeBeforeCreate = followRepository.findAll().size();
        // Create the Follow
        restFollowMockMvc.perform(post("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isCreated());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate + 1);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createFollowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followRepository.findAll().size();

        // Create the Follow with an existing ID
        follow.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowMockMvc.perform(post("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFollows() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList
        restFollowMockMvc.perform(get("/api/follows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(follow.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get the follow
        restFollowMockMvc.perform(get("/api/follows/{id}", follow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(follow.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFollow() throws Exception {
        // Get the follow
        restFollowMockMvc.perform(get("/api/follows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // Update the follow
        Follow updatedFollow = followRepository.findById(follow.getId()).get();
        // Disconnect from session so that the updates on updatedFollow are not directly saved in db
        em.detach(updatedFollow);
        updatedFollow
            .creationDate(UPDATED_CREATION_DATE);

        restFollowMockMvc.perform(put("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFollow)))
            .andExpect(status().isOk());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowMockMvc.perform(put("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeDelete = followRepository.findAll().size();

        // Delete the follow
        restFollowMockMvc.perform(delete("/api/follows/{id}", follow.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
