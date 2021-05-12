package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A VideoLine.
 */
@Entity
@Table(name = "video_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_point_x")
    private Long startPointX;

    @Column(name = "start_point_y")
    private Long startPointY;

    @Column(name = "end_point_x")
    private Long endPointX;

    @Column(name = "end_point_y")
    private Long endPointY;

    @ManyToOne
    private Video video;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public VideoLine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartPointX() {
        return startPointX;
    }

    public VideoLine startPointX(Long startPointX) {
        this.startPointX = startPointX;
        return this;
    }

    public void setStartPointX(Long startPointX) {
        this.startPointX = startPointX;
    }

    public Long getStartPointY() {
        return startPointY;
    }

    public VideoLine startPointY(Long startPointY) {
        this.startPointY = startPointY;
        return this;
    }

    public void setStartPointY(Long startPointY) {
        this.startPointY = startPointY;
    }

    public Long getEndPointX() {
        return endPointX;
    }

    public VideoLine endPointX(Long endPointX) {
        this.endPointX = endPointX;
        return this;
    }

    public void setEndPointX(Long endPointX) {
        this.endPointX = endPointX;
    }

    public Long getEndPointY() {
        return endPointY;
    }

    public VideoLine endPointY(Long endPointY) {
        this.endPointY = endPointY;
        return this;
    }

    public void setEndPointY(Long endPointY) {
        this.endPointY = endPointY;
    }

    public Video getVideo() {
        return video;
    }

    public VideoLine video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
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
        VideoLine videoLine = (VideoLine) o;
        if (videoLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), videoLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VideoLine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startPointX=" + getStartPointX() +
            ", startPointY=" + getStartPointY() +
            ", endPointX=" + getEndPointX() +
            ", endPointY=" + getEndPointY() +
            "}";
    }
}
