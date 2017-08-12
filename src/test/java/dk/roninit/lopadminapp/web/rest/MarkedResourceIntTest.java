package dk.roninit.lopadminapp.web.rest;

import dk.roninit.lopadminapp.LopadminappApp;

import dk.roninit.lopadminapp.domain.Marked;
import dk.roninit.lopadminapp.repository.MarkedRepository;
import dk.roninit.lopadminapp.repository.search.MarkedSearchRepository;
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
import java.util.List;

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
@SpringBootTest(classes = LopadminappApp.class)
public class MarkedResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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
        MarkedResource markedResource = new MarkedResource(markedRepository, markedSearchRepository);
        this.restMarkedMockMvc = MockMvcBuilders.standaloneSetup(markedResource)
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
    public static Marked createEntity(EntityManager em) {
        Marked marked = new Marked()
            .name(DEFAULT_NAME);
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

        // Validate the Marked in Elasticsearch
        Marked markedEs = markedSearchRepository.findOne(testMarked.getId());
        assertThat(markedEs).isEqualToComparingFieldByField(testMarked);
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

        // Validate the Alice in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeCreate);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
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
        updatedMarked
            .name(UPDATED_NAME);

        restMarkedMockMvc.perform(put("/api/markeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarked)))
            .andExpect(status().isOk());

        // Validate the Marked in the database
        List<Marked> markedList = markedRepository.findAll();
        assertThat(markedList).hasSize(databaseSizeBeforeUpdate);
        Marked testMarked = markedList.get(markedList.size() - 1);
        assertThat(testMarked.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Marked in Elasticsearch
        Marked markedEs = markedSearchRepository.findOne(testMarked.getId());
        assertThat(markedEs).isEqualToComparingFieldByField(testMarked);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
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
