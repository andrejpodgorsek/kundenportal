package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.Vertrag;
import de.is2.kundenportal.repository.VertragRepository;
import de.is2.kundenportal.repository.search.VertragSearchRepository;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.Vertrag}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VertragResource {

    private final Logger log = LoggerFactory.getLogger(VertragResource.class);

    private static final String ENTITY_NAME = "vertrag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VertragRepository vertragRepository;

    private final VertragSearchRepository vertragSearchRepository;

    public VertragResource(VertragRepository vertragRepository, VertragSearchRepository vertragSearchRepository) {
        this.vertragRepository = vertragRepository;
        this.vertragSearchRepository = vertragSearchRepository;
    }

    /**
     * {@code POST  /vertrags} : Create a new vertrag.
     *
     * @param vertrag the vertrag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vertrag, or with status {@code 400 (Bad Request)} if the vertrag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vertrags")
    public ResponseEntity<Vertrag> createVertrag(@Valid @RequestBody Vertrag vertrag) throws URISyntaxException {
        log.debug("REST request to save Vertrag : {}", vertrag);
        if (vertrag.getId() != null) {
            throw new BadRequestAlertException("A new vertrag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vertrag result = vertragRepository.save(vertrag);
        vertragSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vertrags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vertrags} : Updates an existing vertrag.
     *
     * @param vertrag the vertrag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vertrag,
     * or with status {@code 400 (Bad Request)} if the vertrag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vertrag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vertrags")
    public ResponseEntity<Vertrag> updateVertrag(@Valid @RequestBody Vertrag vertrag) throws URISyntaxException {
        log.debug("REST request to update Vertrag : {}", vertrag);
        if (vertrag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vertrag result = vertragRepository.save(vertrag);
        vertragSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vertrag.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vertrags} : get all the vertrags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vertrags in body.
     */
    @GetMapping("/vertrags")
    public ResponseEntity<List<Vertrag>> getAllVertrags(Pageable pageable) {
        log.debug("REST request to get a page of Vertrags");
        Page<Vertrag> page = vertragRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vertrags/:id} : get the "id" vertrag.
     *
     * @param id the id of the vertrag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vertrag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vertrags/{id}")
    public ResponseEntity<Vertrag> getVertrag(@PathVariable Long id) {
        log.debug("REST request to get Vertrag : {}", id);
        Optional<Vertrag> vertrag = vertragRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vertrag);
    }

    /**
     * {@code DELETE  /vertrags/:id} : delete the "id" vertrag.
     *
     * @param id the id of the vertrag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vertrags/{id}")
    public ResponseEntity<Void> deleteVertrag(@PathVariable Long id) {
        log.debug("REST request to delete Vertrag : {}", id);
        vertragRepository.deleteById(id);
        vertragSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/vertrags?query=:query} : search for the vertrag corresponding
     * to the query.
     *
     * @param query the query of the vertrag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vertrags")
    public ResponseEntity<List<Vertrag>> searchVertrags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vertrags for query {}", query);
        Page<Vertrag> page = vertragSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
