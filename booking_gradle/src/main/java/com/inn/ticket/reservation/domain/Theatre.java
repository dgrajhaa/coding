package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * A Theatre.
 */
@Entity
@Table(name = "theatre")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Theatre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "thtr_id")
    private Long theatreId;

    @Column(name = "cty_id", insertable = false, updatable = false)
    private Long cityId;

    @Column(name = "thtr_name")
    private String theatreName;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "cty_id", referencedColumnName = "cty_id", nullable = false))})
    private City city;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "theatre", fetch   = FetchType.LAZY)
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "thtr_id", referencedColumnName = "thtr_id", nullable = false))})
    private List<Screen> screens;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "theatre", fetch   = FetchType.LAZY)
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "thtr_id", referencedColumnName = "thtr_id", nullable = false))})
    private List<Movie> movies;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getTheatreId() {
        return this.theatreId;
    }

    public Theatre theatreId(Long theatreId) {
        this.setTheatreId(theatreId);
        return this;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public Theatre cityId(Long cityId) {
        this.setCityId(cityId);
        return this;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getTheatreName() {
        return this.theatreName;
    }

    public Theatre theatreName(String theatreName) {
        this.setTheatreName(theatreName);
        return this;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theatre)) {
            return false;
        }
        return theatreId != null && theatreId.equals(((Theatre) o).theatreId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Theatre{" +
            "theatreId=" + getTheatreId() +
            ", cityId=" + getCityId() +
            ", theatreName='" + getTheatreName() + "'" +
            "}";
    }
}
