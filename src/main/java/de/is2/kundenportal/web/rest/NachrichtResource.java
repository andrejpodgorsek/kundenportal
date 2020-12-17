package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.Nachricht;
import de.is2.kundenportal.repository.NachrichtRepository;
import de.is2.kundenportal.repository.search.NachrichtSearchRepository;
import de.is2.kundenportal.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.Nachricht}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NachrichtResource {

    private final Logger log = LoggerFactory.getLogger(NachrichtResource.class);

    private static final String ENTITY_NAME = "nachricht";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NachrichtRepository nachrichtRepository;

    private final NachrichtSearchRepository nachrichtSearchRepository;

    public NachrichtResource(NachrichtRepository nachrichtRepository, NachrichtSearchRepository nachrichtSearchRepository) {
        this.nachrichtRepository = nachrichtRepository;
        this.nachrichtSearchRepository = nachrichtSearchRepository;
    }

    /**
     * {@code POST  /nachrichts} : Create a new nachricht.
     *
     * @param nachricht the nachricht to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nachricht, or with status {@code 400 (Bad Request)} if the nachricht has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nachrichts")
    public ResponseEntity<Nachricht> createNachricht(@Valid @RequestBody Nachricht nachricht) throws URISyntaxException {
        log.debug("REST request to save Nachricht : {}", nachricht);
        if (nachricht.getId() != null) {
            throw new BadRequestAlertException("A new nachricht cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Nachricht result = nachrichtRepository.save(nachricht);
        nachrichtSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/nachrichts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nachrichts} : Updates an existing nachricht.
     *
     * @param nachricht the nachricht to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nachricht,
     * or with status {@code 400 (Bad Request)} if the nachricht is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nachricht couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nachrichts")
    public ResponseEntity<Nachricht> updateNachricht(@Valid @RequestBody Nachricht nachricht) throws URISyntaxException {
        log.debug("REST request to update Nachricht : {}", nachricht);
        if (nachricht.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Nachricht result = nachrichtRepository.save(nachricht);
        nachrichtSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nachricht.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nachrichts} : get all the nachrichts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nachrichts in body.
     */
    @GetMapping("/nachrichts")
    public ResponseEntity<List<Nachricht>> getAllNachrichts(Pageable pageable) {
        log.debug("REST request to get a page of Nachrichts");
        Page<Nachricht> page = nachrichtRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nachrichts/:id} : get the "id" nachricht.
     *
     * @param id the id of the nachricht to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nachricht, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nachrichts/{id}")
    public ResponseEntity<Nachricht> getNachricht(@PathVariable Long id) {
        log.debug("REST request to get Nachricht : {}", id);
        Optional<Nachricht> nachricht = nachrichtRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nachricht);
    }

    /**
     * {@code DELETE  /nachrichts/:id} : delete the "id" nachricht.
     *
     * @param id the id of the nachricht to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nachrichts/{id}")
    public ResponseEntity<Void> deleteNachricht(@PathVariable Long id) {
        log.debug("REST request to delete Nachricht : {}", id);
        nachrichtRepository.deleteById(id);
        nachrichtSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/nachrichts?query=:query} : search for the nachricht corresponding
     * to the query.
     *
     * @param query the query of the nachricht search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nachrichts")
    public ResponseEntity<List<Nachricht>> searchNachrichts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Nachrichts for query {}", query);
        Page<Nachricht> page = nachrichtSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
