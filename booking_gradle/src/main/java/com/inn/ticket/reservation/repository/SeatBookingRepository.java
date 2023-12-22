package com.inn.ticket.reservation.repository;

import com.inn.ticket.reservation.domain.SeatBooking;
import com.inn.ticket.reservation.service.dto.AvailabilityDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the SeatBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    @Query(value = "select a.mov_id, a.mov_name, b.thtr_id, b.thtr_name, c.scn_id, c.scn_name,d.shw_id,d.shw_date, e.rw_nam, e.seat_no, e.locked,e.sts from ticket_ownr.movie a, ticket_ownr.theatre b, ticket_ownr.screen c, ticket_ownr.shows d, ticket_ownr.seat e where a.thtr_id = b.thtr_id and b.thtr_id = c.thtr_id and c.scn_id = d.scn_id and d.shw_id = e.shw_id and a.mov_name=:movieName and d.shw_date between :fromDate and :toDate and e.sts=:status", nativeQuery = true)
    public List<AvailabilityDTO> getAllSeatsForMovies(String movieName, Instant fromDate, Instant toDate, String status);

}
