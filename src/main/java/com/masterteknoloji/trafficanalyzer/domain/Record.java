package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Record.
 */
@Entity
@Table(name = "record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "insert_date", nullable = false)
    private Instant insertDate;

    @NotNull
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @ManyToOne
    private Line line;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInsertDate() {
        return insertDate;
    }

    public Record insertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Record vehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Line getLine() {
        return line;
    }

    public Record line(Line line) {
        this.line = line;
        return this;
    }

    public void setLine(Line line) {
        this.line = line;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Record record = (Record) o;
        if (record.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), record.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            "}";
    }
}
