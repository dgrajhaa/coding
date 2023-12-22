package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.BookingPlatform;
import com.inn.ticket.reservation.repository.BookingPlatformRepository;
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
 * Integration tests for the {@link BookingPlatformResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookingPlatformResourceIT {

    private static final Long DEFAULT_THEATRE_ID = 1L;
    private static final Long UPDATED_THEATRE_ID = 2L;

    private static final String DEFAULT_PLATFORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLATFORM_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/booking-platforms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{platformId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookingPlatformRepository bookingPlatformRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingPlatformMockMvc;

    private BookingPlatform bookingPlatform;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingPlatform createEntity(EntityManager em) {
        BookingPlatform bookingPlatform = new BookingPlatform().theatreId(DEFAULT_THEATRE_ID).platformName(DEFAULT_PLATFORM_NAME);
        return bookingPlatform;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingPlatform createUpdatedEntity(EntityManager em) {
        BookingPlatform bookingPlatform = new BookingPlatform().theatreId(UPDATED_THEATRE_ID).platformName(UPDATED_PLATFORM_NAME);
        return bookingPlatform;
    }

    @BeforeEach
    public void initTest() {
        bookingPlatform = createEntity(em);
    }

    @Test
    @Transactional
    void createBookingPlatform() throws Exception {
        int databaseSizeBeforeCreate = bookingPlatformRepository.findAll().size();
        // Create the BookingPlatform
        restBookingPlatformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isCreated());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeCreate + 1);
        BookingPlatform testBookingPlatform = bookingPlatformList.get(bookingPlatformList.size() - 1);
        assertThat(testBookingPlatform.getTheatreId()).isEqualTo(DEFAULT_THEATRE_ID);
        assertThat(testBookingPlatform.getPlatformName()).isEqualTo(DEFAULT_PLATFORM_NAME);
    }

    @Test
    @Transactional
    void createBookingPlatformWithExistingId() throws Exception {
        // Create the BookingPlatform with an existing ID
        bookingPlatform.setPlatformId(1L);

        int databaseSizeBeforeCreate = bookingPlatformRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingPlatformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookingPlatforms() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        // Get all the bookingPlatformList
        restBookingPlatformMockMvc
            .perform(get(ENTITY_API_URL + "?sort=platformId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].platformId").value(hasItem(bookingPlatform.getPlatformId().intValue())))
            .andExpect(jsonPath("$.[*].theatreId").value(hasItem(DEFAULT_THEATRE_ID.intValue())))
            .andExpect(jsonPath("$.[*].platformName").value(hasItem(DEFAULT_PLATFORM_NAME)));
    }

    @Test
    @Transactional
    void getBookingPlatform() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        // Get the bookingPlatform
        restBookingPlatformMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingPlatform.getPlatformId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.platformId").value(bookingPlatform.getPlatformId().intValue()))
            .andExpect(jsonPath("$.theatreId").value(DEFAULT_THEATRE_ID.intValue()))
            .andExpect(jsonPath("$.platformName").value(DEFAULT_PLATFORM_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBookingPlatform() throws Exception {
        // Get the bookingPlatform
        restBookingPlatformMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingPlatform() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();

        // Update the bookingPlatform
        BookingPlatform updatedBookingPlatform = bookingPlatformRepository.findById(bookingPlatform.getPlatformId()).get();
        // Disconnect from session so that the updates on updatedBookingPlatform are not directly saved in db
        em.detach(updatedBookingPlatform);
        updatedBookingPlatform.theatreId(UPDATED_THEATRE_ID).platformName(UPDATED_PLATFORM_NAME);

        restBookingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookingPlatform.getPlatformId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
        BookingPlatform testBookingPlatform = bookingPlatformList.get(bookingPlatformList.size() - 1);
        assertThat(testBookingPlatform.getTheatreId()).isEqualTo(UPDATED_THEATRE_ID);
        assertThat(testBookingPlatform.getPlatformName()).isEqualTo(UPDATED_PLATFORM_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingPlatform.getPlatformId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingPlatformWithPatch() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();

        // Update the bookingPlatform using partial update
        BookingPlatform partialUpdatedBookingPlatform = new BookingPlatform();
        partialUpdatedBookingPlatform.setPlatformId(bookingPlatform.getPlatformId());

        partialUpdatedBookingPlatform.theatreId(UPDATED_THEATRE_ID);

        restBookingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingPlatform.getPlatformId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
        BookingPlatform testBookingPlatform = bookingPlatformList.get(bookingPlatformList.size() - 1);
        assertThat(testBookingPlatform.getTheatreId()).isEqualTo(UPDATED_THEATRE_ID);
        assertThat(testBookingPlatform.getPlatformName()).isEqualTo(DEFAULT_PLATFORM_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBookingPlatformWithPatch() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();

        // Update the bookingPlatform using partial update
        BookingPlatform partialUpdatedBookingPlatform = new BookingPlatform();
        partialUpdatedBookingPlatform.setPlatformId(bookingPlatform.getPlatformId());

        partialUpdatedBookingPlatform.theatreId(UPDATED_THEATRE_ID).platformName(UPDATED_PLATFORM_NAME);

        restBookingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingPlatform.getPlatformId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
        BookingPlatform testBookingPlatform = bookingPlatformList.get(bookingPlatformList.size() - 1);
        assertThat(testBookingPlatform.getTheatreId()).isEqualTo(UPDATED_THEATRE_ID);
        assertThat(testBookingPlatform.getPlatformName()).isEqualTo(UPDATED_PLATFORM_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingPlatform.getPlatformId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = bookingPlatformRepository.findAll().size();
        bookingPlatform.setPlatformId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingPlatform))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingPlatform in the database
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingPlatform() throws Exception {
        // Initialize the database
        bookingPlatformRepository.saveAndFlush(bookingPlatform);

        int databaseSizeBeforeDelete = bookingPlatformRepository.findAll().size();

        // Delete the bookingPlatform
        restBookingPlatformMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingPlatform.getPlatformId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookingPlatform> bookingPlatformList = bookingPlatformRepository.findAll();
        assertThat(bookingPlatformList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
