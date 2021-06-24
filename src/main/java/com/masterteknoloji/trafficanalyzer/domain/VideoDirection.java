package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A VideoDirection.
 */
@Entity
@Table(name = "video_direction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoDirection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "index_value", nullable = false)
    private Long indexValue;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private VideoLine startLine;

    @ManyToOne
    private VideoLine endLine;

    @ManyToOne
    private Video video;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndexValue() {
        return indexValue;
    }

    public VideoDirection indexValue(Long indexValue) {
        this.indexValue = indexValue;
        return this;
    }

    public void setIndexValue(Long indexValue) {
        this.indexValue = indexValue;
    }

    public String getName() {
        return name;
    }

    public VideoDirection name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VideoLine getStartLine() {
        return startLine;
    }

    public VideoDirection startLine(VideoLine videoLine) {
        this.startLine = videoLine;
        return this;
    }

    public void setStartLine(VideoLine videoLine) {
        this.startLine = videoLine;
    }

    public VideoLine getEndLine() {
        return endLine;
    }

    public VideoDirection endLine(VideoLine videoLine) {
        this.endLine = videoLine;
        return this;
    }

    public void setEndLine(VideoLine videoLine) {
        this.endLine = videoLine;
    }

    public Video getVideo() {
        return video;
    }

    public VideoDirection video(Video video) {
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
        VideoDirection videoDirection = (VideoDirection) o;
        if (videoDirection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), videoDirection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VideoDirection{" +
            "id=" + getId() +
            ", indexValue=" + getIndexValue() +
            ", name='" + getName() + "'" +
            "}";
    }
}
