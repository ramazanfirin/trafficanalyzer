package com.masterteknoloji.trafficanalyzer.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.TrafficanalyzerApp;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.domain.VideoLine;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.repository.VideoLineRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.ExceptionTranslator;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordQueryVM;
import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoRecordResource REST controller.
 *
 * @see VideoRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrafficanalyzerApp.class)
public class VideoRecordResourceIntTest {

    private static final Instant DEFAULT_INSERT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSERT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    @Autowired
    private VideoRecordRepository videoRecordRepository;

    @Autowired
    VideoLineRepository videoLineRepository;

    @Autowired
    VideoRepository videoRepository;
    
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideoRecordMockMvc;

    private VideoRecord videoRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoRecordResource videoRecordResource = new VideoRecordResource(videoRecordRepository,videoLineRepository,videoRepository);
        this.restVideoRecordMockMvc = MockMvcBuilders.standaloneSetup(videoRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoRecord createEntity(EntityManager em) {
        VideoRecord videoRecord = new VideoRecord()
            .insertDate(DEFAULT_INSERT_DATE)
            .vehicleType(DEFAULT_VEHICLE_TYPE);
        return videoRecord;
    }

    @Before
    public void initTest() {
        videoRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoRecord() throws Exception {
        int databaseSizeBeforeCreate = videoRecordRepository.findAll().size();

        // Create the VideoRecord
        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeCreate + 1);
        VideoRecord testVideoRecord = videoRecordList.get(videoRecordList.size() - 1);
        assertThat(testVideoRecord.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testVideoRecord.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
    }

    @Test
    @Transactional
    public void createVideoRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRecordRepository.findAll().size();

        // Create the VideoRecord with an existing ID
        videoRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInsertDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRecordRepository.findAll().size();
        // set the field null
        videoRecord.setInsertDate(null);

        // Create the VideoRecord, which fails.

        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRecordRepository.findAll().size();
        // set the field null
        videoRecord.setVehicleType(null);

        // Create the VideoRecord, which fails.

        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoRecords() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);

        // Get all the videoRecordList
        restVideoRecordMockMvc.perform(get("/api/video-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(DEFAULT_INSERT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);

        // Get the videoRecord
        restVideoRecordMockMvc.perform(get("/api/video-records/{id}", videoRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoRecord.getId().intValue()))
            .andExpect(jsonPath("$.insertDate").value(DEFAULT_INSERT_DATE.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoRecord() throws Exception {
        // Get the videoRecord
        restVideoRecordMockMvc.perform(get("/api/video-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);
        int databaseSizeBeforeUpdate = videoRecordRepository.findAll().size();

        // Update the videoRecord
        VideoRecord updatedVideoRecord = videoRecordRepository.findOne(videoRecord.getId());
        // Disconnect from session so that the updates on updatedVideoRecord are not directly saved in db
        em.detach(updatedVideoRecord);
        updatedVideoRecord
            .insertDate(UPDATED_INSERT_DATE)
            .vehicleType(UPDATED_VEHICLE_TYPE);

        restVideoRecordMockMvc.perform(put("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoRecord)))
            .andExpect(status().isOk());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeUpdate);
        VideoRecord testVideoRecord = videoRecordList.get(videoRecordList.size() - 1);
        assertThat(testVideoRecord.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testVideoRecord.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoRecord() throws Exception {
        int databaseSizeBeforeUpdate = videoRecordRepository.findAll().size();

        // Create the VideoRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoRecordMockMvc.perform(put("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);
        int databaseSizeBeforeDelete = videoRecordRepository.findAll().size();

        // Get the videoRecord
        restVideoRecordMockMvc.perform(delete("/api/video-records/{id}", videoRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    
    @Test
    @Transactional
    public void getAllVideoRecordsByVideoId() throws Exception {
        // Initialize the database
    	Video video = new Video();
    	video.setName("test");
    	video.setPath("");
    	videoRepository.save(video);
    	
    	VideoLine line = new VideoLine();
    	line.setVideo(video);
    	videoLineRepository.save(line);
    	
    	videoRecord.setVideoLine(line);
        videoRecordRepository.saveAndFlush(videoRecord);

        // Get all the videoRecordList
        MvcResult mvcResult = restVideoRecordMockMvc.perform(get("/api/video-records/getAllData/"+video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();;
    
           
            
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
  	    List<VideoRecord> asList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<VideoRecordQueryVM>>() { });

        assertThat(asList).hasSize(1);

    }
    
    @Test
    public void parseDate() throws ParseException {
    	 DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	 sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
    	 
    	 String dateValue = "0:00:03.520000";
    	 String dateValue2 = "0:00:08";
    	 
    	 
    	 Date date = sdf2.parse("1970-01-01 0"+prepareDateValue(dateValue));
    	 System.out.println(sdf2.format(date));
    	 System.out.println(date.getTime());
    
    	 Date date2 = sdf2.parse("1970-01-01 0"+prepareDateValue(dateValue2));
    	 System.out.println(sdf2.format(date2));
    	 System.out.println(date2.getTime());
    
    }
    
    public String prepareDateValue(String dateValue) {
    	if(dateValue.length()==15)
    	 	dateValue = dateValue.substring(0,11);
    	 else if(dateValue.length()==7) {
    		dateValue = dateValue+".000";
    	 }
    	
    	return dateValue;
    }
    
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoRecord.class);
        VideoRecord videoRecord1 = new VideoRecord();
        videoRecord1.setId(1L);
        VideoRecord videoRecord2 = new VideoRecord();
        videoRecord2.setId(videoRecord1.getId());
        assertThat(videoRecord1).isEqualTo(videoRecord2);
        videoRecord2.setId(2L);
        assertThat(videoRecord1).isNotEqualTo(videoRecord2);
        videoRecord1.setId(null);
        assertThat(videoRecord1).isNotEqualTo(videoRecord2);
    }
}
