package com.inn.ticket.reservation.repository;

import com.inn.ticket.reservation.domain.Shows;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Shows entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShowsRepository extends JpaRepository<Shows, Long> {}
