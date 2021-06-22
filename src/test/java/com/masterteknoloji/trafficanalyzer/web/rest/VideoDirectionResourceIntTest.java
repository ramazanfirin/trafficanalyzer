package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.TrafficanalyzerApp;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirection;
import com.masterteknoloji.trafficanalyzer.repository.VideoDirectionRepository;
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
 * Test class for the VideoDirectionResource REST controller.
 *
 * @see VideoDirectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrafficanalyzerApp.class)
public class VideoDirectionResourceIntTest {

    private static final Long DEFAULT_INDEX_VALUE = 1L;
    private static final Long UPDATED_INDEX_VALUE = 2L;

    @Autowired
    private VideoDirectionRepository videoDirectionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideoDirectionMockMvc;

    private VideoDirection videoDirection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoDirectionResource videoDirectionResource = new VideoDirectionResource(videoDirectionRepository);
        this.restVideoDirectionMockMvc = MockMvcBuilders.standaloneSetup(videoDirectionResource)
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
    public static VideoDirection createEntity(EntityManager em) {
        VideoDirection videoDirection = new VideoDirection()
            .indexValue(DEFAULT_INDEX_VALUE);
        return videoDirection;
    }

    @Before
    public void initTest() {
        videoDirection = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoDirection() throws Exception {
        int databaseSizeBeforeCreate = videoDirectionRepository.findAll().size();

        // Create the VideoDirection
        restVideoDirectionMockMvc.perform(post("/api/video-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirection)))
            .andExpect(status().isCreated());

        // Validate the VideoDirection in the database
        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeCreate + 1);
        VideoDirection testVideoDirection = videoDirectionList.get(videoDirectionList.size() - 1);
        assertThat(testVideoDirection.getIndexValue()).isEqualTo(DEFAULT_INDEX_VALUE);
    }

    @Test
    @Transactional
    public void createVideoDirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoDirectionRepository.findAll().size();

        // Create the VideoDirection with an existing ID
        videoDirection.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoDirectionMockMvc.perform(post("/api/video-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirection)))
            .andExpect(status().isBadRequest());

        // Validate the VideoDirection in the database
        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIndexValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoDirectionRepository.findAll().size();
        // set the field null
        videoDirection.setIndexValue(null);

        // Create the VideoDirection, which fails.

        restVideoDirectionMockMvc.perform(post("/api/video-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirection)))
            .andExpect(status().isBadRequest());

        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoDirections() throws Exception {
        // Initialize the database
        videoDirectionRepository.saveAndFlush(videoDirection);

        // Get all the videoDirectionList
        restVideoDirectionMockMvc.perform(get("/api/video-directions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoDirection.getId().intValue())))
            .andExpect(jsonPath("$.[*].indexValue").value(hasItem(DEFAULT_INDEX_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getVideoDirection() throws Exception {
        // Initialize the database
        videoDirectionRepository.saveAndFlush(videoDirection);

        // Get the videoDirection
        restVideoDirectionMockMvc.perform(get("/api/video-directions/{id}", videoDirection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoDirection.getId().intValue()))
            .andExpect(jsonPath("$.indexValue").value(DEFAULT_INDEX_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoDirection() throws Exception {
        // Get the videoDirection
        restVideoDirectionMockMvc.perform(get("/api/video-directions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoDirection() throws Exception {
        // Initialize the database
        videoDirectionRepository.saveAndFlush(videoDirection);
        int databaseSizeBeforeUpdate = videoDirectionRepository.findAll().size();

        // Update the videoDirection
        VideoDirection updatedVideoDirection = videoDirectionRepository.findOne(videoDirection.getId());
        // Disconnect from session so that the updates on updatedVideoDirection are not directly saved in db
        em.detach(updatedVideoDirection);
        updatedVideoDirection
            .indexValue(UPDATED_INDEX_VALUE);

        restVideoDirectionMockMvc.perform(put("/api/video-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoDirection)))
            .andExpect(status().isOk());

        // Validate the VideoDirection in the database
        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeUpdate);
        VideoDirection testVideoDirection = videoDirectionList.get(videoDirectionList.size() - 1);
        assertThat(testVideoDirection.getIndexValue()).isEqualTo(UPDATED_INDEX_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoDirection() throws Exception {
        int databaseSizeBeforeUpdate = videoDirectionRepository.findAll().size();

        // Create the VideoDirection

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoDirectionMockMvc.perform(put("/api/video-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoDirection)))
            .andExpect(status().isCreated());

        // Validate the VideoDirection in the database
        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideoDirection() throws Exception {
        // Initialize the database
        videoDirectionRepository.saveAndFlush(videoDirection);
        int databaseSizeBeforeDelete = videoDirectionRepository.findAll().size();

        // Get the videoDirection
        restVideoDirectionMockMvc.perform(delete("/api/video-directions/{id}", videoDirection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoDirection> videoDirectionList = videoDirectionRepository.findAll();
        assertThat(videoDirectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoDirection.class);
        VideoDirection videoDirection1 = new VideoDirection();
        videoDirection1.setId(1L);
        VideoDirection videoDirection2 = new VideoDirection();
        videoDirection2.setId(videoDirection1.getId());
        assertThat(videoDirection1).isEqualTo(videoDirection2);
        videoDirection2.setId(2L);
        assertThat(videoDirection1).isNotEqualTo(videoDirection2);
        videoDirection1.setId(null);
        assertThat(videoDirection1).isNotEqualTo(videoDirection2);
    }
}
