package com.inn.ticket.reservation.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A BookingPlatform.
 */
@Entity
@Table(name = "bkg_plt")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingPlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "bkg_plt_id")
    private Long platformId;

    @Column(name = "thtr_id")
    private Long theatreId;

    @Column(name = "plt_name")
    private String platformName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getPlatformId() {
        return this.platformId;
    }

    public BookingPlatform platformId(Long platformId) {
        this.setPlatformId(platformId);
        return this;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public Long getTheatreId() {
        return this.theatreId;
    }

    public BookingPlatform theatreId(Long theatreId) {
        this.setTheatreId(theatreId);
        return this;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getPlatformName() {
        return this.platformName;
    }

    public BookingPlatform platformName(String platformName) {
        this.setPlatformName(platformName);
        return this;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingPlatform)) {
            return false;
        }
        return platformId != null && platformId.equals(((BookingPlatform) o).platformId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingPlatform{" +
            "platformId=" + getPlatformId() +
            ", theatreId=" + getTheatreId() +
            ", platformName='" + getPlatformName() + "'" +
            "}";
    }
}
