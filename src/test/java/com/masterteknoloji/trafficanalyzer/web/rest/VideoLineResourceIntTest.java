package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.TrafficanalyzerApp;

import com.masterteknoloji.trafficanalyzer.domain.VideoLine;
import com.masterteknoloji.trafficanalyzer.repository.VideoLineRepository;
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
import java.util.List;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoLineResource REST controller.
 *
 * @see VideoLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrafficanalyzerApp.class)
public class VideoLineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_START_POINT_X = 1L;
    private static final Long UPDATED_START_POINT_X = 2L;

    private static final Long DEFAULT_START_POINT_Y = 1L;
    private static final Long UPDATED_START_POINT_Y = 2L;

    private static final Long DEFAULT_END_POINT_X = 1L;
    private static final Long UPDATED_END_POINT_X = 2L;

    private static final Long DEFAULT_END_POINT_Y = 1L;
    private static final Long UPDATED_END_POINT_Y = 2L;

    @Autowired
    private VideoLineRepository videoLineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideoLineMockMvc;

    private VideoLine videoLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoLineResource videoLineResource = new VideoLineResource(videoLineRepository);
        this.restVideoLineMockMvc = MockMvcBuilders.standaloneSetup(videoLineResource)
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
    public static VideoLine createEntity(EntityManager em) {
        VideoLine videoLine = new VideoLine()
            .name(DEFAULT_NAME)
            .startPointX(DEFAULT_START_POINT_X)
            .startPointY(DEFAULT_START_POINT_Y)
            .endPointX(DEFAULT_END_POINT_X)
            .endPointY(DEFAULT_END_POINT_Y);
        return videoLine;
    }

    @Before
    public void initTest() {
        videoLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoLine() throws Exception {
        int databaseSizeBeforeCreate = videoLineRepository.findAll().size();

        // Create the VideoLine
        restVideoLineMockMvc.perform(post("/api/video-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoLine)))
            .andExpect(status().isCreated());

        // Validate the VideoLine in the database
        List<VideoLine> videoLineList = videoLineRepository.findAll();
        assertThat(videoLineList).hasSize(databaseSizeBeforeCreate + 1);
        VideoLine testVideoLine = videoLineList.get(videoLineList.size() - 1);
        assertThat(testVideoLine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVideoLine.getStartPointX()).isEqualTo(DEFAULT_START_POINT_X);
        assertThat(testVideoLine.getStartPointY()).isEqualTo(DEFAULT_START_POINT_Y);
        assertThat(testVideoLine.getEndPointX()).isEqualTo(DEFAULT_END_POINT_X);
        assertThat(testVideoLine.getEndPointY()).isEqualTo(DEFAULT_END_POINT_Y);
    }

    @Test
    @Transactional
    public void createVideoLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoLineRepository.findAll().size();

        // Create the VideoLine with an existing ID
        videoLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoLineMockMvc.perform(post("/api/video-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoLine)))
            .andExpect(status().isBadRequest());

        // Validate the VideoLine in the database
        List<VideoLine> videoLineList = videoLineRepository.findAll();
        assertThat(videoLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVideoLines() throws Exception {
        // Initialize the database
        videoLineRepository.saveAndFlush(videoLine);

        // Get all the videoLineList
        restVideoLineMockMvc.perform(get("/api/video-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startPointX").value(hasItem(DEFAULT_START_POINT_X.intValue())))
            .andExpect(jsonPath("$.[*].startPointY").value(hasItem(DEFAULT_START_POINT_Y.intValue())))
            .andExpect(jsonPath("$.[*].endPointX").value(hasItem(DEFAULT_END_POINT_X.intValue())))
            .andExpect(jsonPath("$.[*].endPointY").value(hasItem(DEFAULT_END_POINT_Y.intValue())));
    }

    @Test
    @Transactional
    public void getVideoLine() throws Exception {
        // Initialize the database
        videoLineRepository.saveAndFlush(videoLine);

        // Get the videoLine
        restVideoLineMockMvc.perform(get("/api/video-lines/{id}", videoLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoLine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startPointX").value(DEFAULT_START_POINT_X.intValue()))
            .andExpect(jsonPath("$.startPointY").value(DEFAULT_START_POINT_Y.intValue()))
            .andExpect(jsonPath("$.endPointX").value(DEFAULT_END_POINT_X.intValue()))
            .andExpect(jsonPath("$.endPointY").value(DEFAULT_END_POINT_Y.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoLine() throws Exception {
        // Get the videoLine
        restVideoLineMockMvc.perform(get("/api/video-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoLine() throws Exception {
        // Initialize the database
        videoLineRepository.saveAndFlush(videoLine);
        int databaseSizeBeforeUpdate = videoLineRepository.findAll().size();

        // Update the videoLine
        VideoLine updatedVideoLine = videoLineRepository.findOne(videoLine.getId());
        // Disconnect from session so that the updates on updatedVideoLine are not directly saved in db
        em.detach(updatedVideoLine);
        updatedVideoLine
            .name(UPDATED_NAME)
            .startPointX(UPDATED_START_POINT_X)
            .startPointY(UPDATED_START_POINT_Y)
            .endPointX(UPDATED_END_POINT_X)
            .endPointY(UPDATED_END_POINT_Y);

        restVideoLineMockMvc.perform(put("/api/video-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoLine)))
            .andExpect(status().isOk());

        // Validate the VideoLine in the database
        List<VideoLine> videoLineList = videoLineRepository.findAll();
        assertThat(videoLineList).hasSize(databaseSizeBeforeUpdate);
        VideoLine testVideoLine = videoLineList.get(videoLineList.size() - 1);
        assertThat(testVideoLine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideoLine.getStartPointX()).isEqualTo(UPDATED_START_POINT_X);
        assertThat(testVideoLine.getStartPointY()).isEqualTo(UPDATED_START_POINT_Y);
        assertThat(testVideoLine.getEndPointX()).isEqualTo(UPDATED_END_POINT_X);
        assertThat(testVideoLine.getEndPointY()).isEqualTo(UPDATED_END_POINT_Y);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoLine() throws Exception {
        int databaseSizeBeforeUpdate = videoLineRepository.findAll().size();

        // Create the VideoLine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoLineMockMvc.perform(put("/api/video-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoLine)))
            .andExpect(status().isCreated());

        // Validate the VideoLine in the database
        List<VideoLine> videoLineList = videoLineRepository.findAll();
        assertThat(videoLineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideoLine() throws Exception {
        // Initialize the database
        videoLineRepository.saveAndFlush(videoLine);
        int databaseSizeBeforeDelete = videoLineRepository.findAll().size();

        // Get the videoLine
        restVideoLineMockMvc.perform(delete("/api/video-lines/{id}", videoLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoLine> videoLineList = videoLineRepository.findAll();
        assertThat(videoLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoLine.class);
        VideoLine videoLine1 = new VideoLine();
        videoLine1.setId(1L);
        VideoLine videoLine2 = new VideoLine();
        videoLine2.setId(videoLine1.getId());
        assertThat(videoLine1).isEqualTo(videoLine2);
        videoLine2.setId(2L);
        assertThat(videoLine1).isNotEqualTo(videoLine2);
        videoLine1.setId(null);
        assertThat(videoLine1).isNotEqualTo(videoLine2);
    }
}
