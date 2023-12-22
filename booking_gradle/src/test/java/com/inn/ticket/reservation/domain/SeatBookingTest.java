package com.inn.ticket.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inn.ticket.reservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatBookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatBooking.class);
        SeatBooking seatBooking1 = new SeatBooking();
        seatBooking1.setSeatBookingId(1L);
        SeatBooking seatBooking2 = new SeatBooking();
        seatBooking2.setSeatBookingId(seatBooking1.getSeatBookingId());
        assertThat(seatBooking1).isEqualTo(seatBooking2);
        seatBooking2.setSeatBookingId(2L);
        assertThat(seatBooking1).isNotEqualTo(seatBooking2);
        seatBooking1.setSeatBookingId(null);
        assertThat(seatBooking1).isNotEqualTo(seatBooking2);
    }
}
