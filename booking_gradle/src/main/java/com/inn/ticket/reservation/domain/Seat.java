package com.inn.ticket.reservation.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Seat.
 */
@Entity
@Table(name = "seat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "shw_id")
    private Long showId;

    @Column(name = "rw_nam")
    private String rowName;

    @Column(name = "seat_no")
    private Integer seatNo;

    @Column(name = "lock")
    private String lock;

    @Column(name = "lck_exp")
    private Instant lockExpiresOn;

    @Column(name = "sts")
    private String status;

    @Column(name = "ver")
    private Integer version;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getSeatId() {
        return this.seatId;
    }

    public Seat seatId(Long seatId) {
        this.setSeatId(seatId);
        return this;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getShowId() {
        return this.showId;
    }

    public Seat showId(Long showId) {
        this.setShowId(showId);
        return this;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getRowName() {
        return this.rowName;
    }

    public Seat rowName(String rowName) {
        this.setRowName(rowName);
        return this;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public Integer getSeatNo() {
        return this.seatNo;
    }

    public Seat seatNo(Integer seatNo) {
        this.setSeatNo(seatNo);
        return this;
    }

    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public String getLock() {
        return this.lock;
    }

    public Seat lock(String lock) {
        this.setLock(lock);
        return this;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public Instant getLockExpiresOn() {
        return this.lockExpiresOn;
    }

    public Seat lockExpiresOn(Instant lockExpiresOn) {
        this.setLockExpiresOn(lockExpiresOn);
        return this;
    }

    public void setLockExpiresOn(Instant lockExpiresOn) {
        this.lockExpiresOn = lockExpiresOn;
    }

    public String getStatus() {
        return this.status;
    }

    public Seat status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Seat version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return seatId != null && seatId.equals(((Seat) o).seatId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seat{" +
            "seatId=" + getSeatId() +
            ", showId=" + getShowId() +
            ", rowName='" + getRowName() + "'" +
            ", seatNo=" + getSeatNo() +
            ", lock='" + getLock() + "'" +
            ", lockExpiresOn='" + getLockExpiresOn() + "'" +
            ", status='" + getStatus() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
