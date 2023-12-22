package com.inn.ticket.reservation.repository;

import com.inn.ticket.reservation.domain.BookingPlatform;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingPlatform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingPlatformRepository extends JpaRepository<BookingPlatform, Long> {}
