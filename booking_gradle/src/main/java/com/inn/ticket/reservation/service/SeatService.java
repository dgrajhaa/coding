package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.Seat;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Seat}.
 */
public interface SeatService {
    /**
     * Save a seat.
     *
     * @param seat the entity to save.
     * @return the persisted entity.
     */
    Seat save(Seat seat);

    /**
     * Updates a seat.
     *
     * @param seat the entity to update.
     * @return the persisted entity.
     */
    Seat update(Seat seat);

    /**
     * Partially updates a seat.
     *
     * @param seat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Seat> partialUpdate(Seat seat);

    /**
     * Get all the seats.
     *
     * @return the list of entities.
     */
    List<Seat> findAll();

    /**
     * Get the "id" seat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Seat> findOne(Long id);

    /**
     * Delete the "id" seat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
