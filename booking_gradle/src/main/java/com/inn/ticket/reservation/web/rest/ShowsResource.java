package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.Shows;
import com.inn.ticket.reservation.repository.ShowsRepository;
import com.inn.ticket.reservation.service.ShowsService;
import com.inn.ticket.reservation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.inn.ticket.reservation.domain.Shows}.
 */
@RestController
@RequestMapping("/api")
public class ShowsResource {

    private final Logger log = LoggerFactory.getLogger(ShowsResource.class);

    private static final String ENTITY_NAME = "bookingShows";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowsService showsService;

    private final ShowsRepository showsRepository;

    public ShowsResource(ShowsService showsService, ShowsRepository showsRepository) {
        this.showsService = showsService;
        this.showsRepository = showsRepository;
    }

    /**
     * {@code POST  /shows} : Create a new shows.
     *
     * @param shows the shows to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shows, or with status {@code 400 (Bad Request)} if the shows has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shows")
    public ResponseEntity<Shows> createShows(@RequestBody Shows shows) throws URISyntaxException {
        log.debug("REST request to save Shows : {}", shows);
        if (shows.getShowId() != null) {
            throw new BadRequestAlertException("A new shows cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shows result = showsService.save(shows);
        return ResponseEntity
            .created(new URI("/api/shows/" + result.getShowId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getShowId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shows/:showId} : Updates an existing shows.
     *
     * @param showId the id of the shows to save.
     * @param shows the shows to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shows,
     * or with status {@code 400 (Bad Request)} if the shows is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shows couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shows/{showId}")
    public ResponseEntity<Shows> updateShows(@PathVariable(value = "showId", required = false) final Long showId, @RequestBody Shows shows)
        throws URISyntaxException {
        log.debug("REST request to update Shows : {}, {}", showId, shows);
        if (shows.getShowId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(showId, shows.getShowId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showsRepository.existsById(showId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Shows result = showsService.update(shows);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shows.getShowId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shows/:showId} : Partial updates given fields of an existing shows, field will ignore if it is null
     *
     * @param showId the id of the shows to save.
     * @param shows the shows to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shows,
     * or with status {@code 400 (Bad Request)} if the shows is not valid,
     * or with status {@code 404 (Not Found)} if the shows is not found,
     * or with status {@code 500 (Internal Server Error)} if the shows couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shows/{showId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Shows> partialUpdateShows(
        @PathVariable(value = "showId", required = false) final Long showId,
        @RequestBody Shows shows
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shows partially : {}, {}", showId, shows);
        if (shows.getShowId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(showId, shows.getShowId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showsRepository.existsById(showId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Shows> result = showsService.partialUpdate(shows);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shows.getShowId().toString())
        );
    }

    /**
     * {@code GET  /shows} : get all the shows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shows in body.
     */
    @GetMapping("/shows")
    public List<Shows> getAllShows() {
        log.debug("REST request to get all Shows");
        return showsService.findAll();
    }

    /**
     * {@code GET  /shows/:id} : get the "id" shows.
     *
     * @param id the id of the shows to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shows, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shows/{id}")
    public ResponseEntity<Shows> getShows(@PathVariable Long id) {
        log.debug("REST request to get Shows : {}", id);
        Optional<Shows> shows = showsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shows);
    }

    /**
     * {@code DELETE  /shows/:id} : delete the "id" shows.
     *
     * @param id the id of the shows to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Void> deleteShows(@PathVariable Long id) {
        log.debug("REST request to delete Shows : {}", id);
        showsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
