package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A VideoDirectionRecord.
 */
@Entity
@Table(name = "video_direction_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoDirectionRecord implements Serializable {

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

    @Column(name = "speed")
    private Double speed;

    @ManyToOne
    private VideoDirection videoDirection;

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

    public VideoDirectionRecord insertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public VideoDirectionRecord vehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getDuration() {
        return duration;
    }

    public VideoDirectionRecord duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getSpeed() {
        return speed;
    }

    public VideoDirectionRecord speed(Double speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public VideoDirection getVideoDirection() {
        return videoDirection;
    }

    public VideoDirectionRecord videoDirection(VideoDirection videoDirection) {
        this.videoDirection = videoDirection;
        return this;
    }

    public void setVideoDirection(VideoDirection videoDirection) {
        this.videoDirection = videoDirection;
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
        VideoDirectionRecord videoDirectionRecord = (VideoDirectionRecord) o;
        if (videoDirectionRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), videoDirectionRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VideoDirectionRecord{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", duration=" + getDuration() +
            ", speed=" + getSpeed() +
            "}";
    }
}
