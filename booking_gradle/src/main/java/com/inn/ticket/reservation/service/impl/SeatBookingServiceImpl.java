package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.SeatBooking;
import com.inn.ticket.reservation.repository.SeatBookingRepository;
import com.inn.ticket.reservation.service.SeatBookingService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.inn.ticket.reservation.service.dto.AvailabilityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SeatBooking}.
 */
@Service
@Transactional
public class SeatBookingServiceImpl implements SeatBookingService {

    private final Logger log = LoggerFactory.getLogger(SeatBookingServiceImpl.class);

    private final SeatBookingRepository seatBookingRepository;

    public SeatBookingServiceImpl(SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }

    @Override
    public SeatBooking save(SeatBooking seatBooking) {
        log.debug("Request to save SeatBooking : {}", seatBooking);
        return seatBookingRepository.save(seatBooking);
    }

    @Override
    public SeatBooking update(SeatBooking seatBooking) {
        log.debug("Request to update SeatBooking : {}", seatBooking);
        return seatBookingRepository.save(seatBooking);
    }

    @Override
    public Optional<SeatBooking> partialUpdate(SeatBooking seatBooking) {
        log.debug("Request to partially update SeatBooking : {}", seatBooking);

        return seatBookingRepository
            .findById(seatBooking.getSeatBookingId())
            .map(existingSeatBooking -> {
                if (seatBooking.getBookingId() != null) {
                    existingSeatBooking.setBookingId(seatBooking.getBookingId());
                }
                if (seatBooking.getSeatId() != null) {
                    existingSeatBooking.setSeatId(seatBooking.getSeatId());
                }

                return existingSeatBooking;
            })
            .map(seatBookingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatBooking> findAll() {
        log.debug("Request to get all SeatBookings");
        return seatBookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeatBooking> findOne(Long id) {
        log.debug("Request to get SeatBooking : {}", id);
        return seatBookingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SeatBooking : {}", id);
        seatBookingRepository.deleteById(id);
    }

    @Override
    public List<AvailabilityDTO> getAllSeatsForMovies(String movieName, Instant fromDate, Instant toDate, String status) {
        return seatBookingRepository.getAllSeatsForMovies(movieName, fromDate, toDate, status);
    }
}
