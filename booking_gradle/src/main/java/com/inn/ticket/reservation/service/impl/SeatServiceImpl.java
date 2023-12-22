package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Seat;
import com.inn.ticket.reservation.repository.SeatRepository;
import com.inn.ticket.reservation.service.SeatService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Seat}.
 */
@Service
@Transactional
public class SeatServiceImpl implements SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatServiceImpl.class);

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Seat save(Seat seat) {
        log.debug("Request to save Seat : {}", seat);
        return seatRepository.save(seat);
    }

    @Override
    public Seat update(Seat seat) {
        log.debug("Request to update Seat : {}", seat);
        return seatRepository.save(seat);
    }

    @Override
    public Optional<Seat> partialUpdate(Seat seat) {
        log.debug("Request to partially update Seat : {}", seat);

        return seatRepository
            .findById(seat.getSeatId())
            .map(existingSeat -> {
                if (seat.getShowId() != null) {
                    existingSeat.setShowId(seat.getShowId());
                }
                if (seat.getRowName() != null) {
                    existingSeat.setRowName(seat.getRowName());
                }
                if (seat.getSeatNo() != null) {
                    existingSeat.setSeatNo(seat.getSeatNo());
                }
                if (seat.getLock() != null) {
                    existingSeat.setLock(seat.getLock());
                }
                if (seat.getLockExpiresOn() != null) {
                    existingSeat.setLockExpiresOn(seat.getLockExpiresOn());
                }
                if (seat.getStatus() != null) {
                    existingSeat.setStatus(seat.getStatus());
                }
                if (seat.getVersion() != null) {
                    existingSeat.setVersion(seat.getVersion());
                }

                return existingSeat;
            })
            .map(seatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> findAll() {
        log.debug("Request to get all Seats");
        return seatRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seat> findOne(Long id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Seat : {}", id);
        seatRepository.deleteById(id);
    }
}
