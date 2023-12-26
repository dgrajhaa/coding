package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "cty_id")
    private Long cityId;

    @Column(name = "cty_name")
    private String cityName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "city", fetch   = FetchType.LAZY)
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "cty_id", referencedColumnName = "cty_id", nullable = false))})
    private List<Theatre> theatres;

    public Long getCityId() {
        return this.cityId;
    }

    public City cityId(Long cityId) {
        this.setCityId(cityId);
        return this;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return this.cityName;
    }

    public City cityName(String cityName) {
        this.setCityName(cityName);
        return this;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return cityId != null && cityId.equals(((City) o).cityId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "cityId=" + getCityId() +
            ", cityName='" + getCityName() + "'" +
            "}";
    }
}
