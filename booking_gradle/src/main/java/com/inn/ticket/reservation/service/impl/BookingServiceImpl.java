package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Booking;
import com.inn.ticket.reservation.repository.BookingRepository;
import com.inn.ticket.reservation.service.BookingService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        log.debug("Request to save Booking : {}", booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        log.debug("Request to update Booking : {}", booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> partialUpdate(Booking booking) {
        log.debug("Request to partially update Booking : {}", booking);

        return bookingRepository
            .findById(booking.getBookingId())
            .map(existingBooking -> {
                if (booking.getPlatformId() != null) {
                    existingBooking.setPlatformId(booking.getPlatformId());
                }
                if (booking.getUserId() != null) {
                    existingBooking.setUserId(booking.getUserId());
                }
                if (booking.getPaymentId() != null) {
                    existingBooking.setPaymentId(booking.getPaymentId());
                }
                if (booking.getBookingDate() != null) {
                    existingBooking.setBookingDate(booking.getBookingDate());
                }
                if (booking.getStatus() != null) {
                    existingBooking.setStatus(booking.getStatus());
                }

                return existingBooking;
            })
            .map(bookingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findAll() {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
