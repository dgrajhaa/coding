package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.Screen;
import com.inn.ticket.reservation.repository.ScreenRepository;
import com.inn.ticket.reservation.service.ScreenService;
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
 * REST controller for managing {@link com.inn.ticket.reservation.domain.Screen}.
 */
@RestController
@RequestMapping("/api")
public class ScreenResource {

    private final Logger log = LoggerFactory.getLogger(ScreenResource.class);

    private static final String ENTITY_NAME = "bookingScreen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScreenService screenService;

    private final ScreenRepository screenRepository;

    public ScreenResource(ScreenService screenService, ScreenRepository screenRepository) {
        this.screenService = screenService;
        this.screenRepository = screenRepository;
    }

    /**
     * {@code POST  /screens} : Create a new screen.
     *
     * @param screen the screen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new screen, or with status {@code 400 (Bad Request)} if the screen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/screens")
    public ResponseEntity<Screen> createScreen(@RequestBody Screen screen) throws URISyntaxException {
        log.debug("REST request to save Screen : {}", screen);
        if (screen.getScreenId() != null) {
            throw new BadRequestAlertException("A new screen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Screen result = screenService.save(screen);
        return ResponseEntity
            .created(new URI("/api/screens/" + result.getScreenId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getScreenId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /screens/:screenId} : Updates an existing screen.
     *
     * @param screenId the id of the screen to save.
     * @param screen the screen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screen,
     * or with status {@code 400 (Bad Request)} if the screen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the screen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/screens/{screenId}")
    public ResponseEntity<Screen> updateScreen(
        @PathVariable(value = "screenId", required = false) final Long screenId,
        @RequestBody Screen screen
    ) throws URISyntaxException {
        log.debug("REST request to update Screen : {}, {}", screenId, screen);
        if (screen.getScreenId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(screenId, screen.getScreenId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(screenId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Screen result = screenService.update(screen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, screen.getScreenId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /screens/:screenId} : Partial updates given fields of an existing screen, field will ignore if it is null
     *
     * @param screenId the id of the screen to save.
     * @param screen the screen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screen,
     * or with status {@code 400 (Bad Request)} if the screen is not valid,
     * or with status {@code 404 (Not Found)} if the screen is not found,
     * or with status {@code 500 (Internal Server Error)} if the screen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/screens/{screenId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Screen> partialUpdateScreen(
        @PathVariable(value = "screenId", required = false) final Long screenId,
        @RequestBody Screen screen
    ) throws URISyntaxException {
        log.debug("REST request to partial update Screen partially : {}, {}", screenId, screen);
        if (screen.getScreenId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(screenId, screen.getScreenId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(screenId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Screen> result = screenService.partialUpdate(screen);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, screen.getScreenId().toString())
        );
    }

    /**
     * {@code GET  /screens} : get all the screens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of screens in body.
     */
    @GetMapping("/screens")
    public List<Screen> getAllScreens() {
        log.debug("REST request to get all Screens");
        return screenService.findAll();
    }

    /**
     * {@code GET  /screens/:id} : get the "id" screen.
     *
     * @param id the id of the screen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the screen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/screens/{id}")
    public ResponseEntity<Screen> getScreen(@PathVariable Long id) {
        log.debug("REST request to get Screen : {}", id);
        Optional<Screen> screen = screenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(screen);
    }

    /**
     * {@code DELETE  /screens/:id} : delete the "id" screen.
     *
     * @param id the id of the screen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/screens/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        log.debug("REST request to delete Screen : {}", id);
        screenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
