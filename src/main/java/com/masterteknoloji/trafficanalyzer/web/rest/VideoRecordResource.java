package com.masterteknoloji.trafficanalyzer.web.rest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.domain.VideoLine;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.repository.VideoLineRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordQueryVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordSummaryVM;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing VideoRecord.
 */
@RestController
@RequestMapping("/api")
public class VideoRecordResource {

    private final Logger log = LoggerFactory.getLogger(VideoRecordResource.class);

    private static final String ENTITY_NAME = "videoRecord";

    private final VideoRecordRepository videoRecordRepository;

    private final VideoLineRepository videoLineRepository;

    private final VideoRepository videoRepository;
    
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
    
    public VideoRecordResource(VideoRecordRepository videoRecordRepository, VideoLineRepository videoLineRepository , VideoRepository videoRepository) {
        this.videoRecordRepository = videoRecordRepository;
        this.videoLineRepository = videoLineRepository;
        this.videoRepository = videoRepository;
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * POST  /video-records : Create a new videoRecord.
     *
     * @param videoRecord the videoRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoRecord, or with status 400 (Bad Request) if the videoRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video-records")
    @Timed
    public ResponseEntity<VideoRecord> createVideoRecord(@Valid @RequestBody VideoRecord videoRecord) throws URISyntaxException {
        log.debug("REST request to save VideoRecord : {}", videoRecord);
        if (videoRecord.getId() != null) {
            throw new BadRequestAlertException("A new videoRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoRecord result = videoRecordRepository.save(videoRecord);
        return ResponseEntity.created(new URI("/api/video-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-records : Updates an existing videoRecord.
     *
     * @param videoRecord the videoRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoRecord,
     * or with status 400 (Bad Request) if the videoRecord is not valid,
     * or with status 500 (Internal Server Error) if the videoRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/video-records")
    @Timed
    public ResponseEntity<VideoRecord> updateVideoRecord(@Valid @RequestBody VideoRecord videoRecord) throws URISyntaxException {
        log.debug("REST request to update VideoRecord : {}", videoRecord);
        if (videoRecord.getId() == null) {
            return createVideoRecord(videoRecord);
        }
        VideoRecord result = videoRecordRepository.save(videoRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videoRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-records : get all the videoRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoRecords in body
     */
    @GetMapping("/video-records")
    @Timed
    public ResponseEntity<List<VideoRecord>> getAllVideoRecords(Pageable pageable) {
        log.debug("REST request to get a page of VideoRecords");
        Page<VideoRecord> page = videoRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-records/:id : get the "id" videoRecord.
     *
     * @param id the id of the videoRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoRecord, or with status 404 (Not Found)
     */
    @GetMapping("/video-records/{id}")
    @Timed
    public ResponseEntity<VideoRecord> getVideoRecord(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        VideoRecord videoRecord = videoRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videoRecord));
    }

    /**
     * DELETE  /video-records/:id : delete the "id" videoRecord.
     *
     * @param id the id of the videoRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/video-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideoRecord(@PathVariable Long id) {
        log.debug("REST request to delete VideoRecord : {}", id);
        videoRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    @GetMapping("/video-records/getAllData/{id}")
    @Timed
    public ResponseEntity<List<VideoRecordQueryVM>> getAllDatagetAllData(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        List<VideoRecordQueryVM> result = new ArrayList<VideoRecordQueryVM>();
        
        Iterable<Map<String,Object>> videoRecords = videoRecordRepository.findAllByVideoId(id);
        for (Map<String, Object> map : videoRecords) {
        	result.add(new VideoRecordQueryVM((Long)map.get("id"), (String)map.get("vehicleType"),(Instant)map.get("insertDate"), (Long)map.get("lineId"),(Long)map.get("duration"),(Double)map.get("speed")));
        }
      
        for (VideoRecordQueryVM recordQueryVM : result) {
			Date myDate = Date.from(recordQueryVM.getInsertDate());
			System.out.println(sdf.format(myDate));
			
        }
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    @GetMapping("/video-records/getAllDataSummary/{id}")
    @Timed
    public ResponseEntity<List<VideoRecordSummaryVM>> getAllDataSummary(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
        
        Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getSummaryReport(id);
        for (Map<String, Object> map : videoRecords) {
        	result.add(new VideoRecordSummaryVM(map.get("grouptime"), (String)map.get("type"),(BigInteger)map.get("counts")));
		}
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    @GetMapping("/video-records/parseData")
    @Timed
    public ResponseEntity<Void> parseData() throws FileNotFoundException, IOException, CsvException, ParseException {
		
//    	Video video = videoRepository.findOne(2l);
    	VideoLine videoLine = videoLineRepository.findOne(2l);
    	
    	int i =0;
    	try (CSVReader reader = new CSVReader(new FileReader("C:\\Users\\ramazan\\Downloads\\nigth_2.csv"))) {
    	      List<String[]> r = reader.readAll();
    	      for (String[] strings : r) {
				
    	    	if(strings[2].equals("person"))
    	    		continue;
    	    	  
    	    	VideoRecord videoRecord = new VideoRecord();
				videoRecord.setVehicleType(strings[2]);
				videoRecord.setVideoLine(videoLine);
				videoRecord.setInsertDate(prepareDateValue(strings[1]));
				videoRecord.setDuration(prepareDuration(strings[1]));
//				videoRecord.setSpeed(prepareSpeed(strings[2]));
				videoRecordRepository.save(videoRecord);
				i++;
				System.out.println(i+" bitti");
				//break;
			 }
    	  }
    	
    	return null;
        
    }
    
    private Long prepareDuration(String dateValue) throws ParseException {
    	
    	if(dateValue.length()==14)
    	 	dateValue = dateValue.substring(0,11);
    	 else if(dateValue.length()==7) {
    		dateValue = dateValue+".000";
    	 }
    	
    	Date date = sdf.parse("1970-01-01 0"+dateValue);
    	return date.getTime();
    	
    }
    
    private Double prepareSpeed(String value)  {
    	
    	if(StringUtils.isEmpty(value) || value.equals("Unknown"))
    		return 0d;
    	
    	Double result = 0d;
		try {
			result = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    	
    }
    
    private Instant prepareDateValue(String dateValue) throws ParseException {
    	
    	if(dateValue.length()==14)
    	 	dateValue = dateValue.substring(0,12);
    	 else //if(dateValue.length()==7) 
    	 {
    		dateValue = dateValue+".000";
    	 }
    	
    	
    	Date date = sdf.parse("2000-01-01 0"+dateValue);
    	return date.toInstant();
    	
//    	return Instant.now();    	
    }
    
}
