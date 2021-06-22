package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.VideoDirection;

import com.masterteknoloji.trafficanalyzer.repository.VideoDirectionRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VideoDirection.
 */
@RestController
@RequestMapping("/api")
public class VideoDirectionResource {

    private final Logger log = LoggerFactory.getLogger(VideoDirectionResource.class);

    private static final String ENTITY_NAME = "videoDirection";

    private final VideoDirectionRepository videoDirectionRepository;

    public VideoDirectionResource(VideoDirectionRepository videoDirectionRepository) {
        this.videoDirectionRepository = videoDirectionRepository;
    }

    /**
     * POST  /video-directions : Create a new videoDirection.
     *
     * @param videoDirection the videoDirection to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoDirection, or with status 400 (Bad Request) if the videoDirection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video-directions")
    @Timed
    public ResponseEntity<VideoDirection> createVideoDirection(@Valid @RequestBody VideoDirection videoDirection) throws URISyntaxException {
        log.debug("REST request to save VideoDirection : {}", videoDirection);
        if (videoDirection.getId() != null) {
            throw new BadRequestAlertException("A new videoDirection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoDirection result = videoDirectionRepository.save(videoDirection);
        return ResponseEntity.created(new URI("/api/video-directions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-directions : Updates an existing videoDirection.
     *
     * @param videoDirection the videoDirection to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoDirection,
     * or with status 400 (Bad Request) if the videoDirection is not valid,
     * or with status 500 (Internal Server Error) if the videoDirection couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/video-directions")
    @Timed
    public ResponseEntity<VideoDirection> updateVideoDirection(@Valid @RequestBody VideoDirection videoDirection) throws URISyntaxException {
        log.debug("REST request to update VideoDirection : {}", videoDirection);
        if (videoDirection.getId() == null) {
            return createVideoDirection(videoDirection);
        }
        VideoDirection result = videoDirectionRepository.save(videoDirection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videoDirection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-directions : get all the videoDirections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoDirections in body
     */
    @GetMapping("/video-directions")
    @Timed
    public ResponseEntity<List<VideoDirection>> getAllVideoDirections(Pageable pageable) {
        log.debug("REST request to get a page of VideoDirections");
        Page<VideoDirection> page = videoDirectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-directions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-directions/:id : get the "id" videoDirection.
     *
     * @param id the id of the videoDirection to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoDirection, or with status 404 (Not Found)
     */
    @GetMapping("/video-directions/{id}")
    @Timed
    public ResponseEntity<VideoDirection> getVideoDirection(@PathVariable Long id) {
        log.debug("REST request to get VideoDirection : {}", id);
        VideoDirection videoDirection = videoDirectionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videoDirection));
    }

    /**
     * DELETE  /video-directions/:id : delete the "id" videoDirection.
     *
     * @param id the id of the videoDirection to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/video-directions/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideoDirection(@PathVariable Long id) {
        log.debug("REST request to delete VideoDirection : {}", id);
        videoDirectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
