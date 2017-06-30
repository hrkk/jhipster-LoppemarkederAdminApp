package dk.roninit.lop.admin.web.rest;

import com.codahale.metrics.annotation.Timed;
import dk.roninit.lop.admin.domain.DateInterval;
import dk.roninit.lop.admin.domain.MarkedItem;

import dk.roninit.lop.admin.repository.MarkedItemRepository;
import dk.roninit.lop.admin.repository.search.MarkedItemSearchRepository;
import dk.roninit.lop.admin.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MarkedItem.
 */
@RestController
@RequestMapping("/api")
public class MarkedItemResource {

    private final Logger log = LoggerFactory.getLogger(MarkedItemResource.class);

    private static final String ENTITY_NAME = "markedItem";

    private final MarkedItemRepository markedItemRepository;

    private final MarkedItemSearchRepository markedItemSearchRepository;

    public MarkedItemResource(MarkedItemRepository markedItemRepository, MarkedItemSearchRepository markedItemSearchRepository) {
        this.markedItemRepository = markedItemRepository;
        this.markedItemSearchRepository = markedItemSearchRepository;
    }

    /**
     * POST  /marked-items : Create a new markedItem.
     *
     * @param markedItem the markedItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new markedItem, or with status 400 (Bad Request) if the markedItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/marked-items")
    @Timed
    public ResponseEntity<MarkedItem> createMarkedItem(@Valid @RequestBody MarkedItem markedItem) throws URISyntaxException {
        log.debug("REST request to save MarkedItem : {}", markedItem);
        if (markedItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new markedItem cannot already have an ID")).body(null);
        }
        MarkedItem result = markedItemRepository.save(markedItem);
        markedItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/marked-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /marked-items : Updates an existing markedItem.
     *
     * @param markedItem the markedItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated markedItem,
     * or with status 400 (Bad Request) if the markedItem is not valid,
     * or with status 500 (Internal Server Error) if the markedItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/marked-items")
    @Timed
    public ResponseEntity<MarkedItem> updateMarkedItem(@Valid @RequestBody MarkedItem markedItem) throws URISyntaxException {
        log.debug("REST request to update MarkedItem : {}", markedItem);
        if (markedItem.getId() == null) {
            return createMarkedItem(markedItem);
        }
        MarkedItem result = markedItemRepository.save(markedItem);
        markedItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, markedItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /marked-items : get all the markedItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of markedItems in body
     */
    @GetMapping("/marked-items")
    @Timed
    public List<MarkedItem> getAllMarkedItems() {
        log.debug("REST request to get all MarkedItems");
        List<MarkedItem> all = markedItemRepository.findAll();
        // clear all
        all.stream().forEach(e -> e.getDateIntervals().stream().forEach(it -> it.setMarkedItem(null)));
        return all;
    }

    /**
     * GET  /marked-items/:id : get the "id" markedItem.
     *
     * @param id the id of the markedItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the markedItem, or with status 404 (Not Found)
     */
    @GetMapping("/marked-items/{id}")
    @Timed
    public ResponseEntity<MarkedItem> getMarkedItem(@PathVariable Long id) {
        log.debug("REST request to get MarkedItem : {}", id);
        MarkedItem markedItem = markedItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(markedItem));
    }

    /**
     * DELETE  /marked-items/:id : delete the "id" markedItem.
     *
     * @param id the id of the markedItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/marked-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarkedItem(@PathVariable Long id) {
        log.debug("REST request to delete MarkedItem : {}", id);
        markedItemRepository.delete(id);
        markedItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/marked-items?query=:query : search for the markedItem corresponding
     * to the query.
     *
     * @param query the query of the markedItem search
     * @return the result of the search
     */
    @GetMapping("/_search/marked-items")
    @Timed
    public List<MarkedItem> searchMarkedItems(@RequestParam String query) {
        log.debug("REST request to search MarkedItems for query {}", query);
        return StreamSupport
            .stream(markedItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
