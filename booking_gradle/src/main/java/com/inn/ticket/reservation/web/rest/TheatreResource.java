package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.Theatre;
import com.inn.ticket.reservation.repository.TheatreRepository;
import com.inn.ticket.reservation.service.TheatreService;
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
 * REST controller for managing {@link com.inn.ticket.reservation.domain.Theatre}.
 */
@RestController
@RequestMapping("/api")
public class TheatreResource {

    private final Logger log = LoggerFactory.getLogger(TheatreResource.class);

    private static final String ENTITY_NAME = "bookingTheatre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TheatreService theatreService;

    private final TheatreRepository theatreRepository;

    public TheatreResource(TheatreService theatreService, TheatreRepository theatreRepository) {
        this.theatreService = theatreService;
        this.theatreRepository = theatreRepository;
    }

    /**
     * {@code POST  /theatres} : Create a new theatre.
     *
     * @param theatre the theatre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new theatre, or with status {@code 400 (Bad Request)} if the theatre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theatres")
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) throws URISyntaxException {
        log.debug("REST request to save Theatre : {}", theatre);
        if (theatre.getTheatreId() != null) {
            throw new BadRequestAlertException("A new theatre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Theatre result = theatreService.save(theatre);
        return ResponseEntity
            .created(new URI("/api/theatres/" + result.getTheatreId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getTheatreId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theatres/:theatreId} : Updates an existing theatre.
     *
     * @param theatreId the id of the theatre to save.
     * @param theatre the theatre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatre,
     * or with status {@code 400 (Bad Request)} if the theatre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theatre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theatres/{theatreId}")
    public ResponseEntity<Theatre> updateTheatre(
        @PathVariable(value = "theatreId", required = false) final Long theatreId,
        @RequestBody Theatre theatre
    ) throws URISyntaxException {
        log.debug("REST request to update Theatre : {}, {}", theatreId, theatre);
        if (theatre.getTheatreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(theatreId, theatre.getTheatreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatreRepository.existsById(theatreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Theatre result = theatreService.update(theatre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, theatre.getTheatreId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theatres/:theatreId} : Partial updates given fields of an existing theatre, field will ignore if it is null
     *
     * @param theatreId the id of the theatre to save.
     * @param theatre the theatre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatre,
     * or with status {@code 400 (Bad Request)} if the theatre is not valid,
     * or with status {@code 404 (Not Found)} if the theatre is not found,
     * or with status {@code 500 (Internal Server Error)} if the theatre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/theatres/{theatreId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Theatre> partialUpdateTheatre(
        @PathVariable(value = "theatreId", required = false) final Long theatreId,
        @RequestBody Theatre theatre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Theatre partially : {}, {}", theatreId, theatre);
        if (theatre.getTheatreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(theatreId, theatre.getTheatreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatreRepository.existsById(theatreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Theatre> result = theatreService.partialUpdate(theatre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, theatre.getTheatreId().toString())
        );
    }

    /**
     * {@code GET  /theatres} : get all the theatres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of theatres in body.
     */
    @GetMapping("/theatres")
    public List<Theatre> getAllTheatres() {
        log.debug("REST request to get all Theatres");
        return theatreService.findAll();
    }

    /**
     * {@code GET  /theatres/:id} : get the "id" theatre.
     *
     * @param id the id of the theatre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the theatre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theatres/{id}")
    public ResponseEntity<Theatre> getTheatre(@PathVariable Long id) {
        log.debug("REST request to get Theatre : {}", id);
        Optional<Theatre> theatre = theatreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(theatre);
    }

    /**
     * {@code DELETE  /theatres/:id} : delete the "id" theatre.
     *
     * @param id the id of the theatre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theatres/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        log.debug("REST request to delete Theatre : {}", id);
        theatreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
