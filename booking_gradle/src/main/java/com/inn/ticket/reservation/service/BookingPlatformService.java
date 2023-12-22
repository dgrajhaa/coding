package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.BookingPlatform;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BookingPlatform}.
 */
public interface BookingPlatformService {
    /**
     * Save a bookingPlatform.
     *
     * @param bookingPlatform the entity to save.
     * @return the persisted entity.
     */
    BookingPlatform save(BookingPlatform bookingPlatform);

    /**
     * Updates a bookingPlatform.
     *
     * @param bookingPlatform the entity to update.
     * @return the persisted entity.
     */
    BookingPlatform update(BookingPlatform bookingPlatform);

    /**
     * Partially updates a bookingPlatform.
     *
     * @param bookingPlatform the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookingPlatform> partialUpdate(BookingPlatform bookingPlatform);

    /**
     * Get all the bookingPlatforms.
     *
     * @return the list of entities.
     */
    List<BookingPlatform> findAll();

    /**
     * Get the "id" bookingPlatform.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookingPlatform> findOne(Long id);

    /**
     * Delete the "id" bookingPlatform.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
