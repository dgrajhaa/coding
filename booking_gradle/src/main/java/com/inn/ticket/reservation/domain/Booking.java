package com.inn.ticket.reservation.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "bkg_id")
    private Long bookingId;

    @Column(name = "bkg_plt_id")
    private Long platformId;

    @Column(name = "usr_id")
    private Long userId;

    @Column(name = "pmt_id")
    private Long paymentId;

    @Column(name = "bkg_date")
    private Instant bookingDate;

    @Column(name = "bkg_sts")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getBookingId() {
        return this.bookingId;
    }

    public Booking bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getPlatformId() {
        return this.platformId;
    }

    public Booking platformId(Long platformId) {
        this.setPlatformId(platformId);
        return this;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Booking userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public Booking paymentId(Long paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Instant getBookingDate() {
        return this.bookingDate;
    }

    public Booking bookingDate(Instant bookingDate) {
        this.setBookingDate(bookingDate);
        return this;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Booking status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return bookingId != null && bookingId.equals(((Booking) o).bookingId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "bookingId=" + getBookingId() +
            ", platformId=" + getPlatformId() +
            ", userId=" + getUserId() +
            ", paymentId=" + getPaymentId() +
            ", bookingDate='" + getBookingDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
