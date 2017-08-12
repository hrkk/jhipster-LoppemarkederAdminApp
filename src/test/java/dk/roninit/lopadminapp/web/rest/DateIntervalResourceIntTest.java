package dk.roninit.lopadminapp.web.rest;

import dk.roninit.lopadminapp.LopadminappApp;

import dk.roninit.lopadminapp.domain.DateInterval;
import dk.roninit.lopadminapp.repository.DateIntervalRepository;
import dk.roninit.lopadminapp.repository.search.DateIntervalSearchRepository;
import dk.roninit.lopadminapp.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DateIntervalResource REST controller.
 *
 * @see DateIntervalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LopadminappApp.class)
public class DateIntervalResourceIntTest {

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DateIntervalRepository dateIntervalRepository;

    @Autowired
    private DateIntervalSearchRepository dateIntervalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDateIntervalMockMvc;

    private DateInterval dateInterval;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DateIntervalResource dateIntervalResource = new DateIntervalResource(dateIntervalRepository, dateIntervalSearchRepository);
        this.restDateIntervalMockMvc = MockMvcBuilders.standaloneSetup(dateIntervalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DateInterval createEntity(EntityManager em) {
        DateInterval dateInterval = new DateInterval()
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE);
        return dateInterval;
    }

    @Before
    public void initTest() {
        dateIntervalSearchRepository.deleteAll();
        dateInterval = createEntity(em);
    }

    @Test
    @Transactional
    public void createDateInterval() throws Exception {
        int databaseSizeBeforeCreate = dateIntervalRepository.findAll().size();

        // Create the DateInterval
        restDateIntervalMockMvc.perform(post("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dateInterval)))
            .andExpect(status().isCreated());

        // Validate the DateInterval in the database
        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeCreate + 1);
        DateInterval testDateInterval = dateIntervalList.get(dateIntervalList.size() - 1);
        assertThat(testDateInterval.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testDateInterval.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the DateInterval in Elasticsearch
        DateInterval dateIntervalEs = dateIntervalSearchRepository.findOne(testDateInterval.getId());
        assertThat(dateIntervalEs).isEqualToComparingFieldByField(testDateInterval);
    }

    @Test
    @Transactional
    public void createDateIntervalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dateIntervalRepository.findAll().size();

        // Create the DateInterval with an existing ID
        dateInterval.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDateIntervalMockMvc.perform(post("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dateInterval)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dateIntervalRepository.findAll().size();
        // set the field null
        dateInterval.setFromDate(null);

        // Create the DateInterval, which fails.

        restDateIntervalMockMvc.perform(post("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dateInterval)))
            .andExpect(status().isBadRequest());

        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dateIntervalRepository.findAll().size();
        // set the field null
        dateInterval.setToDate(null);

        // Create the DateInterval, which fails.

        restDateIntervalMockMvc.perform(post("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dateInterval)))
            .andExpect(status().isBadRequest());

        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDateIntervals() throws Exception {
        // Initialize the database
        dateIntervalRepository.saveAndFlush(dateInterval);

        // Get all the dateIntervalList
        restDateIntervalMockMvc.perform(get("/api/date-intervals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dateInterval.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDateInterval() throws Exception {
        // Initialize the database
        dateIntervalRepository.saveAndFlush(dateInterval);

        // Get the dateInterval
        restDateIntervalMockMvc.perform(get("/api/date-intervals/{id}", dateInterval.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dateInterval.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDateInterval() throws Exception {
        // Get the dateInterval
        restDateIntervalMockMvc.perform(get("/api/date-intervals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDateInterval() throws Exception {
        // Initialize the database
        dateIntervalRepository.saveAndFlush(dateInterval);
        dateIntervalSearchRepository.save(dateInterval);
        int databaseSizeBeforeUpdate = dateIntervalRepository.findAll().size();

        // Update the dateInterval
        DateInterval updatedDateInterval = dateIntervalRepository.findOne(dateInterval.getId());
        updatedDateInterval
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);

        restDateIntervalMockMvc.perform(put("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDateInterval)))
            .andExpect(status().isOk());

        // Validate the DateInterval in the database
        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeUpdate);
        DateInterval testDateInterval = dateIntervalList.get(dateIntervalList.size() - 1);
        assertThat(testDateInterval.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testDateInterval.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the DateInterval in Elasticsearch
        DateInterval dateIntervalEs = dateIntervalSearchRepository.findOne(testDateInterval.getId());
        assertThat(dateIntervalEs).isEqualToComparingFieldByField(testDateInterval);
    }

    @Test
    @Transactional
    public void updateNonExistingDateInterval() throws Exception {
        int databaseSizeBeforeUpdate = dateIntervalRepository.findAll().size();

        // Create the DateInterval

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDateIntervalMockMvc.perform(put("/api/date-intervals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dateInterval)))
            .andExpect(status().isCreated());

        // Validate the DateInterval in the database
        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDateInterval() throws Exception {
        // Initialize the database
        dateIntervalRepository.saveAndFlush(dateInterval);
        dateIntervalSearchRepository.save(dateInterval);
        int databaseSizeBeforeDelete = dateIntervalRepository.findAll().size();

        // Get the dateInterval
        restDateIntervalMockMvc.perform(delete("/api/date-intervals/{id}", dateInterval.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dateIntervalExistsInEs = dateIntervalSearchRepository.exists(dateInterval.getId());
        assertThat(dateIntervalExistsInEs).isFalse();

        // Validate the database is empty
        List<DateInterval> dateIntervalList = dateIntervalRepository.findAll();
        assertThat(dateIntervalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDateInterval() throws Exception {
        // Initialize the database
        dateIntervalRepository.saveAndFlush(dateInterval);
        dateIntervalSearchRepository.save(dateInterval);

        // Search the dateInterval
        restDateIntervalMockMvc.perform(get("/api/_search/date-intervals?query=id:" + dateInterval.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dateInterval.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DateInterval.class);
        DateInterval dateInterval1 = new DateInterval();
        dateInterval1.setId(1L);
        DateInterval dateInterval2 = new DateInterval();
        dateInterval2.setId(dateInterval1.getId());
        assertThat(dateInterval1).isEqualTo(dateInterval2);
        dateInterval2.setId(2L);
        assertThat(dateInterval1).isNotEqualTo(dateInterval2);
        dateInterval1.setId(null);
        assertThat(dateInterval1).isNotEqualTo(dateInterval2);
    }
}
