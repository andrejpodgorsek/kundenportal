package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.Schade;
import de.is2.kundenportal.repository.SchadeRepository;
import de.is2.kundenportal.repository.search.SchadeSearchRepository;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.Schade}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SchadeResource {

    private final Logger log = LoggerFactory.getLogger(SchadeResource.class);

    private static final String ENTITY_NAME = "schade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchadeRepository schadeRepository;

    private final SchadeSearchRepository schadeSearchRepository;

    public SchadeResource(SchadeRepository schadeRepository, SchadeSearchRepository schadeSearchRepository) {
        this.schadeRepository = schadeRepository;
        this.schadeSearchRepository = schadeSearchRepository;
    }

    /**
     * {@code POST  /schades} : Create a new schade.
     *
     * @param schade the schade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schade, or with status {@code 400 (Bad Request)} if the schade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schades")
    public ResponseEntity<Schade> createSchade(@Valid @RequestBody Schade schade) throws URISyntaxException {
        log.debug("REST request to save Schade : {}", schade);
        if (schade.getId() != null) {
            throw new BadRequestAlertException("A new schade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Schade result = schadeRepository.save(schade);
        schadeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/schades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schades} : Updates an existing schade.
     *
     * @param schade the schade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schade,
     * or with status {@code 400 (Bad Request)} if the schade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schades")
    public ResponseEntity<Schade> updateSchade(@Valid @RequestBody Schade schade) throws URISyntaxException {
        log.debug("REST request to update Schade : {}", schade);
        if (schade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Schade result = schadeRepository.save(schade);
        schadeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schade.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /schades} : get all the schades.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schades in body.
     */
    @GetMapping("/schades")
    public ResponseEntity<List<Schade>> getAllSchades(Pageable pageable) {
        log.debug("REST request to get a page of Schades");
        Page<Schade> page = schadeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schades/:id} : get the "id" schade.
     *
     * @param id the id of the schade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schades/{id}")
    public ResponseEntity<Schade> getSchade(@PathVariable Long id) {
        log.debug("REST request to get Schade : {}", id);
        Optional<Schade> schade = schadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(schade);
    }

    /**
     * {@code DELETE  /schades/:id} : delete the "id" schade.
     *
     * @param id the id of the schade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schades/{id}")
    public ResponseEntity<Void> deleteSchade(@PathVariable Long id) {
        log.debug("REST request to delete Schade : {}", id);
        schadeRepository.deleteById(id);
        schadeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/schades?query=:query} : search for the schade corresponding
     * to the query.
     *
     * @param query the query of the schade search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/schades")
    public ResponseEntity<List<Schade>> searchSchades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Schades for query {}", query);
        Page<Schade> page = schadeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
