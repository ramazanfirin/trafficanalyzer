package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A VideoRecord.
 */
@Entity
@Table(name = "video_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoRecord implements Serializable {

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

    @NotNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne
    private VideoLine videoLine;

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

    public VideoRecord insertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public VideoRecord vehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getDuration() {
        return duration;
    }

    public VideoRecord duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public VideoLine getVideoLine() {
        return videoLine;
    }

    public VideoRecord videoLine(VideoLine videoLine) {
        this.videoLine = videoLine;
        return this;
    }

    public void setVideoLine(VideoLine videoLine) {
        this.videoLine = videoLine;
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
        VideoRecord videoRecord = (VideoRecord) o;
        if (videoRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), videoRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VideoRecord{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", duration=" + getDuration() +
            "}";
    }
}
