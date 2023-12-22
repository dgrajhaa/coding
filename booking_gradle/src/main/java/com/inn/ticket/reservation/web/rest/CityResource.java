package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.domain.City;
import com.inn.ticket.reservation.repository.CityRepository;
import com.inn.ticket.reservation.service.CityService;
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
 * REST controller for managing {@link com.inn.ticket.reservation.domain.City}.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "bookingCity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityService cityService;

    private final CityRepository cityRepository;

    public CityResource(CityService cityService, CityRepository cityRepository) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
    }

    /**
     * {@code POST  /cities} : Create a new city.
     *
     * @param city the city to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new city, or with status {@code 400 (Bad Request)} if the city has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@RequestBody City city) throws URISyntaxException {
        log.debug("REST request to save City : {}", city);
        if (city.getCityId() != null) {
            throw new BadRequestAlertException("A new city cannot already have an ID", ENTITY_NAME, "idexists");
        }
        City result = cityService.save(city);
        return ResponseEntity
            .created(new URI("/api/cities/" + result.getCityId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getCityId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cities/:cityId} : Updates an existing city.
     *
     * @param cityId the id of the city to save.
     * @param city the city to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated city,
     * or with status {@code 400 (Bad Request)} if the city is not valid,
     * or with status {@code 500 (Internal Server Error)} if the city couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cities/{cityId}")
    public ResponseEntity<City> updateCity(@PathVariable(value = "cityId", required = false) final Long cityId, @RequestBody City city)
        throws URISyntaxException {
        log.debug("REST request to update City : {}, {}", cityId, city);
        if (city.getCityId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(cityId, city.getCityId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(cityId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        City result = cityService.update(city);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, city.getCityId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cities/:cityId} : Partial updates given fields of an existing city, field will ignore if it is null
     *
     * @param cityId the id of the city to save.
     * @param city the city to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated city,
     * or with status {@code 400 (Bad Request)} if the city is not valid,
     * or with status {@code 404 (Not Found)} if the city is not found,
     * or with status {@code 500 (Internal Server Error)} if the city couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cities/{cityId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<City> partialUpdateCity(
        @PathVariable(value = "cityId", required = false) final Long cityId,
        @RequestBody City city
    ) throws URISyntaxException {
        log.debug("REST request to partial update City partially : {}, {}", cityId, city);
        if (city.getCityId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(cityId, city.getCityId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(cityId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<City> result = cityService.partialUpdate(city);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, city.getCityId().toString())
        );
    }

    /**
     * {@code GET  /cities} : get all the cities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cities in body.
     */
    @GetMapping("/cities")
    public List<City> getAllCities() {
        log.debug("REST request to get all Cities");
        return cityService.findAll();
    }

    /**
     * {@code GET  /cities/:id} : get the "id" city.
     *
     * @param id the id of the city to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the city, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCity(@PathVariable Long id) {
        log.debug("REST request to get City : {}", id);
        Optional<City> city = cityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(city);
    }

    /**
     * {@code DELETE  /cities/:id} : delete the "id" city.
     *
     * @param id the id of the city to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.debug("REST request to delete City : {}", id);
        cityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
