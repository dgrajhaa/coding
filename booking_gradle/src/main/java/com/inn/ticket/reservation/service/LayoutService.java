package com.inn.ticket.reservation.service;

import com.inn.ticket.reservation.domain.Layout;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Layout}.
 */
public interface LayoutService {
    /**
     * Save a layout.
     *
     * @param layout the entity to save.
     * @return the persisted entity.
     */
    Layout save(Layout layout);

    /**
     * Updates a layout.
     *
     * @param layout the entity to update.
     * @return the persisted entity.
     */
    Layout update(Layout layout);

    /**
     * Partially updates a layout.
     *
     * @param layout the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Layout> partialUpdate(Layout layout);

    /**
     * Get all the layouts.
     *
     * @return the list of entities.
     */
    List<Layout> findAll();

    /**
     * Get the "id" layout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Layout> findOne(Long id);

    /**
     * Delete the "id" layout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
