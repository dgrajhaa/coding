package com.inn.ticket.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inn.ticket.reservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingPlatformTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingPlatform.class);
        BookingPlatform bookingPlatform1 = new BookingPlatform();
        bookingPlatform1.setPlatformId(1L);
        BookingPlatform bookingPlatform2 = new BookingPlatform();
        bookingPlatform2.setPlatformId(bookingPlatform1.getPlatformId());
        assertThat(bookingPlatform1).isEqualTo(bookingPlatform2);
        bookingPlatform2.setPlatformId(2L);
        assertThat(bookingPlatform1).isNotEqualTo(bookingPlatform2);
        bookingPlatform1.setPlatformId(null);
        assertThat(bookingPlatform1).isNotEqualTo(bookingPlatform2);
    }
}
