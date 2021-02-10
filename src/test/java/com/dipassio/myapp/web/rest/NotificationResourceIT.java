package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.SpringgoodApp;
import com.dipassio.myapp.domain.Notification;
import com.dipassio.myapp.domain.Appuser;
import com.dipassio.myapp.repository.NotificationRepository;

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

import com.dipassio.myapp.domain.enumeration.NotificationReason;
/**
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = SpringgoodApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NOTIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NOTIFICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NotificationReason DEFAULT_NOTIFICATION_REASON = NotificationReason.FOLLOWING;
    private static final NotificationReason UPDATED_NOTIFICATION_REASON = NotificationReason.UNFOLLOWING;

    private static final String DEFAULT_NOTIFICATION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELIVERED = false;
    private static final Boolean UPDATED_IS_DELIVERED = true;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .creationDate(DEFAULT_CREATION_DATE)
            .notificationDate(DEFAULT_NOTIFICATION_DATE)
            .notificationReason(DEFAULT_NOTIFICATION_REASON)
            .notificationText(DEFAULT_NOTIFICATION_TEXT)
            .isDelivered(DEFAULT_IS_DELIVERED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        notification.getAppusers().add(appuser);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .creationDate(UPDATED_CREATION_DATE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .notificationReason(UPDATED_NOTIFICATION_REASON)
            .notificationText(UPDATED_NOTIFICATION_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        notification.getAppusers().add(appuser);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(DEFAULT_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(DEFAULT_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(DEFAULT_NOTIFICATION_TEXT);
        assertThat(testNotification.isIsDelivered()).isEqualTo(DEFAULT_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setCreationDate(null);

        // Create the Notification, which fails.


        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setNotificationReason(null);

        // Create the Notification, which fails.


        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationReason").value(hasItem(DEFAULT_NOTIFICATION_REASON.toString())))
            .andExpect(jsonPath("$.[*].notificationText").value(hasItem(DEFAULT_NOTIFICATION_TEXT)))
            .andExpect(jsonPath("$.[*].isDelivered").value(hasItem(DEFAULT_IS_DELIVERED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.notificationDate").value(DEFAULT_NOTIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.notificationReason").value(DEFAULT_NOTIFICATION_REASON.toString()))
            .andExpect(jsonPath("$.notificationText").value(DEFAULT_NOTIFICATION_TEXT))
            .andExpect(jsonPath("$.isDelivered").value(DEFAULT_IS_DELIVERED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .creationDate(UPDATED_CREATION_DATE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .notificationReason(UPDATED_NOTIFICATION_REASON)
            .notificationText(UPDATED_NOTIFICATION_TEXT)
            .isDelivered(UPDATED_IS_DELIVERED);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNotification.getNotificationDate()).isEqualTo(UPDATED_NOTIFICATION_DATE);
        assertThat(testNotification.getNotificationReason()).isEqualTo(UPDATED_NOTIFICATION_REASON);
        assertThat(testNotification.getNotificationText()).isEqualTo(UPDATED_NOTIFICATION_TEXT);
        assertThat(testNotification.isIsDelivered()).isEqualTo(UPDATED_IS_DELIVERED);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
