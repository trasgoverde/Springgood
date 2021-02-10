package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Appprofile;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.repository.AppprofileRepository;

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

import com.dipassio.myapp.domain.enumeration.Gender;
/**
 * Integration tests for the {@link AppprofileResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppprofileResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE_PLUS = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_PLUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_SIBBLINGS = -1;
    private static final Integer UPDATED_SIBBLINGS = 0;

    @Autowired
    private AppprofileRepository appprofileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppprofileMockMvc;

    private Appprofile appprofile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appprofile createEntity(EntityManager em) {
        Appprofile appprofile = new Appprofile()
            .creationDate(DEFAULT_CREATION_DATE)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .bio(DEFAULT_BIO)
            .facebook(DEFAULT_FACEBOOK)
            .twitter(DEFAULT_TWITTER)
            .linkedin(DEFAULT_LINKEDIN)
            .instagram(DEFAULT_INSTAGRAM)
            .googlePlus(DEFAULT_GOOGLE_PLUS)
            .birthdate(DEFAULT_BIRTHDATE)
            .sibblings(DEFAULT_SIBBLINGS);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appprofile.setAppuser(appuser);
        return appprofile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appprofile createUpdatedEntity(EntityManager em) {
        Appprofile appprofile = new Appprofile()
            .creationDate(UPDATED_CREATION_DATE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .googlePlus(UPDATED_GOOGLE_PLUS)
            .birthdate(UPDATED_BIRTHDATE)
            .sibblings(UPDATED_SIBBLINGS);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appprofile.setAppuser(appuser);
        return appprofile;
    }

    @BeforeEach
    public void initTest() {
        appprofile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppprofile() throws Exception {
        int databaseSizeBeforeCreate = appprofileRepository.findAll().size();
        // Create the Appprofile
        restAppprofileMockMvc.perform(post("/api/appprofiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appprofile)))
            .andExpect(status().isCreated());

        // Validate the Appprofile in the database
        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeCreate + 1);
        Appprofile testAppprofile = appprofileList.get(appprofileList.size() - 1);
        assertThat(testAppprofile.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppprofile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAppprofile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAppprofile.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testAppprofile.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testAppprofile.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testAppprofile.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testAppprofile.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testAppprofile.getGooglePlus()).isEqualTo(DEFAULT_GOOGLE_PLUS);
        assertThat(testAppprofile.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testAppprofile.getSibblings()).isEqualTo(DEFAULT_SIBBLINGS);
    }

    @Test
    @Transactional
    public void createAppprofileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appprofileRepository.findAll().size();

        // Create the Appprofile with an existing ID
        appprofile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppprofileMockMvc.perform(post("/api/appprofiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appprofile)))
            .andExpect(status().isBadRequest());

        // Validate the Appprofile in the database
        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appprofileRepository.findAll().size();
        // set the field null
        appprofile.setCreationDate(null);

        // Create the Appprofile, which fails.


        restAppprofileMockMvc.perform(post("/api/appprofiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appprofile)))
            .andExpect(status().isBadRequest());

        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppprofiles() throws Exception {
        // Initialize the database
        appprofileRepository.saveAndFlush(appprofile);

        // Get all the appprofileList
        restAppprofileMockMvc.perform(get("/api/appprofiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appprofile.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].googlePlus").value(hasItem(DEFAULT_GOOGLE_PLUS)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].sibblings").value(hasItem(DEFAULT_SIBBLINGS)));
    }
    
    @Test
    @Transactional
    public void getAppprofile() throws Exception {
        // Initialize the database
        appprofileRepository.saveAndFlush(appprofile);

        // Get the appprofile
        restAppprofileMockMvc.perform(get("/api/appprofiles/{id}", appprofile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appprofile.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM))
            .andExpect(jsonPath("$.googlePlus").value(DEFAULT_GOOGLE_PLUS))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.sibblings").value(DEFAULT_SIBBLINGS));
    }
    @Test
    @Transactional
    public void getNonExistingAppprofile() throws Exception {
        // Get the appprofile
        restAppprofileMockMvc.perform(get("/api/appprofiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppprofile() throws Exception {
        // Initialize the database
        appprofileRepository.saveAndFlush(appprofile);

        int databaseSizeBeforeUpdate = appprofileRepository.findAll().size();

        // Update the appprofile
        Appprofile updatedAppprofile = appprofileRepository.findById(appprofile.getId()).get();
        // Disconnect from session so that the updates on updatedAppprofile are not directly saved in db
        em.detach(updatedAppprofile);
        updatedAppprofile
            .creationDate(UPDATED_CREATION_DATE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .googlePlus(UPDATED_GOOGLE_PLUS)
            .birthdate(UPDATED_BIRTHDATE)
            .sibblings(UPDATED_SIBBLINGS);

        restAppprofileMockMvc.perform(put("/api/appprofiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppprofile)))
            .andExpect(status().isOk());

        // Validate the Appprofile in the database
        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeUpdate);
        Appprofile testAppprofile = appprofileList.get(appprofileList.size() - 1);
        assertThat(testAppprofile.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppprofile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAppprofile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAppprofile.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testAppprofile.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testAppprofile.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testAppprofile.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testAppprofile.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testAppprofile.getGooglePlus()).isEqualTo(UPDATED_GOOGLE_PLUS);
        assertThat(testAppprofile.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testAppprofile.getSibblings()).isEqualTo(UPDATED_SIBBLINGS);
    }

    @Test
    @Transactional
    public void updateNonExistingAppprofile() throws Exception {
        int databaseSizeBeforeUpdate = appprofileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppprofileMockMvc.perform(put("/api/appprofiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appprofile)))
            .andExpect(status().isBadRequest());

        // Validate the Appprofile in the database
        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppprofile() throws Exception {
        // Initialize the database
        appprofileRepository.saveAndFlush(appprofile);

        int databaseSizeBeforeDelete = appprofileRepository.findAll().size();

        // Delete the appprofile
        restAppprofileMockMvc.perform(delete("/api/appprofiles/{id}", appprofile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appprofile> appprofileList = appprofileRepository.findAll();
        assertThat(appprofileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
