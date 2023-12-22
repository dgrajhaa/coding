package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.Theatre;
import com.inn.ticket.reservation.repository.TheatreRepository;
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
 * Integration tests for the {@link TheatreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TheatreResourceIT {

    private static final Long DEFAULT_CITY_ID = 1L;
    private static final Long UPDATED_CITY_ID = 2L;

    private static final String DEFAULT_THEATRE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_THEATRE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theatres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{theatreId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTheatreMockMvc;

    private Theatre theatre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatre createEntity(EntityManager em) {
        Theatre theatre = new Theatre().cityId(DEFAULT_CITY_ID).theatreName(DEFAULT_THEATRE_NAME);
        return theatre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatre createUpdatedEntity(EntityManager em) {
        Theatre theatre = new Theatre().cityId(UPDATED_CITY_ID).theatreName(UPDATED_THEATRE_NAME);
        return theatre;
    }

    @BeforeEach
    public void initTest() {
        theatre = createEntity(em);
    }

    @Test
    @Transactional
    void createTheatre() throws Exception {
        int databaseSizeBeforeCreate = theatreRepository.findAll().size();
        // Create the Theatre
        restTheatreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatre)))
            .andExpect(status().isCreated());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeCreate + 1);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testTheatre.getTheatreName()).isEqualTo(DEFAULT_THEATRE_NAME);
    }

    @Test
    @Transactional
    void createTheatreWithExistingId() throws Exception {
        // Create the Theatre with an existing ID
        theatre.setTheatreId(1L);

        int databaseSizeBeforeCreate = theatreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTheatreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatre)))
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTheatres() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=theatreId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].theatreId").value(hasItem(theatre.getTheatreId().intValue())))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].theatreName").value(hasItem(DEFAULT_THEATRE_NAME)));
    }

    @Test
    @Transactional
    void getTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get the theatre
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL_ID, theatre.getTheatreId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.theatreId").value(theatre.getTheatreId().intValue()))
            .andExpect(jsonPath("$.cityId").value(DEFAULT_CITY_ID.intValue()))
            .andExpect(jsonPath("$.theatreName").value(DEFAULT_THEATRE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTheatre() throws Exception {
        // Get the theatre
        restTheatreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre
        Theatre updatedTheatre = theatreRepository.findById(theatre.getTheatreId()).get();
        // Disconnect from session so that the updates on updatedTheatre are not directly saved in db
        em.detach(updatedTheatre);
        updatedTheatre.cityId(UPDATED_CITY_ID).theatreName(UPDATED_THEATRE_NAME);

        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTheatre.getTheatreId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTheatre))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testTheatre.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, theatre.getTheatreId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTheatreWithPatch() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre using partial update
        Theatre partialUpdatedTheatre = new Theatre();
        partialUpdatedTheatre.setTheatreId(theatre.getTheatreId());

        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatre.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatre))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testTheatre.getTheatreName()).isEqualTo(DEFAULT_THEATRE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTheatreWithPatch() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre using partial update
        Theatre partialUpdatedTheatre = new Theatre();
        partialUpdatedTheatre.setTheatreId(theatre.getTheatreId());

        partialUpdatedTheatre.cityId(UPDATED_CITY_ID).theatreName(UPDATED_THEATRE_NAME);

        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatre.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatre))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testTheatre.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, theatre.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(theatre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeDelete = theatreRepository.findAll().size();

        // Delete the theatre
        restTheatreMockMvc
            .perform(delete(ENTITY_API_URL_ID, theatre.getTheatreId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
