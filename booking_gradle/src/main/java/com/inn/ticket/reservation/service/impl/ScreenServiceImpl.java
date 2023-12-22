package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Screen;
import com.inn.ticket.reservation.repository.ScreenRepository;
import com.inn.ticket.reservation.service.ScreenService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Screen}.
 */
@Service
@Transactional
public class ScreenServiceImpl implements ScreenService {

    private final Logger log = LoggerFactory.getLogger(ScreenServiceImpl.class);

    private final ScreenRepository screenRepository;

    public ScreenServiceImpl(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    @Override
    public Screen save(Screen screen) {
        log.debug("Request to save Screen : {}", screen);
        return screenRepository.save(screen);
    }

    @Override
    public Screen update(Screen screen) {
        log.debug("Request to update Screen : {}", screen);
        return screenRepository.save(screen);
    }

    @Override
    public Optional<Screen> partialUpdate(Screen screen) {
        log.debug("Request to partially update Screen : {}", screen);

        return screenRepository
            .findById(screen.getScreenId())
            .map(existingScreen -> {
                if (screen.getLayoutId() != null) {
                    existingScreen.setLayoutId(screen.getLayoutId());
                }
                if (screen.getScreenName() != null) {
                    existingScreen.setScreenName(screen.getScreenName());
                }

                return existingScreen;
            })
            .map(screenRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Screen> findAll() {
        log.debug("Request to get all Screens");
        return screenRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Screen> findOne(Long id) {
        log.debug("Request to get Screen : {}", id);
        return screenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Screen : {}", id);
        screenRepository.deleteById(id);
    }
}
