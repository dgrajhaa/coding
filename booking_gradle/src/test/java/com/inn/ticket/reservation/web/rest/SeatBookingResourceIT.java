package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.SeatBooking;
import com.inn.ticket.reservation.repository.SeatBookingRepository;
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
 * Integration tests for the {@link SeatBookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatBookingResourceIT {

    private static final Long DEFAULT_BOOKING_ID = 1L;
    private static final Long UPDATED_BOOKING_ID = 2L;

    private static final Long DEFAULT_SEAT_ID = 1L;
    private static final Long UPDATED_SEAT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/seat-bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{seatBookingId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatBookingRepository seatBookingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatBookingMockMvc;

    private SeatBooking seatBooking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatBooking createEntity(EntityManager em) {
        SeatBooking seatBooking = new SeatBooking().bookingId(DEFAULT_BOOKING_ID).seatId(DEFAULT_SEAT_ID);
        return seatBooking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatBooking createUpdatedEntity(EntityManager em) {
        SeatBooking seatBooking = new SeatBooking().bookingId(UPDATED_BOOKING_ID).seatId(UPDATED_SEAT_ID);
        return seatBooking;
    }

    @BeforeEach
    public void initTest() {
        seatBooking = createEntity(em);
    }

    @Test
    @Transactional
    void createSeatBooking() throws Exception {
        int databaseSizeBeforeCreate = seatBookingRepository.findAll().size();
        // Create the SeatBooking
        restSeatBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatBooking)))
            .andExpect(status().isCreated());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeCreate + 1);
        SeatBooking testSeatBooking = seatBookingList.get(seatBookingList.size() - 1);
        assertThat(testSeatBooking.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testSeatBooking.getSeatId()).isEqualTo(DEFAULT_SEAT_ID);
    }

    @Test
    @Transactional
    void createSeatBookingWithExistingId() throws Exception {
        // Create the SeatBooking with an existing ID
        seatBooking.setSeatBookingId(1L);

        int databaseSizeBeforeCreate = seatBookingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatBooking)))
            .andExpect(status().isBadRequest());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeatBookings() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        // Get all the seatBookingList
        restSeatBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=seatBookingId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].seatBookingId").value(hasItem(seatBooking.getSeatBookingId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID.intValue())))
            .andExpect(jsonPath("$.[*].seatId").value(hasItem(DEFAULT_SEAT_ID.intValue())));
    }

    @Test
    @Transactional
    void getSeatBooking() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        // Get the seatBooking
        restSeatBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, seatBooking.getSeatBookingId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.seatBookingId").value(seatBooking.getSeatBookingId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID.intValue()))
            .andExpect(jsonPath("$.seatId").value(DEFAULT_SEAT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSeatBooking() throws Exception {
        // Get the seatBooking
        restSeatBookingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeatBooking() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();

        // Update the seatBooking
        SeatBooking updatedSeatBooking = seatBookingRepository.findById(seatBooking.getSeatBookingId()).get();
        // Disconnect from session so that the updates on updatedSeatBooking are not directly saved in db
        em.detach(updatedSeatBooking);
        updatedSeatBooking.bookingId(UPDATED_BOOKING_ID).seatId(UPDATED_SEAT_ID);

        restSeatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeatBooking.getSeatBookingId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeatBooking))
            )
            .andExpect(status().isOk());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
        SeatBooking testSeatBooking = seatBookingList.get(seatBookingList.size() - 1);
        assertThat(testSeatBooking.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testSeatBooking.getSeatId()).isEqualTo(UPDATED_SEAT_ID);
    }

    @Test
    @Transactional
    void putNonExistingSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatBooking.getSeatBookingId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatBooking))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatBooking))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatBooking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatBookingWithPatch() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();

        // Update the seatBooking using partial update
        SeatBooking partialUpdatedSeatBooking = new SeatBooking();
        partialUpdatedSeatBooking.setSeatBookingId(seatBooking.getSeatBookingId());

        restSeatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatBooking.getSeatBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatBooking))
            )
            .andExpect(status().isOk());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
        SeatBooking testSeatBooking = seatBookingList.get(seatBookingList.size() - 1);
        assertThat(testSeatBooking.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testSeatBooking.getSeatId()).isEqualTo(DEFAULT_SEAT_ID);
    }

    @Test
    @Transactional
    void fullUpdateSeatBookingWithPatch() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();

        // Update the seatBooking using partial update
        SeatBooking partialUpdatedSeatBooking = new SeatBooking();
        partialUpdatedSeatBooking.setSeatBookingId(seatBooking.getSeatBookingId());

        partialUpdatedSeatBooking.bookingId(UPDATED_BOOKING_ID).seatId(UPDATED_SEAT_ID);

        restSeatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatBooking.getSeatBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatBooking))
            )
            .andExpect(status().isOk());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
        SeatBooking testSeatBooking = seatBookingList.get(seatBookingList.size() - 1);
        assertThat(testSeatBooking.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testSeatBooking.getSeatId()).isEqualTo(UPDATED_SEAT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatBooking.getSeatBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatBooking))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatBooking))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeatBooking() throws Exception {
        int databaseSizeBeforeUpdate = seatBookingRepository.findAll().size();
        seatBooking.setSeatBookingId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seatBooking))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatBooking in the database
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeatBooking() throws Exception {
        // Initialize the database
        seatBookingRepository.saveAndFlush(seatBooking);

        int databaseSizeBeforeDelete = seatBookingRepository.findAll().size();

        // Delete the seatBooking
        restSeatBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, seatBooking.getSeatBookingId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SeatBooking> seatBookingList = seatBookingRepository.findAll();
        assertThat(seatBookingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
