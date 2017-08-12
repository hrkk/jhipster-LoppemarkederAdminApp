package dk.roninit.lopadminapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import dk.roninit.lopadminapp.domain.DateInterval;

import dk.roninit.lopadminapp.repository.DateIntervalRepository;
import dk.roninit.lopadminapp.repository.search.DateIntervalSearchRepository;
import dk.roninit.lopadminapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DateInterval.
 */
@RestController
@RequestMapping("/api")
public class DateIntervalResource {

    private final Logger log = LoggerFactory.getLogger(DateIntervalResource.class);

    private static final String ENTITY_NAME = "dateInterval";

    private final DateIntervalRepository dateIntervalRepository;

    private final DateIntervalSearchRepository dateIntervalSearchRepository;

    public DateIntervalResource(DateIntervalRepository dateIntervalRepository, DateIntervalSearchRepository dateIntervalSearchRepository) {
        this.dateIntervalRepository = dateIntervalRepository;
        this.dateIntervalSearchRepository = dateIntervalSearchRepository;
    }

    /**
     * POST  /date-intervals : Create a new dateInterval.
     *
     * @param dateInterval the dateInterval to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dateInterval, or with status 400 (Bad Request) if the dateInterval has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/date-intervals")
    @Timed
    public ResponseEntity<DateInterval> createDateInterval(@Valid @RequestBody DateInterval dateInterval) throws URISyntaxException {
        log.debug("REST request to save DateInterval : {}", dateInterval);
        if (dateInterval.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dateInterval cannot already have an ID")).body(null);
        }
        DateInterval result = dateIntervalRepository.save(dateInterval);
        dateIntervalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/date-intervals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /date-intervals : Updates an existing dateInterval.
     *
     * @param dateInterval the dateInterval to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dateInterval,
     * or with status 400 (Bad Request) if the dateInterval is not valid,
     * or with status 500 (Internal Server Error) if the dateInterval couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/date-intervals")
    @Timed
    public ResponseEntity<DateInterval> updateDateInterval(@Valid @RequestBody DateInterval dateInterval) throws URISyntaxException {
        log.debug("REST request to update DateInterval : {}", dateInterval);
        if (dateInterval.getId() == null) {
            return createDateInterval(dateInterval);
        }
        DateInterval result = dateIntervalRepository.save(dateInterval);
        dateIntervalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dateInterval.getId().toString()))
            .body(result);
    }

    /**
     * GET  /date-intervals : get all the dateIntervals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dateIntervals in body
     */
    @GetMapping("/date-intervals")
    @Timed
    public List<DateInterval> getAllDateIntervals() {
        log.debug("REST request to get all DateIntervals");
        return dateIntervalRepository.findAll();
    }

    /**
     * GET  /date-intervals/:id : get the "id" dateInterval.
     *
     * @param id the id of the dateInterval to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dateInterval, or with status 404 (Not Found)
     */
    @GetMapping("/date-intervals/{id}")
    @Timed
    public ResponseEntity<DateInterval> getDateInterval(@PathVariable Long id) {
        log.debug("REST request to get DateInterval : {}", id);
        DateInterval dateInterval = dateIntervalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dateInterval));
    }

    /**
     * DELETE  /date-intervals/:id : delete the "id" dateInterval.
     *
     * @param id the id of the dateInterval to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/date-intervals/{id}")
    @Timed
    public ResponseEntity<Void> deleteDateInterval(@PathVariable Long id) {
        log.debug("REST request to delete DateInterval : {}", id);
        dateIntervalRepository.delete(id);
        dateIntervalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/date-intervals?query=:query : search for the dateInterval corresponding
     * to the query.
     *
     * @param query the query of the dateInterval search
     * @return the result of the search
     */
    @GetMapping("/_search/date-intervals")
    @Timed
    public List<DateInterval> searchDateIntervals(@RequestParam String query) {
        log.debug("REST request to search DateIntervals for query {}", query);
        return StreamSupport
            .stream(dateIntervalSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
