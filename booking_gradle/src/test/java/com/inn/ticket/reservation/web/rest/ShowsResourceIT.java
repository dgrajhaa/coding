package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.Shows;
import com.inn.ticket.reservation.repository.ShowsRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShowsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShowsResourceIT {

    private static final Long DEFAULT_SCREEN_ID = 1L;
    private static final Long UPDATED_SCREEN_ID = 2L;

    private static final Instant DEFAULT_SHOW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHOW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_STARTING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{showId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShowsRepository showsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShowsMockMvc;

    private Shows shows;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shows createEntity(EntityManager em) {
        Shows shows = new Shows()
            .screenId(DEFAULT_SCREEN_ID)
            .showDate(DEFAULT_SHOW_DATE)
            .startingTime(DEFAULT_STARTING_TIME)
            .endingTime(DEFAULT_ENDING_TIME);
        return shows;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shows createUpdatedEntity(EntityManager em) {
        Shows shows = new Shows()
            .screenId(UPDATED_SCREEN_ID)
            .showDate(UPDATED_SHOW_DATE)
            .startingTime(UPDATED_STARTING_TIME)
            .endingTime(UPDATED_ENDING_TIME);
        return shows;
    }

    @BeforeEach
    public void initTest() {
        shows = createEntity(em);
    }

    @Test
    @Transactional
    void createShows() throws Exception {
        int databaseSizeBeforeCreate = showsRepository.findAll().size();
        // Create the Shows
        restShowsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shows)))
            .andExpect(status().isCreated());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeCreate + 1);
        Shows testShows = showsList.get(showsList.size() - 1);
        assertThat(testShows.getScreenId()).isEqualTo(DEFAULT_SCREEN_ID);
        assertThat(testShows.getShowDate()).isEqualTo(DEFAULT_SHOW_DATE);
        assertThat(testShows.getStartingTime()).isEqualTo(DEFAULT_STARTING_TIME);
        assertThat(testShows.getEndingTime()).isEqualTo(DEFAULT_ENDING_TIME);
    }

    @Test
    @Transactional
    void createShowsWithExistingId() throws Exception {
        // Create the Shows with an existing ID
        shows.setShowId(1L);

        int databaseSizeBeforeCreate = showsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shows)))
            .andExpect(status().isBadRequest());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShows() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        // Get all the showsList
        restShowsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].showId").value(hasItem(shows.getShowId().intValue())))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(DEFAULT_SCREEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].showDate").value(hasItem(DEFAULT_SHOW_DATE.toString())))
            .andExpect(jsonPath("$.[*].startingTime").value(hasItem(DEFAULT_STARTING_TIME.toString())))
            .andExpect(jsonPath("$.[*].endingTime").value(hasItem(DEFAULT_ENDING_TIME.toString())));
    }

    @Test
    @Transactional
    void getShows() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        // Get the shows
        restShowsMockMvc
            .perform(get(ENTITY_API_URL_ID, shows.getShowId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.showId").value(shows.getShowId().intValue()))
            .andExpect(jsonPath("$.screenId").value(DEFAULT_SCREEN_ID.intValue()))
            .andExpect(jsonPath("$.showDate").value(DEFAULT_SHOW_DATE.toString()))
            .andExpect(jsonPath("$.startingTime").value(DEFAULT_STARTING_TIME.toString()))
            .andExpect(jsonPath("$.endingTime").value(DEFAULT_ENDING_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShows() throws Exception {
        // Get the shows
        restShowsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShows() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        int databaseSizeBeforeUpdate = showsRepository.findAll().size();

        // Update the shows
        Shows updatedShows = showsRepository.findById(shows.getShowId()).get();
        // Disconnect from session so that the updates on updatedShows are not directly saved in db
        em.detach(updatedShows);
        updatedShows
            .screenId(UPDATED_SCREEN_ID)
            .showDate(UPDATED_SHOW_DATE)
            .startingTime(UPDATED_STARTING_TIME)
            .endingTime(UPDATED_ENDING_TIME);

        restShowsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShows.getShowId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShows))
            )
            .andExpect(status().isOk());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
        Shows testShows = showsList.get(showsList.size() - 1);
        assertThat(testShows.getScreenId()).isEqualTo(UPDATED_SCREEN_ID);
        assertThat(testShows.getShowDate()).isEqualTo(UPDATED_SHOW_DATE);
        assertThat(testShows.getStartingTime()).isEqualTo(UPDATED_STARTING_TIME);
        assertThat(testShows.getEndingTime()).isEqualTo(UPDATED_ENDING_TIME);
    }

    @Test
    @Transactional
    void putNonExistingShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shows.getShowId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shows))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shows))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shows)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShowsWithPatch() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        int databaseSizeBeforeUpdate = showsRepository.findAll().size();

        // Update the shows using partial update
        Shows partialUpdatedShows = new Shows();
        partialUpdatedShows.setShowId(shows.getShowId());

        partialUpdatedShows.showDate(UPDATED_SHOW_DATE).startingTime(UPDATED_STARTING_TIME);

        restShowsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShows.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShows))
            )
            .andExpect(status().isOk());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
        Shows testShows = showsList.get(showsList.size() - 1);
        assertThat(testShows.getScreenId()).isEqualTo(DEFAULT_SCREEN_ID);
        assertThat(testShows.getShowDate()).isEqualTo(UPDATED_SHOW_DATE);
        assertThat(testShows.getStartingTime()).isEqualTo(UPDATED_STARTING_TIME);
        assertThat(testShows.getEndingTime()).isEqualTo(DEFAULT_ENDING_TIME);
    }

    @Test
    @Transactional
    void fullUpdateShowsWithPatch() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        int databaseSizeBeforeUpdate = showsRepository.findAll().size();

        // Update the shows using partial update
        Shows partialUpdatedShows = new Shows();
        partialUpdatedShows.setShowId(shows.getShowId());

        partialUpdatedShows
            .screenId(UPDATED_SCREEN_ID)
            .showDate(UPDATED_SHOW_DATE)
            .startingTime(UPDATED_STARTING_TIME)
            .endingTime(UPDATED_ENDING_TIME);

        restShowsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShows.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShows))
            )
            .andExpect(status().isOk());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
        Shows testShows = showsList.get(showsList.size() - 1);
        assertThat(testShows.getScreenId()).isEqualTo(UPDATED_SCREEN_ID);
        assertThat(testShows.getShowDate()).isEqualTo(UPDATED_SHOW_DATE);
        assertThat(testShows.getStartingTime()).isEqualTo(UPDATED_STARTING_TIME);
        assertThat(testShows.getEndingTime()).isEqualTo(UPDATED_ENDING_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shows.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shows))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shows))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShows() throws Exception {
        int databaseSizeBeforeUpdate = showsRepository.findAll().size();
        shows.setShowId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shows)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shows in the database
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShows() throws Exception {
        // Initialize the database
        showsRepository.saveAndFlush(shows);

        int databaseSizeBeforeDelete = showsRepository.findAll().size();

        // Delete the shows
        restShowsMockMvc
            .perform(delete(ENTITY_API_URL_ID, shows.getShowId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shows> showsList = showsRepository.findAll();
        assertThat(showsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
