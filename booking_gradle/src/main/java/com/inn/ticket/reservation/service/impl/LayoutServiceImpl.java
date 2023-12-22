package com.inn.ticket.reservation.service.impl;

import com.inn.ticket.reservation.domain.Layout;
import com.inn.ticket.reservation.repository.LayoutRepository;
import com.inn.ticket.reservation.service.LayoutService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Layout}.
 */
@Service
@Transactional
public class LayoutServiceImpl implements LayoutService {

    private final Logger log = LoggerFactory.getLogger(LayoutServiceImpl.class);

    private final LayoutRepository layoutRepository;

    public LayoutServiceImpl(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

    @Override
    public Layout save(Layout layout) {
        log.debug("Request to save Layout : {}", layout);
        return layoutRepository.save(layout);
    }

    @Override
    public Layout update(Layout layout) {
        log.debug("Request to update Layout : {}", layout);
        return layoutRepository.save(layout);
    }

    @Override
    public Optional<Layout> partialUpdate(Layout layout) {
        log.debug("Request to partially update Layout : {}", layout);

        return layoutRepository
            .findById(layout.getLayoutId())
            .map(existingLayout -> {
                if (layout.getTotalRows() != null) {
                    existingLayout.setTotalRows(layout.getTotalRows());
                }
                if (layout.getTotalColumn() != null) {
                    existingLayout.setTotalColumn(layout.getTotalColumn());
                }

                return existingLayout;
            })
            .map(layoutRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Layout> findAll() {
        log.debug("Request to get all Layouts");
        return layoutRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Layout> findOne(Long id) {
        log.debug("Request to get Layout : {}", id);
        return layoutRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Layout : {}", id);
        layoutRepository.deleteById(id);
    }
}
