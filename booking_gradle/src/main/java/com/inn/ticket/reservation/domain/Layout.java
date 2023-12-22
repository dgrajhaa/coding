package com.inn.ticket.reservation.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Layout.
 */
@Entity
@Table(name = "layout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Layout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "lyt_id")
    private Long layoutId;

    @Column(name = "ttl_row")
    private Integer totalRows;

    @Column(name = "ttl_clmn")
    private Integer totalColumn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getLayoutId() {
        return this.layoutId;
    }

    public Layout layoutId(Long layoutId) {
        this.setLayoutId(layoutId);
        return this;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getTotalRows() {
        return this.totalRows;
    }

    public Layout totalRows(Integer totalRows) {
        this.setTotalRows(totalRows);
        return this;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalColumn() {
        return this.totalColumn;
    }

    public Layout totalColumn(Integer totalColumn) {
        this.setTotalColumn(totalColumn);
        return this;
    }

    public void setTotalColumn(Integer totalColumn) {
        this.totalColumn = totalColumn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Layout)) {
            return false;
        }
        return layoutId != null && layoutId.equals(((Layout) o).layoutId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Layout{" +
            "layoutId=" + getLayoutId() +
            ", totalRows=" + getTotalRows() +
            ", totalColumn=" + getTotalColumn() +
            "}";
    }
}
