package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.VideoDirectionRecord;

import com.masterteknoloji.trafficanalyzer.repository.VideoDirectionRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordQueryVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordSummaryVM;

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

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing VideoDirectionRecord.
 */
@RestController
@RequestMapping("/api")
public class VideoDirectionRecordResource {

    private final Logger log = LoggerFactory.getLogger(VideoDirectionRecordResource.class);

    private static final String ENTITY_NAME = "videoDirectionRecord";

    private final VideoDirectionRecordRepository videoDirectionRecordRepository;

    public VideoDirectionRecordResource(VideoDirectionRecordRepository videoDirectionRecordRepository) {
        this.videoDirectionRecordRepository = videoDirectionRecordRepository;
    }

    /**
     * POST  /video-direction-records : Create a new videoDirectionRecord.
     *
     * @param videoDirectionRecord the videoDirectionRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoDirectionRecord, or with status 400 (Bad Request) if the videoDirectionRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video-direction-records")
    @Timed
    public ResponseEntity<VideoDirectionRecord> createVideoDirectionRecord(@Valid @RequestBody VideoDirectionRecord videoDirectionRecord) throws URISyntaxException {
        log.debug("REST request to save VideoDirectionRecord : {}", videoDirectionRecord);
        if (videoDirectionRecord.getId() != null) {
            throw new BadRequestAlertException("A new videoDirectionRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoDirectionRecord result = videoDirectionRecordRepository.save(videoDirectionRecord);
        return ResponseEntity.created(new URI("/api/video-direction-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-direction-records : Updates an existing videoDirectionRecord.
     *
     * @param videoDirectionRecord the videoDirectionRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoDirectionRecord,
     * or with status 400 (Bad Request) if the videoDirectionRecord is not valid,
     * or with status 500 (Internal Server Error) if the videoDirectionRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/video-direction-records")
    @Timed
    public ResponseEntity<VideoDirectionRecord> updateVideoDirectionRecord(@Valid @RequestBody VideoDirectionRecord videoDirectionRecord) throws URISyntaxException {
        log.debug("REST request to update VideoDirectionRecord : {}", videoDirectionRecord);
        if (videoDirectionRecord.getId() == null) {
            return createVideoDirectionRecord(videoDirectionRecord);
        }
        VideoDirectionRecord result = videoDirectionRecordRepository.save(videoDirectionRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videoDirectionRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-direction-records : get all the videoDirectionRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoDirectionRecords in body
     */
    @GetMapping("/video-direction-records")
    @Timed
    public ResponseEntity<List<VideoDirectionRecord>> getAllVideoDirectionRecords(Pageable pageable) {
        log.debug("REST request to get a page of VideoDirectionRecords");
        Page<VideoDirectionRecord> page = videoDirectionRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-direction-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-direction-records/:id : get the "id" videoDirectionRecord.
     *
     * @param id the id of the videoDirectionRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoDirectionRecord, or with status 404 (Not Found)
     */
    @GetMapping("/video-direction-records/{id}")
    @Timed
    public ResponseEntity<VideoDirectionRecord> getVideoDirectionRecord(@PathVariable Long id) {
        log.debug("REST request to get VideoDirectionRecord : {}", id);
        VideoDirectionRecord videoDirectionRecord = videoDirectionRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videoDirectionRecord));
    }

    /**
     * DELETE  /video-direction-records/:id : delete the "id" videoDirectionRecord.
     *
     * @param id the id of the videoDirectionRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/video-direction-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideoDirectionRecord(@PathVariable Long id) {
        log.debug("REST request to delete VideoDirectionRecord : {}", id);
        videoDirectionRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    @GetMapping("/video-direction-records/getLineData/{videoId}/{directionIndex}")
    @Timed
    public ResponseEntity<List<VideoRecordQueryVM>> getAllDatagetAllData(@PathVariable Long videoId,@PathVariable Long directionIndex) {
        log.debug("REST request to get VideoRecord : {}", videoId);
        List<VideoRecordQueryVM> result = new ArrayList<VideoRecordQueryVM>();
        
        List<VideoDirectionRecord> videoRecords = videoDirectionRecordRepository.findIntersectionData(videoId,directionIndex);
        for (VideoDirectionRecord map : videoRecords) {
        	result.add(new VideoRecordQueryVM(map.getId(), map.getVehicleType(),map.getInsertDate(), map.getVideoDirection().getIndexValue(),
        			map.getDuration(),0d,map.getVideoDirection().getName()));
        }
      
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    
    @GetMapping("/video-direction-records/getAllDataSummary/{id}")
    @Timed
    public ResponseEntity<List<VideoRecordSummaryVM>> getAllDataSummary(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
        
        Iterable<Map<String,Object>> videoRecords = videoDirectionRecordRepository.getSummaryReport(id);
        for (Map<String, Object> map : videoRecords) {
        	result.add(new VideoRecordSummaryVM((BigInteger)map.get("counts"), (String)map.get("videoname"),(String)map.get("directionname")));
		}
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
