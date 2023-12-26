package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.*;

/**
 * A Shows.
 */
@Entity
@Table(name = "shows")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shows implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "shw_id")
    private Long showId;

    @Column(name = "scn_id", insertable = false, updatable = false)
    private Long screenId;

    @Column(name = "shw_date")
    private Instant showDate;

    @Column(name = "strt_time")
    private Instant startingTime;

    @Column(name = "end_time")
    private Instant endingTime;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "scn_id", referencedColumnName = "scn_id", nullable = false))})
    private Screen screen;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "show", fetch   = FetchType.LAZY)
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "shw_id", referencedColumnName = "shw_id", nullable = false))})
    private List<Seat> seats;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getShowId() {
        return this.showId;
    }

    public Shows showId(Long showId) {
        this.setShowId(showId);
        return this;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getScreenId() {
        return this.screenId;
    }

    public Shows screenId(Long screenId) {
        this.setScreenId(screenId);
        return this;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Instant getShowDate() {
        return this.showDate;
    }

    public Shows showDate(Instant showDate) {
        this.setShowDate(showDate);
        return this;
    }

    public void setShowDate(Instant showDate) {
        this.showDate = showDate;
    }

    public Instant getStartingTime() {
        return this.startingTime;
    }

    public Shows startingTime(Instant startingTime) {
        this.setStartingTime(startingTime);
        return this;
    }

    public void setStartingTime(Instant startingTime) {
        this.startingTime = startingTime;
    }

    public Instant getEndingTime() {
        return this.endingTime;
    }

    public Shows endingTime(Instant endingTime) {
        this.setEndingTime(endingTime);
        return this;
    }

    public void setEndingTime(Instant endingTime) {
        this.endingTime = endingTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shows)) {
            return false;
        }
        return showId != null && showId.equals(((Shows) o).showId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shows{" +
            "showId=" + getShowId() +
            ", screenId=" + getScreenId() +
            ", showDate='" + getShowDate() + "'" +
            ", startingTime='" + getStartingTime() + "'" +
            ", endingTime='" + getEndingTime() + "'" +
            "}";
    }
}
