package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.BookingPlatform;
import com.inn.ticket.reservation.repository.BookingPlatformRepository;
import com.inn.ticket.reservation.service.BookingPlatformService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookingPlatform}.
 */
@Service
@Transactional
public class BookingPlatformServiceImpl implements BookingPlatformService {

    private final Logger log = LoggerFactory.getLogger(BookingPlatformServiceImpl.class);

    private final BookingPlatformRepository bookingPlatformRepository;

    public BookingPlatformServiceImpl(BookingPlatformRepository bookingPlatformRepository) {
        this.bookingPlatformRepository = bookingPlatformRepository;
    }

    @Override
    public BookingPlatform save(BookingPlatform bookingPlatform) {
        log.debug("Request to save BookingPlatform : {}", bookingPlatform);
        return bookingPlatformRepository.save(bookingPlatform);
    }

    @Override
    public BookingPlatform update(BookingPlatform bookingPlatform) {
        log.debug("Request to update BookingPlatform : {}", bookingPlatform);
        return bookingPlatformRepository.save(bookingPlatform);
    }

    @Override
    public Optional<BookingPlatform> partialUpdate(BookingPlatform bookingPlatform) {
        log.debug("Request to partially update BookingPlatform : {}", bookingPlatform);

        return bookingPlatformRepository
            .findById(bookingPlatform.getPlatformId())
            .map(existingBookingPlatform -> {
                if (bookingPlatform.getTheatreId() != null) {
                    existingBookingPlatform.setTheatreId(bookingPlatform.getTheatreId());
                }
                if (bookingPlatform.getPlatformName() != null) {
                    existingBookingPlatform.setPlatformName(bookingPlatform.getPlatformName());
                }

                return existingBookingPlatform;
            })
            .map(bookingPlatformRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingPlatform> findAll() {
        log.debug("Request to get all BookingPlatforms");
        return bookingPlatformRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingPlatform> findOne(Long id) {
        log.debug("Request to get BookingPlatform : {}", id);
        return bookingPlatformRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookingPlatform : {}", id);
        bookingPlatformRepository.deleteById(id);
    }
}
