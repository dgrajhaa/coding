package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A SeatBooking.
 */
@Entity
@Table(name = "seat_book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeatBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "st_bk_id")
    private Long seatBookingId;

    @Column(name = "bkg_id", insertable = false, updatable = false)
    private Long bookingId;

    @Column(name = "seat_id", insertable = false, updatable = false)
    private Long seatId;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "bkg_id", referencedColumnName = "bkg_id", nullable = false))})
    private Booking booking;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "seat_id", referencedColumnName = "seat_id", nullable = false))})
    private Seat seat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getSeatBookingId() {
        return this.seatBookingId;
    }

    public SeatBooking seatBookingId(Long seatBookingId) {
        this.setSeatBookingId(seatBookingId);
        return this;
    }

    public void setSeatBookingId(Long seatBookingId) {
        this.seatBookingId = seatBookingId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public SeatBooking bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getSeatId() {
        return this.seatId;
    }

    public SeatBooking seatId(Long seatId) {
        this.setSeatId(seatId);
        return this;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatBooking)) {
            return false;
        }
        return seatBookingId != null && seatBookingId.equals(((SeatBooking) o).seatBookingId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatBooking{" +
            "seatBookingId=" + getSeatBookingId() +
            ", bookingId=" + getBookingId() +
            ", seatId=" + getSeatId() +
            "}";
    }
}
