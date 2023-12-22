package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Theatre;
import com.inn.ticket.reservation.repository.TheatreRepository;
import com.inn.ticket.reservation.service.TheatreService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Theatre}.
 */
@Service
@Transactional
public class TheatreServiceImpl implements TheatreService {

    private final Logger log = LoggerFactory.getLogger(TheatreServiceImpl.class);

    private final TheatreRepository theatreRepository;

    public TheatreServiceImpl(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    public Theatre save(Theatre theatre) {
        log.debug("Request to save Theatre : {}", theatre);
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre update(Theatre theatre) {
        log.debug("Request to update Theatre : {}", theatre);
        return theatreRepository.save(theatre);
    }

    @Override
    public Optional<Theatre> partialUpdate(Theatre theatre) {
        log.debug("Request to partially update Theatre : {}", theatre);

        return theatreRepository
            .findById(theatre.getTheatreId())
            .map(existingTheatre -> {
                if (theatre.getCityId() != null) {
                    existingTheatre.setCityId(theatre.getCityId());
                }
                if (theatre.getTheatreName() != null) {
                    existingTheatre.setTheatreName(theatre.getTheatreName());
                }

                return existingTheatre;
            })
            .map(theatreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Theatre> findAll() {
        log.debug("Request to get all Theatres");
        return theatreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Theatre> findOne(Long id) {
        log.debug("Request to get Theatre : {}", id);
        return theatreRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Theatre : {}", id);
        theatreRepository.deleteById(id);
    }
}
