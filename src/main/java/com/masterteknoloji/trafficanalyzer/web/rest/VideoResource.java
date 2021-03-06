package com.masterteknoloji.trafficanalyzer.web.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.repository.VideoRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Video.
 */
@RestController
@RequestMapping("/api")
public class VideoResource {

    private final Logger log = LoggerFactory.getLogger(VideoResource.class);

    private static final String ENTITY_NAME = "video";

    private final VideoRepository videoRepository;
    
    private final ApplicationProperties applicationProperties;

    public VideoResource(VideoRepository videoRepository,ApplicationProperties applicationProperties) {
        this.videoRepository = videoRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /videos : Create a new video.
     *
     * @param video the video to create
     * @return the ResponseEntity with status 201 (Created) and with body the new video, or with status 400 (Bad Request) if the video has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/videos")
    @Timed
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to save Video : {}", video);
        if (video.getId() != null) {
            throw new BadRequestAlertException("A new video cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Video result = videoRepository.save(video);
        return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /videos : Updates an existing video.
     *
     * @param video the video to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated video,
     * or with status 400 (Bad Request) if the video is not valid,
     * or with status 500 (Internal Server Error) if the video couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/videos")
    @Timed
    public ResponseEntity<Video> updateVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to update Video : {}", video);
        if (video.getId() == null) {
            return createVideo(video);
        }
        Video result = videoRepository.save(video);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, video.getId().toString()))
            .body(result);
    }

    /**
     * GET  /videos : get all the videos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videos in body
     */
    @GetMapping("/videos")
    @Timed
    public ResponseEntity<List<Video>> getAllVideos(Pageable pageable) {
        log.debug("REST request to get a page of Videos");
        Page<Video> page = videoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/videos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /videos/:id : get the "id" video.
     *
     * @param id the id of the video to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the video, or with status 404 (Not Found)
     */
    @GetMapping("/videos/{id}")
    @Timed
    public ResponseEntity<Video> getVideo(@PathVariable Long id) {
        log.debug("REST request to get Video : {}", id);
        Video video = videoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(video));
    }

    /**
     * DELETE  /videos/:id : delete the "id" video.
     *
     * @param id the id of the video to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/videos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        log.debug("REST request to delete Video : {}", id);
        videoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping(value = "/videos/image/{name}")
    public @ResponseBody byte[] getImage(@PathVariable String name) throws IOException {
        
    	String filePath = applicationProperties.getVideoFilesPath()+"\\"+name+".mp4";
    	
    	File file = new File(filePath);
    	file.exists();
    	
    	
    	FFmpegFrameGrabber g = new FFmpegFrameGrabber(filePath);
		g.start();

		Java2DFrameConverter bimConverter = new Java2DFrameConverter();
		
		BufferedImage image=null;
		for (int i = 0 ; i < 5 ; i++) {
		    
		Frame f =	g.grabFrame();
			image = bimConverter.convert(f);
			
		}

		
		
		g.stop();
		
		bimConverter.close();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  ImageIO.write(image, "jpg", baos);
        
        
       return baos.toByteArray();
      
    }
    
    //@Scheduled(fixedRate = 60000)
    public void synchFileList() throws IOException {
    
    	log.info("synchFileList ba??ladi" );
    	List<Video> videoList = videoRepository.findAll();
    	List<String> list = Arrays.asList(new File(applicationProperties.getVideoFilesPath()).list());
    	
    	for (String string : list) {
			if(getVideoByName(videoList, string)==null){
				Video videoNew = new Video();
				videoNew.setName(string);
				videoNew.setPath(string);
				videoRepository.save(videoNew);
				log.info("synchFileList -"+videoNew+" oluturuldu" );
		        
			}
		}
    	
    	
    	log.info("synchFileList bitti" );
   }
    
    public Video getVideoByName(List<Video> videoList,String name) {
    	for (Video video : videoList) {
			if(video.getName().equals(name))
				return video;
		}
    	return null;
    }
}
