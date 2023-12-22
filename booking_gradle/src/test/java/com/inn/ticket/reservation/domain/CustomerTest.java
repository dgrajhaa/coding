package com.inn.ticket.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inn.ticket.reservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setUserId(1L);
        Customer customer2 = new Customer();
        customer2.setUserId(customer1.getUserId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setUserId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setUserId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
