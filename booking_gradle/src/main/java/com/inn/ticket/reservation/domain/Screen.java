package com.inn.ticket.reservation.domain;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * A Screen.
 */
@Entity
@Table(name = "screen")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Screen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "scn_id")
    private Long screenId;

    @Column(name = "lyt_id")
    private Long layoutId;

    @Column(name = "thtr_id", insertable = false, updatable = false)
    private Long theatreId;

    @Column(name = "scn_name")
    private String screenName;

    @ManyToOne
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "thtr_id", referencedColumnName = "thtr_id", nullable = false))})
    private Theatre theatre;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "screen", fetch   = FetchType.LAZY)
    @JoinColumnsOrFormulas({@JoinColumnOrFormula(column = @JoinColumn(name = "scn_id", referencedColumnName = "scn_id", nullable = false))})
    private List<Shows> shows;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getScreenId() {
        return this.screenId;
    }

    public Screen screenId(Long screenId) {
        this.setScreenId(screenId);
        return this;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getLayoutId() {
        return this.layoutId;
    }

    public Screen layoutId(Long layoutId) {
        this.setLayoutId(layoutId);
        return this;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public Screen screenName(String screenName) {
        this.setScreenName(screenName);
        return this;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Screen)) {
            return false;
        }
        return screenId != null && screenId.equals(((Screen) o).screenId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Screen{" +
            "screenId=" + getScreenId() +
            ", layoutId=" + getLayoutId() +
            ", screenName='" + getScreenName() + "'" +
            "}";
    }
}
