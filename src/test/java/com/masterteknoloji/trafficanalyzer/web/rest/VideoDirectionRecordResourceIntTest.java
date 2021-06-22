package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.TrafficanalyzerApp;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirectionRecord;
import com.masterteknoloji.trafficanalyzer.repository.VideoDirectionRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.ExceptionTranslator;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoDirectionRecordResource REST controller.
 *
 * @see VideoDirectionRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrafficanalyzerApp.class)
public class VideoDirectionRecordResourceIntTest {

    private static final Instant DEFAULT_INSERT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSERT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;

    private static final Double DEFAULT_SPEED = 1D;
    private static final Double UPDATED_SPEED = 2D;

    @Autowired
    private VideoDirectionRecordRepository videoDirectionRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideoDirectionRecordMockMvc;

    private VideoDirectionRecord videoDirectionRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoDirectionRecordResource videoDirectionRecordResource = new VideoDirectionRecordResource(videoDirectionRecordRepository);
        this.restVideoDirectionRecordMockMvc = MockMvcBuilders.standaloneSetup(videoDirectionRecordResource)
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
    public static VideoDirectionRecord createEntity(EntityManager em) {
        VideoDirectionRecord videoDirectionRecord = new VideoDirectionRecord()
            .insertDate(DEFAULT_INSERT_DATE)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .duration(DEFAULT_DURATION)
            .speed(DEFAULT_SPEED);
        return videoDirectionRecord;
    }

    @Before
    public void initTest() {
        videoDirectionRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoDirectionRecord() throws Exception {
        int databaseSizeBeforeCreate = videoDirectionRecordRepository.findAll().size();

        // Create the VideoDirectionRecord
        restVideoDirectionRecordMockMvc.perform(post("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoDirectionRecord in the database
        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeCreate + 1);
        VideoDirectionRecord testVideoDirectionRecord = videoDirectionRecordList.get(videoDirectionRecordList.size() - 1);
        assertThat(testVideoDirectionRecord.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testVideoDirectionRecord.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVideoDirectionRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoDirectionRecord.getSpeed()).isEqualTo(DEFAULT_SPEED);
    }

    @Test
    @Transactional
    public void createVideoDirectionRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoDirectionRecordRepository.findAll().size();

        // Create the VideoDirectionRecord with an existing ID
        videoDirectionRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoDirectionRecordMockMvc.perform(post("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isBadRequest());

        // Validate the VideoDirectionRecord in the database
        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInsertDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoDirectionRecordRepository.findAll().size();
        // set the field null
        videoDirectionRecord.setInsertDate(null);

        // Create the VideoDirectionRecord, which fails.

        restVideoDirectionRecordMockMvc.perform(post("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isBadRequest());

        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoDirectionRecordRepository.findAll().size();
        // set the field null
        videoDirectionRecord.setVehicleType(null);

        // Create the VideoDirectionRecord, which fails.

        restVideoDirectionRecordMockMvc.perform(post("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isBadRequest());

        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoDirectionRecordRepository.findAll().size();
        // set the field null
        videoDirectionRecord.setDuration(null);

        // Create the VideoDirectionRecord, which fails.

        restVideoDirectionRecordMockMvc.perform(post("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isBadRequest());

        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoDirectionRecords() throws Exception {
        // Initialize the database
        videoDirectionRecordRepository.saveAndFlush(videoDirectionRecord);

        // Get all the videoDirectionRecordList
        restVideoDirectionRecordMockMvc.perform(get("/api/video-direction-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoDirectionRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(DEFAULT_INSERT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())));
    }

    @Test
    @Transactional
    public void getVideoDirectionRecord() throws Exception {
        // Initialize the database
        videoDirectionRecordRepository.saveAndFlush(videoDirectionRecord);

        // Get the videoDirectionRecord
        restVideoDirectionRecordMockMvc.perform(get("/api/video-direction-records/{id}", videoDirectionRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoDirectionRecord.getId().intValue()))
            .andExpect(jsonPath("$.insertDate").value(DEFAULT_INSERT_DATE.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoDirectionRecord() throws Exception {
        // Get the videoDirectionRecord
        restVideoDirectionRecordMockMvc.perform(get("/api/video-direction-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoDirectionRecord() throws Exception {
        // Initialize the database
        videoDirectionRecordRepository.saveAndFlush(videoDirectionRecord);
        int databaseSizeBeforeUpdate = videoDirectionRecordRepository.findAll().size();

        // Update the videoDirectionRecord
        VideoDirectionRecord updatedVideoDirectionRecord = videoDirectionRecordRepository.findOne(videoDirectionRecord.getId());
        // Disconnect from session so that the updates on updatedVideoDirectionRecord are not directly saved in db
        em.detach(updatedVideoDirectionRecord);
        updatedVideoDirectionRecord
            .insertDate(UPDATED_INSERT_DATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .duration(UPDATED_DURATION)
            .speed(UPDATED_SPEED);

        restVideoDirectionRecordMockMvc.perform(put("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoDirectionRecord)))
            .andExpect(status().isOk());

        // Validate the VideoDirectionRecord in the database
        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeUpdate);
        VideoDirectionRecord testVideoDirectionRecord = videoDirectionRecordList.get(videoDirectionRecordList.size() - 1);
        assertThat(testVideoDirectionRecord.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testVideoDirectionRecord.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVideoDirectionRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoDirectionRecord.getSpeed()).isEqualTo(UPDATED_SPEED);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoDirectionRecord() throws Exception {
        int databaseSizeBeforeUpdate = videoDirectionRecordRepository.findAll().size();

        // Create the VideoDirectionRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoDirectionRecordMockMvc.perform(put("/api/video-direction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirectionRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoDirectionRecord in the database
        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideoDirectionRecord() throws Exception {
        // Initialize the database
        videoDirectionRecordRepository.saveAndFlush(videoDirectionRecord);
        int databaseSizeBeforeDelete = videoDirectionRecordRepository.findAll().size();

        // Get the videoDirectionRecord
        restVideoDirectionRecordMockMvc.perform(delete("/api/video-direction-records/{id}", videoDirectionRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoDirectionRecord> videoDirectionRecordList = videoDirectionRecordRepository.findAll();
        assertThat(videoDirectionRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoDirectionRecord.class);
        VideoDirectionRecord videoDirectionRecord1 = new VideoDirectionRecord();
        videoDirectionRecord1.setId(1L);
        VideoDirectionRecord videoDirectionRecord2 = new VideoDirectionRecord();
        videoDirectionRecord2.setId(videoDirectionRecord1.getId());
        assertThat(videoDirectionRecord1).isEqualTo(videoDirectionRecord2);
        videoDirectionRecord2.setId(2L);
        assertThat(videoDirectionRecord1).isNotEqualTo(videoDirectionRecord2);
        videoDirectionRecord1.setId(null);
        assertThat(videoDirectionRecord1).isNotEqualTo(videoDirectionRecord2);
    }
}
