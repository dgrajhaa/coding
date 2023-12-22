package com.inn.ticket.reservation.web.rest;

import com.inn.ticket.reservation.config.Constants;
import com.inn.ticket.reservation.domain.City;
import com.inn.ticket.reservation.domain.SeatBooking;
import com.inn.ticket.reservation.repository.SeatBookingRepository;
import com.inn.ticket.reservation.service.CityService;
import com.inn.ticket.reservation.service.SeatBookingService;
import com.inn.ticket.reservation.service.dto.AvailabilityDTO;
import com.inn.ticket.reservation.service.dto.BookingRequestDTO;
import com.inn.ticket.reservation.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.inn.ticket.reservation.domain.SeatBooking}.
 */
@RestController
@RequestMapping("/api")
public class SeatBookingResource {

    private final Logger log = LoggerFactory.getLogger(SeatBookingResource.class);

    private static final String ENTITY_NAME = "bookingSeatBooking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private SeatBookingService seatBookingService;
//    @Autowired
//    private SeatBookingRepository seatBookingRepository;

    @Autowired
    private CityService cityService;


    /**
     * {@code POST  /request-bookings} : locking booking.
     *
     * @param requestDTO the seatBooking to create.
     * @return the {@link BookingRequestDTO} with status {@code 201 (Created)} and with body the new seatBooking, or with status {@code 400 (Bad Request)} if the seatBooking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-bookings")
    public BookingRequestDTO requestSeatBooking(@RequestBody BookingRequestDTO requestDTO) throws URISyntaxException {
        log.debug("REST request to save SeatBooking : {}", requestDTO);
//        BookingRequestDTO requestDTO = new BookingRequestDTO();
        City city = cityService.findByCityName(requestDTO.getCity());
        List<AvailabilityDTO> availabilityDTOS = seatBookingService.getAllSeatsForMovies(requestDTO.getMovieName(), requestDTO.getShowDate(), requestDTO.getShowDate(), Constants.AVAILABLE);
        if(CollectionUtils.isEmpty(availabilityDTOS)) {
            throw new BadRequestAlertException("Ticket Not Available at this moment", ENTITY_NAME, "unavailable");
        }
        else {
            Long count = availabilityDTOS.stream().map(availabilityDTO -> requestDTO.getSeatDetails().stream().allMatch(seatDetails -> {
                String row_name = seatDetails.getSeatRow();
                return availabilityDTO.getRw_nam().equalsIgnoreCase(row_name) &&
                        seatDetails.getSeatNumbers().stream().allMatch(seatNo -> Objects.equals(seatNo, availabilityDTO.getSeat_no()));
            })).count();

            if(count == availabilityDTOS.size()) {
                // create records in tables as follows booking, seatBooking and update the status in seat as locked
                double totalCharge = requestDTO.getSeatDetails().stream().map(seatDetails -> Constants.TICKET_PRICE * seatDetails.getSeatNumbers().size()).mapToDouble(Double::doubleValue).sum();
                requestDTO.setTotalCharge(BigDecimal.valueOf(totalCharge));
                requestDTO.setTotal_tax(BigDecimal.valueOf(totalCharge).multiply(BigDecimal.valueOf(0.18)));
                return requestDTO;
            }
            else {
                throw new BadRequestAlertException("Ticket Not Available at this moment", ENTITY_NAME, "unavailable");
            }

        }

    }

    /**
     * {@code POST  /seat-bookings} : Create a new seatBooking.
     *
     * @param seatBooking the seatBooking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seatBooking, or with status {@code 400 (Bad Request)} if the seatBooking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/confirm-bookings")
    public ResponseEntity<SeatBooking> confirmSeatBooking(@RequestBody SeatBooking seatBooking) throws URISyntaxException {
        return null;
    }

//    /**
//     * {@code PATCH  /seat-bookings/:seatBookingId} : Partial updates given fields of an existing seatBooking, field will ignore if it is null
//     *
//     * @param seatBookingId the id of the seatBooking to save.
//     * @param seatBooking the seatBooking to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatBooking,
//     * or with status {@code 400 (Bad Request)} if the seatBooking is not valid,
//     * or with status {@code 404 (Not Found)} if the seatBooking is not found,
//     * or with status {@code 500 (Internal Server Error)} if the seatBooking couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/seat-bookings/{seatBookingId}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<SeatBooking> partialUpdateSeatBooking(
//        @PathVariable(value = "seatBookingId", required = false) final Long seatBookingId,
//        @RequestBody SeatBooking seatBooking
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update SeatBooking partially : {}, {}", seatBookingId, seatBooking);
//        if (seatBooking.getSeatBookingId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(seatBookingId, seatBooking.getSeatBookingId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!seatBookingRepository.existsById(seatBookingId)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<SeatBooking> result = seatBookingService.partialUpdate(seatBooking);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seatBooking.getSeatBookingId().toString())
//        );
//    }
//
//    /**
//     * {@code GET  /seat-bookings} : get all the seatBookings.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seatBookings in body.
//     */
//    @GetMapping("/seat-bookings")
//    public List<SeatBooking> getAllSeatBookings() {
//        log.debug("REST request to get all SeatBookings");
//        return seatBookingService.findAll();
//    }
//
//    /**
//     * {@code GET  /seat-bookings/:id} : get the "id" seatBooking.
//     *
//     * @param id the id of the seatBooking to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seatBooking, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/seat-bookings/{id}")
//    public ResponseEntity<SeatBooking> getSeatBooking(@PathVariable Long id) {
//        log.debug("REST request to get SeatBooking : {}", id);
//        Optional<SeatBooking> seatBooking = seatBookingService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(seatBooking);
//    }
//
//    /**
//     * {@code DELETE  /seat-bookings/:id} : delete the "id" seatBooking.
//     *
//     * @param id the id of the seatBooking to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/seat-bookings/{id}")
//    public ResponseEntity<Void> deleteSeatBooking(@PathVariable Long id) {
//        log.debug("REST request to delete SeatBooking : {}", id);
//        seatBookingService.delete(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
//            .build();
//    }
}
