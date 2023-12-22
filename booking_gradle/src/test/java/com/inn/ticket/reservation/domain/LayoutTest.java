package com.inn.ticket.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inn.ticket.reservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Layout.class);
        Layout layout1 = new Layout();
        layout1.setLayoutId(1L);
        Layout layout2 = new Layout();
        layout2.setLayoutId(layout1.getLayoutId());
        assertThat(layout1).isEqualTo(layout2);
        layout2.setLayoutId(2L);
        assertThat(layout1).isNotEqualTo(layout2);
        layout1.setLayoutId(null);
        assertThat(layout1).isNotEqualTo(layout2);
    }
}
