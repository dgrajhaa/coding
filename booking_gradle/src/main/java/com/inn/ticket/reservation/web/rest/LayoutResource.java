package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.Layout;
import com.inn.ticket.reservation.repository.LayoutRepository;
import com.inn.ticket.reservation.service.LayoutService;
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
 * REST controller for managing {@link com.inn.ticket.reservation.domain.Layout}.
 */
@RestController
@RequestMapping("/api")
public class LayoutResource {

    private final Logger log = LoggerFactory.getLogger(LayoutResource.class);

    private static final String ENTITY_NAME = "bookingLayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayoutService layoutService;

    private final LayoutRepository layoutRepository;

    public LayoutResource(LayoutService layoutService, LayoutRepository layoutRepository) {
        this.layoutService = layoutService;
        this.layoutRepository = layoutRepository;
    }

    /**
     * {@code POST  /layouts} : Create a new layout.
     *
     * @param layout the layout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layout, or with status {@code 400 (Bad Request)} if the layout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layouts")
    public ResponseEntity<Layout> createLayout(@RequestBody Layout layout) throws URISyntaxException {
        log.debug("REST request to save Layout : {}", layout);
        if (layout.getLayoutId() != null) {
            throw new BadRequestAlertException("A new layout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Layout result = layoutService.save(layout);
        return ResponseEntity
            .created(new URI("/api/layouts/" + result.getLayoutId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getLayoutId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layouts/:layoutId} : Updates an existing layout.
     *
     * @param layoutId the id of the layout to save.
     * @param layout the layout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layout,
     * or with status {@code 400 (Bad Request)} if the layout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layouts/{layoutId}")
    public ResponseEntity<Layout> updateLayout(
        @PathVariable(value = "layoutId", required = false) final Long layoutId,
        @RequestBody Layout layout
    ) throws URISyntaxException {
        log.debug("REST request to update Layout : {}, {}", layoutId, layout);
        if (layout.getLayoutId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(layoutId, layout.getLayoutId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layoutRepository.existsById(layoutId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Layout result = layoutService.update(layout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layout.getLayoutId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /layouts/:layoutId} : Partial updates given fields of an existing layout, field will ignore if it is null
     *
     * @param layoutId the id of the layout to save.
     * @param layout the layout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layout,
     * or with status {@code 400 (Bad Request)} if the layout is not valid,
     * or with status {@code 404 (Not Found)} if the layout is not found,
     * or with status {@code 500 (Internal Server Error)} if the layout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/layouts/{layoutId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Layout> partialUpdateLayout(
        @PathVariable(value = "layoutId", required = false) final Long layoutId,
        @RequestBody Layout layout
    ) throws URISyntaxException {
        log.debug("REST request to partial update Layout partially : {}, {}", layoutId, layout);
        if (layout.getLayoutId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(layoutId, layout.getLayoutId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layoutRepository.existsById(layoutId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Layout> result = layoutService.partialUpdate(layout);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layout.getLayoutId().toString())
        );
    }

    /**
     * {@code GET  /layouts} : get all the layouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layouts in body.
     */
    @GetMapping("/layouts")
    public List<Layout> getAllLayouts() {
        log.debug("REST request to get all Layouts");
        return layoutService.findAll();
    }

    /**
     * {@code GET  /layouts/:id} : get the "id" layout.
     *
     * @param id the id of the layout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layouts/{id}")
    public ResponseEntity<Layout> getLayout(@PathVariable Long id) {
        log.debug("REST request to get Layout : {}", id);
        Optional<Layout> layout = layoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layout);
    }

    /**
     * {@code DELETE  /layouts/:id} : delete the "id" layout.
     *
     * @param id the id of the layout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layouts/{id}")
    public ResponseEntity<Void> deleteLayout(@PathVariable Long id) {
        log.debug("REST request to delete Layout : {}", id);
        layoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
