package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "mov_id")
    private Long movieId;

    @Column(name = "mov_name")
    private String movieName;

    @Column(name = "thtr_id", insertable = false, updatable = false)
    private Long theatreId;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "thtr_id", referencedColumnName = "thtr_id", nullable = false))})
    private Theatre theatre;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getMovieId() {
        return this.movieId;
    }

    public Movie movieId(Long movieId) {
        this.setMovieId(movieId);
        return this;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return this.movieName;
    }

    public Movie movieName(String movieName) {
        this.setMovieName(movieName);
        return this;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Long getTheatreId() {
        return this.theatreId;
    }

    public Movie theatreId(Long theatreId) {
        this.setTheatreId(theatreId);
        return this;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return movieId != null && movieId.equals(((Movie) o).movieId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "movieId=" + getMovieId() +
            ", movieName='" + getMovieName() + "'" +
            ", theatreId=" + getTheatreId() +
            "}";
    }
}
