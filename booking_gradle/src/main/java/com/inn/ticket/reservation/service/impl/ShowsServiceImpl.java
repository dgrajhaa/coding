package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Shows;
import com.inn.ticket.reservation.repository.ShowsRepository;
import com.inn.ticket.reservation.service.ShowsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shows}.
 */
@Service
@Transactional
public class ShowsServiceImpl implements ShowsService {

    private final Logger log = LoggerFactory.getLogger(ShowsServiceImpl.class);

    private final ShowsRepository showsRepository;

    public ShowsServiceImpl(ShowsRepository showsRepository) {
        this.showsRepository = showsRepository;
    }

    @Override
    public Shows save(Shows shows) {
        log.debug("Request to save Shows : {}", shows);
        return showsRepository.save(shows);
    }

    @Override
    public Shows update(Shows shows) {
        log.debug("Request to update Shows : {}", shows);
        return showsRepository.save(shows);
    }

    @Override
    public Optional<Shows> partialUpdate(Shows shows) {
        log.debug("Request to partially update Shows : {}", shows);

        return showsRepository
            .findById(shows.getShowId())
            .map(existingShows -> {
                if (shows.getScreenId() != null) {
                    existingShows.setScreenId(shows.getScreenId());
                }
                if (shows.getShowDate() != null) {
                    existingShows.setShowDate(shows.getShowDate());
                }
                if (shows.getStartingTime() != null) {
                    existingShows.setStartingTime(shows.getStartingTime());
                }
                if (shows.getEndingTime() != null) {
                    existingShows.setEndingTime(shows.getEndingTime());
                }

                return existingShows;
            })
            .map(showsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shows> findAll() {
        log.debug("Request to get all Shows");
        return showsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shows> findOne(Long id) {
        log.debug("Request to get Shows : {}", id);
        return showsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Shows : {}", id);
        showsRepository.deleteById(id);
    }
}
