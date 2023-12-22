package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.SeatBooking;
import com.inn.ticket.reservation.service.dto.AvailabilityDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SeatBooking}.
 */
public interface SeatBookingService {
    /**
     * Save a seatBooking.
     *
     * @param seatBooking the entity to save.
     * @return the persisted entity.
     */
    SeatBooking save(SeatBooking seatBooking);

    /**
     * Updates a seatBooking.
     *
     * @param seatBooking the entity to update.
     * @return the persisted entity.
     */
    SeatBooking update(SeatBooking seatBooking);

    /**
     * Partially updates a seatBooking.
     *
     * @param seatBooking the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SeatBooking> partialUpdate(SeatBooking seatBooking);

    /**
     * Get all the seatBookings.
     *
     * @return the list of entities.
     */
    List<SeatBooking> findAll();

    /**
     * Get the "id" seatBooking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SeatBooking> findOne(Long id);

    /**
     * Delete the "id" seatBooking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    public List<AvailabilityDTO> getAllSeatsForMovies(String movieName, Instant shw_date, Instant toDate, String status);
}
