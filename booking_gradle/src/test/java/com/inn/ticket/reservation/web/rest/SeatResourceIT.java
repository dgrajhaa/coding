package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.Seat;
import com.inn.ticket.reservation.repository.SeatRepository;
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
 * Integration tests for the {@link SeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatResourceIT {

    private static final Long DEFAULT_SHOW_ID = 1L;
    private static final Long UPDATED_SHOW_ID = 2L;

    private static final String DEFAULT_ROW_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROW_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEAT_NO = 1;
    private static final Integer UPDATED_SEAT_NO = 2;

    private static final String DEFAULT_LOCK = "AAAAAAAAAA";
    private static final String UPDATED_LOCK = "BBBBBBBBBB";

    private static final Instant DEFAULT_LOCK_EXPIRES_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOCK_EXPIRES_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String ENTITY_API_URL = "/api/seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{seatId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatMockMvc;

    private Seat seat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createEntity(EntityManager em) {
        Seat seat = new Seat()
            .showId(DEFAULT_SHOW_ID)
            .rowName(DEFAULT_ROW_NAME)
            .seatNo(DEFAULT_SEAT_NO)
            .lock(DEFAULT_LOCK)
            .lockExpiresOn(DEFAULT_LOCK_EXPIRES_ON)
            .status(DEFAULT_STATUS)
            .version(DEFAULT_VERSION);
        return seat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createUpdatedEntity(EntityManager em) {
        Seat seat = new Seat()
            .showId(UPDATED_SHOW_ID)
            .rowName(UPDATED_ROW_NAME)
            .seatNo(UPDATED_SEAT_NO)
            .lock(UPDATED_LOCK)
            .lockExpiresOn(UPDATED_LOCK_EXPIRES_ON)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION);
        return seat;
    }

    @BeforeEach
    public void initTest() {
        seat = createEntity(em);
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        int databaseSizeBeforeCreate = seatRepository.findAll().size();
        // Create the Seat
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isCreated());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate + 1);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getShowId()).isEqualTo(DEFAULT_SHOW_ID);
        assertThat(testSeat.getRowName()).isEqualTo(DEFAULT_ROW_NAME);
        assertThat(testSeat.getSeatNo()).isEqualTo(DEFAULT_SEAT_NO);
        assertThat(testSeat.getLock()).isEqualTo(DEFAULT_LOCK);
        assertThat(testSeat.getLockExpiresOn()).isEqualTo(DEFAULT_LOCK_EXPIRES_ON);
        assertThat(testSeat.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSeat.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void createSeatWithExistingId() throws Exception {
        // Create the Seat with an existing ID
        seat.setSeatId(1L);

        int databaseSizeBeforeCreate = seatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeats() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=seatId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].seatId").value(hasItem(seat.getSeatId().intValue())))
            .andExpect(jsonPath("$.[*].showId").value(hasItem(DEFAULT_SHOW_ID.intValue())))
            .andExpect(jsonPath("$.[*].rowName").value(hasItem(DEFAULT_ROW_NAME)))
            .andExpect(jsonPath("$.[*].seatNo").value(hasItem(DEFAULT_SEAT_NO)))
            .andExpect(jsonPath("$.[*].lock").value(hasItem(DEFAULT_LOCK)))
            .andExpect(jsonPath("$.[*].lockExpiresOn").value(hasItem(DEFAULT_LOCK_EXPIRES_ON.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    void getSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get the seat
        restSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, seat.getSeatId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.seatId").value(seat.getSeatId().intValue()))
            .andExpect(jsonPath("$.showId").value(DEFAULT_SHOW_ID.intValue()))
            .andExpect(jsonPath("$.rowName").value(DEFAULT_ROW_NAME))
            .andExpect(jsonPath("$.seatNo").value(DEFAULT_SEAT_NO))
            .andExpect(jsonPath("$.lock").value(DEFAULT_LOCK))
            .andExpect(jsonPath("$.lockExpiresOn").value(DEFAULT_LOCK_EXPIRES_ON.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    void getNonExistingSeat() throws Exception {
        // Get the seat
        restSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat
        Seat updatedSeat = seatRepository.findById(seat.getSeatId()).get();
        // Disconnect from session so that the updates on updatedSeat are not directly saved in db
        em.detach(updatedSeat);
        updatedSeat
            .showId(UPDATED_SHOW_ID)
            .rowName(UPDATED_ROW_NAME)
            .seatNo(UPDATED_SEAT_NO)
            .lock(UPDATED_LOCK)
            .lockExpiresOn(UPDATED_LOCK_EXPIRES_ON)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION);

        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeat.getSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getShowId()).isEqualTo(UPDATED_SHOW_ID);
        assertThat(testSeat.getRowName()).isEqualTo(UPDATED_ROW_NAME);
        assertThat(testSeat.getSeatNo()).isEqualTo(UPDATED_SEAT_NO);
        assertThat(testSeat.getLock()).isEqualTo(UPDATED_LOCK);
        assertThat(testSeat.getLockExpiresOn()).isEqualTo(UPDATED_LOCK_EXPIRES_ON);
        assertThat(testSeat.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSeat.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void putNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seat.getSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setSeatId(seat.getSeatId());

        partialUpdatedSeat
            .showId(UPDATED_SHOW_ID)
            .rowName(UPDATED_ROW_NAME)
            .seatNo(UPDATED_SEAT_NO)
            .lock(UPDATED_LOCK)
            .lockExpiresOn(UPDATED_LOCK_EXPIRES_ON)
            .status(UPDATED_STATUS);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getShowId()).isEqualTo(UPDATED_SHOW_ID);
        assertThat(testSeat.getRowName()).isEqualTo(UPDATED_ROW_NAME);
        assertThat(testSeat.getSeatNo()).isEqualTo(UPDATED_SEAT_NO);
        assertThat(testSeat.getLock()).isEqualTo(UPDATED_LOCK);
        assertThat(testSeat.getLockExpiresOn()).isEqualTo(UPDATED_LOCK_EXPIRES_ON);
        assertThat(testSeat.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSeat.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void fullUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setSeatId(seat.getSeatId());

        partialUpdatedSeat
            .showId(UPDATED_SHOW_ID)
            .rowName(UPDATED_ROW_NAME)
            .seatNo(UPDATED_SEAT_NO)
            .lock(UPDATED_LOCK)
            .lockExpiresOn(UPDATED_LOCK_EXPIRES_ON)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getShowId()).isEqualTo(UPDATED_SHOW_ID);
        assertThat(testSeat.getRowName()).isEqualTo(UPDATED_ROW_NAME);
        assertThat(testSeat.getSeatNo()).isEqualTo(UPDATED_SEAT_NO);
        assertThat(testSeat.getLock()).isEqualTo(UPDATED_LOCK);
        assertThat(testSeat.getLockExpiresOn()).isEqualTo(UPDATED_LOCK_EXPIRES_ON);
        assertThat(testSeat.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSeat.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void patchNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seat.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeDelete = seatRepository.findAll().size();

        // Delete the seat
        restSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, seat.getSeatId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
