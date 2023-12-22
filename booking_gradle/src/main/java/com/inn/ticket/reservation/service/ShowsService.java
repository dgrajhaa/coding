package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.Shows;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Shows}.
 */
public interface ShowsService {
    /**
     * Save a shows.
     *
     * @param shows the entity to save.
     * @return the persisted entity.
     */
    Shows save(Shows shows);

    /**
     * Updates a shows.
     *
     * @param shows the entity to update.
     * @return the persisted entity.
     */
    Shows update(Shows shows);

    /**
     * Partially updates a shows.
     *
     * @param shows the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Shows> partialUpdate(Shows shows);

    /**
     * Get all the shows.
     *
     * @return the list of entities.
     */
    List<Shows> findAll();

    /**
     * Get the "id" shows.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Shows> findOne(Long id);

    /**
     * Delete the "id" shows.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
