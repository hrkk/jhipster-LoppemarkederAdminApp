package dk.roninit.lop.admin.web.rest;

import dk.roninit.lop.admin.LopadminappApp;

import dk.roninit.lop.admin.domain.MarkedItem;
import dk.roninit.lop.admin.repository.MarkedItemRepository;
import dk.roninit.lop.admin.repository.search.MarkedItemSearchRepository;
import dk.roninit.lop.admin.web.rest.errors.ExceptionTranslator;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MarkedItemResource REST controller.
 *
 * @see MarkedItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LopadminappApp.class)
public class MarkedItemResourceIntTest {

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

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Boolean DEFAULT_ENABLE_BOOKING = false;
    private static final Boolean UPDATED_ENABLE_BOOKING = true;

    @Autowired
    private MarkedItemRepository markedItemRepository;

    @Autowired
    private MarkedItemSearchRepository markedItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarkedItemMockMvc;

    private MarkedItem markedItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MarkedItemResource markedItemResource = new MarkedItemResource(markedItemRepository, markedItemSearchRepository);
        this.restMarkedItemMockMvc = MockMvcBuilders.standaloneSetup(markedItemResource)
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
    public static MarkedItem createEntity(EntityManager em) {
        MarkedItem markedItem = new MarkedItem()
            .name(DEFAULT_NAME)
            .markedInformation(DEFAULT_MARKED_INFORMATION)
            .markedRules(DEFAULT_MARKED_RULES)
            .entreInfo(DEFAULT_ENTRE_INFO)
            .dateExtraInfo(DEFAULT_DATE_EXTRA_INFO)
            .address(DEFAULT_ADDRESS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .enableBooking(DEFAULT_ENABLE_BOOKING);
        return markedItem;
    }

    @Before
    public void initTest() {
        markedItemSearchRepository.deleteAll();
        markedItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarkedItem() throws Exception {
        int databaseSizeBeforeCreate = markedItemRepository.findAll().size();

        // Create the MarkedItem
        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isCreated());

        // Validate the MarkedItem in the database
        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeCreate + 1);
        MarkedItem testMarkedItem = markedItemList.get(markedItemList.size() - 1);
        assertThat(testMarkedItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMarkedItem.getMarkedInformation()).isEqualTo(DEFAULT_MARKED_INFORMATION);
        assertThat(testMarkedItem.getMarkedRules()).isEqualTo(DEFAULT_MARKED_RULES);
        assertThat(testMarkedItem.getEntreInfo()).isEqualTo(DEFAULT_ENTRE_INFO);
        assertThat(testMarkedItem.getDateExtraInfo()).isEqualTo(DEFAULT_DATE_EXTRA_INFO);
        assertThat(testMarkedItem.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMarkedItem.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testMarkedItem.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testMarkedItem.isEnableBooking()).isEqualTo(DEFAULT_ENABLE_BOOKING);

        // Validate the MarkedItem in Elasticsearch
        MarkedItem markedItemEs = markedItemSearchRepository.findOne(testMarkedItem.getId());
        assertThat(markedItemEs).isEqualToComparingFieldByField(testMarkedItem);
    }

    @Test
    @Transactional
    public void createMarkedItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = markedItemRepository.findAll().size();

        // Create the MarkedItem with an existing ID
        markedItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setName(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarkedInformationIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setMarkedInformation(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarkedRulesIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setMarkedRules(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntreInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setEntreInfo(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setAddress(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setLatitude(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setLongitude(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnableBookingIsRequired() throws Exception {
        int databaseSizeBeforeTest = markedItemRepository.findAll().size();
        // set the field null
        markedItem.setEnableBooking(null);

        // Create the MarkedItem, which fails.

        restMarkedItemMockMvc.perform(post("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isBadRequest());

        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarkedItems() throws Exception {
        // Initialize the database
        markedItemRepository.saveAndFlush(markedItem);

        // Get all the markedItemList
        restMarkedItemMockMvc.perform(get("/api/marked-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(markedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].markedInformation").value(hasItem(DEFAULT_MARKED_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].markedRules").value(hasItem(DEFAULT_MARKED_RULES.toString())))
            .andExpect(jsonPath("$.[*].entreInfo").value(hasItem(DEFAULT_ENTRE_INFO.toString())))
            .andExpect(jsonPath("$.[*].dateExtraInfo").value(hasItem(DEFAULT_DATE_EXTRA_INFO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].enableBooking").value(hasItem(DEFAULT_ENABLE_BOOKING.booleanValue())));
    }

    @Test
    @Transactional
    public void getMarkedItem() throws Exception {
        // Initialize the database
        markedItemRepository.saveAndFlush(markedItem);

        // Get the markedItem
        restMarkedItemMockMvc.perform(get("/api/marked-items/{id}", markedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(markedItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.markedInformation").value(DEFAULT_MARKED_INFORMATION.toString()))
            .andExpect(jsonPath("$.markedRules").value(DEFAULT_MARKED_RULES.toString()))
            .andExpect(jsonPath("$.entreInfo").value(DEFAULT_ENTRE_INFO.toString()))
            .andExpect(jsonPath("$.dateExtraInfo").value(DEFAULT_DATE_EXTRA_INFO.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.enableBooking").value(DEFAULT_ENABLE_BOOKING.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMarkedItem() throws Exception {
        // Get the markedItem
        restMarkedItemMockMvc.perform(get("/api/marked-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarkedItem() throws Exception {
        // Initialize the database
        markedItemRepository.saveAndFlush(markedItem);
        markedItemSearchRepository.save(markedItem);
        int databaseSizeBeforeUpdate = markedItemRepository.findAll().size();

        // Update the markedItem
        MarkedItem updatedMarkedItem = markedItemRepository.findOne(markedItem.getId());
        updatedMarkedItem
            .name(UPDATED_NAME)
            .markedInformation(UPDATED_MARKED_INFORMATION)
            .markedRules(UPDATED_MARKED_RULES)
            .entreInfo(UPDATED_ENTRE_INFO)
            .dateExtraInfo(UPDATED_DATE_EXTRA_INFO)
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .enableBooking(UPDATED_ENABLE_BOOKING);

        restMarkedItemMockMvc.perform(put("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarkedItem)))
            .andExpect(status().isOk());

        // Validate the MarkedItem in the database
        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeUpdate);
        MarkedItem testMarkedItem = markedItemList.get(markedItemList.size() - 1);
        assertThat(testMarkedItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMarkedItem.getMarkedInformation()).isEqualTo(UPDATED_MARKED_INFORMATION);
        assertThat(testMarkedItem.getMarkedRules()).isEqualTo(UPDATED_MARKED_RULES);
        assertThat(testMarkedItem.getEntreInfo()).isEqualTo(UPDATED_ENTRE_INFO);
        assertThat(testMarkedItem.getDateExtraInfo()).isEqualTo(UPDATED_DATE_EXTRA_INFO);
        assertThat(testMarkedItem.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMarkedItem.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testMarkedItem.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testMarkedItem.isEnableBooking()).isEqualTo(UPDATED_ENABLE_BOOKING);

        // Validate the MarkedItem in Elasticsearch
        MarkedItem markedItemEs = markedItemSearchRepository.findOne(testMarkedItem.getId());
        assertThat(markedItemEs).isEqualToComparingFieldByField(testMarkedItem);
    }

    @Test
    @Transactional
    public void updateNonExistingMarkedItem() throws Exception {
        int databaseSizeBeforeUpdate = markedItemRepository.findAll().size();

        // Create the MarkedItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarkedItemMockMvc.perform(put("/api/marked-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(markedItem)))
            .andExpect(status().isCreated());

        // Validate the MarkedItem in the database
        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMarkedItem() throws Exception {
        // Initialize the database
        markedItemRepository.saveAndFlush(markedItem);
        markedItemSearchRepository.save(markedItem);
        int databaseSizeBeforeDelete = markedItemRepository.findAll().size();

        // Get the markedItem
        restMarkedItemMockMvc.perform(delete("/api/marked-items/{id}", markedItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean markedItemExistsInEs = markedItemSearchRepository.exists(markedItem.getId());
        assertThat(markedItemExistsInEs).isFalse();

        // Validate the database is empty
        List<MarkedItem> markedItemList = markedItemRepository.findAll();
        assertThat(markedItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarkedItem() throws Exception {
        // Initialize the database
        markedItemRepository.saveAndFlush(markedItem);
        markedItemSearchRepository.save(markedItem);

        // Search the markedItem
        restMarkedItemMockMvc.perform(get("/api/_search/marked-items?query=id:" + markedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(markedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].markedInformation").value(hasItem(DEFAULT_MARKED_INFORMATION.toString())))
            .andExpect(jsonPath("$.[*].markedRules").value(hasItem(DEFAULT_MARKED_RULES.toString())))
            .andExpect(jsonPath("$.[*].entreInfo").value(hasItem(DEFAULT_ENTRE_INFO.toString())))
            .andExpect(jsonPath("$.[*].dateExtraInfo").value(hasItem(DEFAULT_DATE_EXTRA_INFO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].enableBooking").value(hasItem(DEFAULT_ENABLE_BOOKING.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarkedItem.class);
        MarkedItem markedItem1 = new MarkedItem();
        markedItem1.setId(1L);
        MarkedItem markedItem2 = new MarkedItem();
        markedItem2.setId(markedItem1.getId());
        assertThat(markedItem1).isEqualTo(markedItem2);
        markedItem2.setId(2L);
        assertThat(markedItem1).isNotEqualTo(markedItem2);
        markedItem1.setId(null);
        assertThat(markedItem1).isNotEqualTo(markedItem2);
    }
}
