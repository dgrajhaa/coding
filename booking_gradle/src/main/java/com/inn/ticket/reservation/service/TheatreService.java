package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.Theatre;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Theatre}.
 */
public interface TheatreService {
    /**
     * Save a theatre.
     *
     * @param theatre the entity to save.
     * @return the persisted entity.
     */
    Theatre save(Theatre theatre);

    /**
     * Updates a theatre.
     *
     * @param theatre the entity to update.
     * @return the persisted entity.
     */
    Theatre update(Theatre theatre);

    /**
     * Partially updates a theatre.
     *
     * @param theatre the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Theatre> partialUpdate(Theatre theatre);

    /**
     * Get all the theatres.
     *
     * @return the list of entities.
     */
    List<Theatre> findAll();

    /**
     * Get the "id" theatre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Theatre> findOne(Long id);

    /**
     * Delete the "id" theatre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
