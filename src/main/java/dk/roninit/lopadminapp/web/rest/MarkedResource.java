package dk.roninit.lopadminapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import dk.roninit.lopadminapp.domain.Marked;

import dk.roninit.lopadminapp.repository.MarkedRepository;
import dk.roninit.lopadminapp.repository.search.MarkedSearchRepository;
import dk.roninit.lopadminapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Marked.
 */
@RestController
@RequestMapping("/api")
public class MarkedResource {

    private final Logger log = LoggerFactory.getLogger(MarkedResource.class);

    private static final String ENTITY_NAME = "marked";

    private final MarkedRepository markedRepository;

    private final MarkedSearchRepository markedSearchRepository;

    public MarkedResource(MarkedRepository markedRepository, MarkedSearchRepository markedSearchRepository) {
        this.markedRepository = markedRepository;
        this.markedSearchRepository = markedSearchRepository;
    }

    /**
     * POST  /markeds : Create a new marked.
     *
     * @param marked the marked to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marked, or with status 400 (Bad Request) if the marked has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/markeds")
    @Timed
    public ResponseEntity<Marked> createMarked(@RequestBody Marked marked) throws URISyntaxException {
        log.debug("REST request to save Marked : {}", marked);
        if (marked.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new marked cannot already have an ID")).body(null);
        }
        Marked result = markedRepository.save(marked);
        markedSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/markeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /markeds : Updates an existing marked.
     *
     * @param marked the marked to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marked,
     * or with status 400 (Bad Request) if the marked is not valid,
     * or with status 500 (Internal Server Error) if the marked couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/markeds")
    @Timed
    public ResponseEntity<Marked> updateMarked(@RequestBody Marked marked) throws URISyntaxException {
        log.debug("REST request to update Marked : {}", marked);
        if (marked.getId() == null) {
            return createMarked(marked);
        }
        Marked result = markedRepository.save(marked);
        markedSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marked.getId().toString()))
            .body(result);
    }

    /**
     * GET  /markeds : get all the markeds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of markeds in body
     */
    @GetMapping("/markeds")
    @Timed
    public List<Marked> getAllMarkeds() {
        log.debug("REST request to get all Markeds");
        return markedRepository.findAll();
    }

    /**
     * GET  /markeds/:id : get the "id" marked.
     *
     * @param id the id of the marked to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marked, or with status 404 (Not Found)
     */
    @GetMapping("/markeds/{id}")
    @Timed
    public ResponseEntity<Marked> getMarked(@PathVariable Long id) {
        log.debug("REST request to get Marked : {}", id);
        Marked marked = markedRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marked));
    }

    /**
     * DELETE  /markeds/:id : delete the "id" marked.
     *
     * @param id the id of the marked to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/markeds/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarked(@PathVariable Long id) {
        log.debug("REST request to delete Marked : {}", id);
        markedRepository.delete(id);
        markedSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/markeds?query=:query : search for the marked corresponding
     * to the query.
     *
     * @param query the query of the marked search
     * @return the result of the search
     */
    @GetMapping("/_search/markeds")
    @Timed
    public List<Marked> searchMarkeds(@RequestParam String query) {
        log.debug("REST request to search Markeds for query {}", query);
        return StreamSupport
            .stream(markedSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
