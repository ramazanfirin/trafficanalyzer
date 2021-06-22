package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.VideoLine;

import com.masterteknoloji.trafficanalyzer.repository.VideoLineRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordQueryVM;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing VideoLine.
 */
@RestController
@RequestMapping("/api")
public class VideoLineResource {

    private final Logger log = LoggerFactory.getLogger(VideoLineResource.class);

    private static final String ENTITY_NAME = "videoLine";

    private final VideoLineRepository videoLineRepository;

    public VideoLineResource(VideoLineRepository videoLineRepository) {
        this.videoLineRepository = videoLineRepository;
    }

    /**
     * POST  /video-lines : Create a new videoLine.
     *
     * @param videoLine the videoLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoLine, or with status 400 (Bad Request) if the videoLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video-lines")
    @Timed
    public ResponseEntity<VideoLine> createVideoLine(@RequestBody VideoLine videoLine) throws URISyntaxException {
        log.debug("REST request to save VideoLine : {}", videoLine);
        if (videoLine.getId() != null) {
            throw new BadRequestAlertException("A new videoLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoLine result = videoLineRepository.save(videoLine);
        return ResponseEntity.created(new URI("/api/video-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-lines : Updates an existing videoLine.
     *
     * @param videoLine the videoLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoLine,
     * or with status 400 (Bad Request) if the videoLine is not valid,
     * or with status 500 (Internal Server Error) if the videoLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/video-lines")
    @Timed
    public ResponseEntity<VideoLine> updateVideoLine(@RequestBody VideoLine videoLine) throws URISyntaxException {
        log.debug("REST request to update VideoLine : {}", videoLine);
        if (videoLine.getId() == null) {
            return createVideoLine(videoLine);
        }
        VideoLine result = videoLineRepository.save(videoLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videoLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-lines : get all the videoLines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoLines in body
     */
    @GetMapping("/video-lines")
    @Timed
    public ResponseEntity<List<VideoLine>> getAllVideoLines(Pageable pageable) {
        log.debug("REST request to get a page of VideoLines");
        Page<VideoLine> page = videoLineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-lines/:id : get the "id" videoLine.
     *
     * @param id the id of the videoLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoLine, or with status 404 (Not Found)
     */
    @GetMapping("/video-lines/{id}")
    @Timed
    public ResponseEntity<VideoLine> getVideoLine(@PathVariable Long id) {
        log.debug("REST request to get VideoLine : {}", id);
        VideoLine videoLine = videoLineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videoLine));
    }

    /**
     * DELETE  /video-lines/:id : delete the "id" videoLine.
     *
     * @param id the id of the videoLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/video-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideoLine(@PathVariable Long id) {
        log.debug("REST request to delete VideoLine : {}", id);
        videoLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    @GetMapping("/video-lines/getAllData/{id}")
    @Timed
    public ResponseEntity<List<VideoRecordQueryVM>> getAllDatagetAllData(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        List<VideoRecordQueryVM> result = new ArrayList<VideoRecordQueryVM>();
        
        Iterable<Map<String,Object>> videoRecords = videoLineRepository.findAllByLineId(id);
        for (Map<String, Object> map : videoRecords) {
        	result.add(new VideoRecordQueryVM((Long)map.get("id"), (String)map.get("vehicleType"),(Instant)map.get("insertDate"), (Long)map.get("lineId"),(Long)map.get("duration"),(Double)map.get("speed")));
        }
      
        for (VideoRecordQueryVM recordQueryVM : result) {
			Date myDate = Date.from(recordQueryVM.getInsertDate());
			//System.out.println(sdf.format(myDate));
			
        }
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
