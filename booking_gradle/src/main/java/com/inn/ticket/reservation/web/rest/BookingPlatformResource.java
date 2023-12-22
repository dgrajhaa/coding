package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.BookingPlatform;
import com.inn.ticket.reservation.repository.BookingPlatformRepository;
import com.inn.ticket.reservation.service.BookingPlatformService;
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
 * REST controller for managing {@link com.inn.ticket.reservation.domain.BookingPlatform}.
 */
@RestController
@RequestMapping("/api")
public class BookingPlatformResource {

    private final Logger log = LoggerFactory.getLogger(BookingPlatformResource.class);

    private static final String ENTITY_NAME = "bookingBookingPlatform";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingPlatformService bookingPlatformService;

    private final BookingPlatformRepository bookingPlatformRepository;

    public BookingPlatformResource(BookingPlatformService bookingPlatformService, BookingPlatformRepository bookingPlatformRepository) {
        this.bookingPlatformService = bookingPlatformService;
        this.bookingPlatformRepository = bookingPlatformRepository;
    }

    /**
     * {@code POST  /booking-platforms} : Create a new bookingPlatform.
     *
     * @param bookingPlatform the bookingPlatform to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingPlatform, or with status {@code 400 (Bad Request)} if the bookingPlatform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/booking-platforms")
    public ResponseEntity<BookingPlatform> createBookingPlatform(@RequestBody BookingPlatform bookingPlatform) throws URISyntaxException {
        log.debug("REST request to save BookingPlatform : {}", bookingPlatform);
        if (bookingPlatform.getPlatformId() != null) {
            throw new BadRequestAlertException("A new bookingPlatform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingPlatform result = bookingPlatformService.save(bookingPlatform);
        return ResponseEntity
            .created(new URI("/api/booking-platforms/" + result.getPlatformId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getPlatformId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /booking-platforms/:platformId} : Updates an existing bookingPlatform.
     *
     * @param platformId the id of the bookingPlatform to save.
     * @param bookingPlatform the bookingPlatform to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingPlatform,
     * or with status {@code 400 (Bad Request)} if the bookingPlatform is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingPlatform couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/booking-platforms/{platformId}")
    public ResponseEntity<BookingPlatform> updateBookingPlatform(
        @PathVariable(value = "platformId", required = false) final Long platformId,
        @RequestBody BookingPlatform bookingPlatform
    ) throws URISyntaxException {
        log.debug("REST request to update BookingPlatform : {}, {}", platformId, bookingPlatform);
        if (bookingPlatform.getPlatformId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(platformId, bookingPlatform.getPlatformId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingPlatformRepository.existsById(platformId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookingPlatform result = bookingPlatformService.update(bookingPlatform);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingPlatform.getPlatformId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /booking-platforms/:platformId} : Partial updates given fields of an existing bookingPlatform, field will ignore if it is null
     *
     * @param platformId the id of the bookingPlatform to save.
     * @param bookingPlatform the bookingPlatform to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingPlatform,
     * or with status {@code 400 (Bad Request)} if the bookingPlatform is not valid,
     * or with status {@code 404 (Not Found)} if the bookingPlatform is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingPlatform couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/booking-platforms/{platformId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingPlatform> partialUpdateBookingPlatform(
        @PathVariable(value = "platformId", required = false) final Long platformId,
        @RequestBody BookingPlatform bookingPlatform
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookingPlatform partially : {}, {}", platformId, bookingPlatform);
        if (bookingPlatform.getPlatformId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(platformId, bookingPlatform.getPlatformId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingPlatformRepository.existsById(platformId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingPlatform> result = bookingPlatformService.partialUpdate(bookingPlatform);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingPlatform.getPlatformId().toString())
        );
    }

    /**
     * {@code GET  /booking-platforms} : get all the bookingPlatforms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingPlatforms in body.
     */
    @GetMapping("/booking-platforms")
    public List<BookingPlatform> getAllBookingPlatforms() {
        log.debug("REST request to get all BookingPlatforms");
        return bookingPlatformService.findAll();
    }

    /**
     * {@code GET  /booking-platforms/:id} : get the "id" bookingPlatform.
     *
     * @param id the id of the bookingPlatform to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingPlatform, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/booking-platforms/{id}")
    public ResponseEntity<BookingPlatform> getBookingPlatform(@PathVariable Long id) {
        log.debug("REST request to get BookingPlatform : {}", id);
        Optional<BookingPlatform> bookingPlatform = bookingPlatformService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingPlatform);
    }

    /**
     * {@code DELETE  /booking-platforms/:id} : delete the "id" bookingPlatform.
     *
     * @param id the id of the bookingPlatform to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/booking-platforms/{id}")
    public ResponseEntity<Void> deleteBookingPlatform(@PathVariable Long id) {
        log.debug("REST request to delete BookingPlatform : {}", id);
        bookingPlatformService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
