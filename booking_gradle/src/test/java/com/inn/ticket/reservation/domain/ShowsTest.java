package com.inn.ticket.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inn.ticket.reservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shows.class);
        Shows shows1 = new Shows();
        shows1.setShowId(1L);
        Shows shows2 = new Shows();
        shows2.setShowId(shows1.getShowId());
        assertThat(shows1).isEqualTo(shows2);
        shows2.setShowId(2L);
        assertThat(shows1).isNotEqualTo(shows2);
        shows1.setShowId(null);
        assertThat(shows1).isNotEqualTo(shows2);
    }
}
