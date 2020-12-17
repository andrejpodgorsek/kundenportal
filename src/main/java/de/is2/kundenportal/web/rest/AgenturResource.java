package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.Agentur;
import de.is2.kundenportal.repository.AgenturRepository;
import de.is2.kundenportal.repository.search.AgenturSearchRepository;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.Agentur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AgenturResource {

    private final Logger log = LoggerFactory.getLogger(AgenturResource.class);

    private static final String ENTITY_NAME = "agentur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgenturRepository agenturRepository;

    private final AgenturSearchRepository agenturSearchRepository;

    public AgenturResource(AgenturRepository agenturRepository, AgenturSearchRepository agenturSearchRepository) {
        this.agenturRepository = agenturRepository;
        this.agenturSearchRepository = agenturSearchRepository;
    }

    /**
     * {@code POST  /agenturs} : Create a new agentur.
     *
     * @param agentur the agentur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentur, or with status {@code 400 (Bad Request)} if the agentur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenturs")
    public ResponseEntity<Agentur> createAgentur(@Valid @RequestBody Agentur agentur) throws URISyntaxException {
        log.debug("REST request to save Agentur : {}", agentur);
        if (agentur.getId() != null) {
            throw new BadRequestAlertException("A new agentur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agentur result = agenturRepository.save(agentur);
        agenturSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/agenturs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenturs} : Updates an existing agentur.
     *
     * @param agentur the agentur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentur,
     * or with status {@code 400 (Bad Request)} if the agentur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenturs")
    public ResponseEntity<Agentur> updateAgentur(@Valid @RequestBody Agentur agentur) throws URISyntaxException {
        log.debug("REST request to update Agentur : {}", agentur);
        if (agentur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Agentur result = agenturRepository.save(agentur);
        agenturSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentur.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agenturs} : get all the agenturs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agenturs in body.
     */
    @GetMapping("/agenturs")
    public ResponseEntity<List<Agentur>> getAllAgenturs(Pageable pageable) {
        log.debug("REST request to get a page of Agenturs");
        Page<Agentur> page = agenturRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agenturs/:id} : get the "id" agentur.
     *
     * @param id the id of the agentur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenturs/{id}")
    public ResponseEntity<Agentur> getAgentur(@PathVariable Long id) {
        log.debug("REST request to get Agentur : {}", id);
        Optional<Agentur> agentur = agenturRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agentur);
    }

    /**
     * {@code DELETE  /agenturs/:id} : delete the "id" agentur.
     *
     * @param id the id of the agentur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenturs/{id}")
    public ResponseEntity<Void> deleteAgentur(@PathVariable Long id) {
        log.debug("REST request to delete Agentur : {}", id);
        agenturRepository.deleteById(id);
        agenturSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/agenturs?query=:query} : search for the agentur corresponding
     * to the query.
     *
     * @param query the query of the agentur search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/agenturs")
    public ResponseEntity<List<Agentur>> searchAgenturs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Agenturs for query {}", query);
        Page<Agentur> page = agenturSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
