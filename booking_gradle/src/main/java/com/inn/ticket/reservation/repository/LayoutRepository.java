package com.inn.ticket.reservation.repository;

import com.inn.ticket.reservation.domain.Layout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Layout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long> {}
