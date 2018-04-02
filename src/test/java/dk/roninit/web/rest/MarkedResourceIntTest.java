package dk.roninit.web.rest;

import dk.roninit.LoppemarkederAdminApp;

import dk.roninit.domain.Marked;
import dk.roninit.repository.MarkedRepository;
import dk.roninit.repository.search.MarkedSearchRepository;
import dk.roninit.web.rest.errors.ExceptionTranslator;

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

import static dk.roninit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MarkedResource REST controller.
 *
 * @see MarkedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoppemarkederAdminApp.class)
public class MarkedResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MARKED_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_MARKED_INFORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_MARKED_RULES = "AAAAAAAAAA";
    private static final String UPDATED_MARKED_RULES = "BBBBBBBBBB";

    private static final String DEFAULT_ENTRE_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ENTRE_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_EXTRA_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DATE_EXTRA_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Boolean DEFAULT_ENABLE_BOOKING = false;
    private static final Boolean UPDATED_ENABLE_BOOKING = true;

    @Autowired
    private MarkedRepository markedRepository;

    @Autowired
    private MarkedSearchRepository markedSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarkedMockMvc;

    private Marked marked;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarkedResource markedResource = new MarkedResource(markedRepository, markedSearchRepository);
        this.restMarkedMockMvc = MockMvcBuilders.standaloneSetup(markedResource)
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
    public static Marked createEntity(EntityManager em) {
        Marked marked = new Marked()
            .name(DEFAULT_NAME)
            .markedInformation(DEFAULT_MARKED_INFORMATION)
            .markedRules(DEFAULT_MARKED_RULES)
            .entreInfo(DEFAULT_ENTRE_INFO)
            .dateExtraInfo(DEFAULT_DATE_EXTRA_INFO)
            .address(DEFAULT_ADDRESS)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .enableBooking(DEFAULT_ENABLE_BOOKING);
        return marked;
    }

    @Before
    public void initTest() {
        markedSearchRepository.deleteAll();
        marked = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarked() throws Exception {
        int databaseSizeBeforeCreate = markedRepository.findAll().size();

        // Create the Marked
        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isCreated());

        // Validate the Marked in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeCreate + 1);
        Marked testMarked = markedList.get(markedList.size() - 1);
        assertThat(testMarked.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMarked.getMarkedInformation()).isEqualTo(DEFAULT_MARKED_INFORMATION);
        assertThat(testMarked.getMarkedRules()).isEqualTo(DEFAULT_MARKED_RULES);
        assertThat(testMarked.getEntreInfo()).isEqualTo(DEFAULT_ENTRE_INFO);
        assertThat(testMarked.getDateExtraInfo()).isEqualTo(DEFAULT_DATE_EXTRA_INFO);
        assertThat(testMarked.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMarked.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testMarked.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testMarked.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testMarked.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testMarked.isEnableBooking()).isEqualTo(DEFAULT_ENABLE_BOOKING);

        // Validate the Marked in Elasticsearch
        Marked markedEs = markedSearchRepository.findOne(testMarked.getId());
        assertThat(markedEs).isEqualToIgnoringGivenFields(testMarked);
    }

    @Test
    @Transactional
    public void createMarkedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = markedRepository.findAll().size();

        // Create the Marked with an existing ID
        marked.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        // Validate the Marked in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setName(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarkedInformationIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setMarkedInformation(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarkedRulesIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setMarkedRules(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntreInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setEntreInfo(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setAddress(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setFromDate(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setToDate(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setLatitude(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setLongitude(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnableBookingIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedRepository.findAll().size();
        // set the field null
        marked.setEnableBooking(null);

        // Create the Marked, which fails.

        restMarkedMockMvc.perform(post("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isBadRequest());

        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarkeds() throws Exception {
        // Initialize the database
        markedRepository.saveAndFlush(marked);

        // Get all the markedList
        restMarkedMockMvc.perform(get("/api/markeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marked.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].markedInformation").value(hasItem(DEFAULT_MARKED_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].markedRules").value(hasItem(DEFAULT_MARKED_RULES.toString())))
            .andExpect(jsonPath("$.[*].entreInfo").value(hasItem(DEFAULT_ENTRE_INFO.toString())))
            .andExpect(jsonPath("$.[*].dateExtraInfo").value(hasItem(DEFAULT_DATE_EXTRA_INFO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].enableBooking").value(hasItem(DEFAULT_ENABLE_BOOKING.booleanValue())));
    }

    @Test
    @Transactional
    public void getMarked() throws Exception {
        // Initialize the database
        markedRepository.saveAndFlush(marked);

        // Get the marked
        restMarkedMockMvc.perform(get("/api/markeds/{id}", marked.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marked.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.markedInformation").value(DEFAULT_MARKED_INFORMATION.toString()))
            .andExpect(jsonPath("$.markedRules").value(DEFAULT_MARKED_RULES.toString()))
            .andExpect(jsonPath("$.entreInfo").value(DEFAULT_ENTRE_INFO.toString()))
            .andExpect(jsonPath("$.dateExtraInfo").value(DEFAULT_DATE_EXTRA_INFO.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.enableBooking").value(DEFAULT_ENABLE_BOOKING.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMarked() throws Exception {
        // Get the marked
        restMarkedMockMvc.perform(get("/api/markeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarked() throws Exception {
        // Initialize the database
        markedRepository.saveAndFlush(marked);
        markedSearchRepository.save(marked);
        int databaseSizeBeforeUpdate = markedRepository.findAll().size();

        // Update the marked
        Marked updatedMarked = markedRepository.findOne(marked.getId());
        // Disconnect from session so that the updates on updatedMarked are not directly saved in db
        em.detach(updatedMarked);
        updatedMarked
            .name(UPDATED_NAME)
            .markedInformation(UPDATED_MARKED_INFORMATION)
            .markedRules(UPDATED_MARKED_RULES)
            .entreInfo(UPDATED_ENTRE_INFO)
            .dateExtraInfo(UPDATED_DATE_EXTRA_INFO)
            .address(UPDATED_ADDRESS)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .enableBooking(UPDATED_ENABLE_BOOKING);

        restMarkedMockMvc.perform(put("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarked)))
            .andExpect(status().isOk());

        // Validate the Marked in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeUpdate);
        Marked testMarked = markedList.get(markedList.size() - 1);
        assertThat(testMarked.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMarked.getMarkedInformation()).isEqualTo(UPDATED_MARKED_INFORMATION);
        assertThat(testMarked.getMarkedRules()).isEqualTo(UPDATED_MARKED_RULES);
        assertThat(testMarked.getEntreInfo()).isEqualTo(UPDATED_ENTRE_INFO);
        assertThat(testMarked.getDateExtraInfo()).isEqualTo(UPDATED_DATE_EXTRA_INFO);
        assertThat(testMarked.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMarked.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testMarked.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testMarked.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testMarked.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testMarked.isEnableBooking()).isEqualTo(UPDATED_ENABLE_BOOKING);

        // Validate the Marked in Elasticsearch
        Marked markedEs = markedSearchRepository.findOne(testMarked.getId());
        assertThat(markedEs).isEqualToIgnoringGivenFields(testMarked);
    }

    @Test
    @Transactional
    public void updateNonExistingMarked() throws Exception {
        int databaseSizeBeforeUpdate = markedRepository.findAll().size();

        // Create the Marked

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarkedMockMvc.perform(put("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marked)))
            .andExpect(status().isCreated());

        // Validate the Marked in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMarked() throws Exception {
        // Initialize the database
        markedRepository.saveAndFlush(marked);
        markedSearchRepository.save(marked);
        int databaseSizeBeforeDelete = markedRepository.findAll().size();

        // Get the marked
        restMarkedMockMvc.perform(delete("/api/markeds/{id}", marked.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean markedExistsInEs = markedSearchRepository.exists(marked.getId());
        assertThat(markedExistsInEs).isFalse();

        // Validate the database is empty
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarked() throws Exception {
        // Initialize the database
        markedRepository.saveAndFlush(marked);
        markedSearchRepository.save(marked);

        // Search the marked
        restMarkedMockMvc.perform(get("/api/_search/markeds?query=id:" + marked.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marked.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].markedInformation").value(hasItem(DEFAULT_MARKED_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].markedRules").value(hasItem(DEFAULT_MARKED_RULES.toString())))
            .andExpect(jsonPath("$.[*].entreInfo").value(hasItem(DEFAULT_ENTRE_INFO.toString())))
            .andExpect(jsonPath("$.[*].dateExtraInfo").value(hasItem(DEFAULT_DATE_EXTRA_INFO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].enableBooking").value(hasItem(DEFAULT_ENABLE_BOOKING.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marked.class);
        Marked marked1 = new Marked();
        marked1.setId(1L);
        Marked marked2 = new Marked();
        marked2.setId(marked1.getId());
        assertThat(marked1).isEqualTo(marked2);
        marked2.setId(2L);
        assertThat(marked1).isNotEqualTo(marked2);
        marked1.setId(null);
        assertThat(marked1).isNotEqualTo(marked2);
    }
}
