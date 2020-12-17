package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.Kunde;
import de.is2.kundenportal.repository.KundeRepository;
import de.is2.kundenportal.repository.search.KundeSearchRepository;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.Kunde}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KundeResource {

    private final Logger log = LoggerFactory.getLogger(KundeResource.class);

    private static final String ENTITY_NAME = "kunde";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KundeRepository kundeRepository;

    private final KundeSearchRepository kundeSearchRepository;

    public KundeResource(KundeRepository kundeRepository, KundeSearchRepository kundeSearchRepository) {
        this.kundeRepository = kundeRepository;
        this.kundeSearchRepository = kundeSearchRepository;
    }

    /**
     * {@code POST  /kundes} : Create a new kunde.
     *
     * @param kunde the kunde to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kunde, or with status {@code 400 (Bad Request)} if the kunde has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kundes")
    public ResponseEntity<Kunde> createKunde(@Valid @RequestBody Kunde kunde) throws URISyntaxException {
        log.debug("REST request to save Kunde : {}", kunde);
        if (kunde.getId() != null) {
            throw new BadRequestAlertException("A new kunde cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kunde result = kundeRepository.save(kunde);
        kundeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/kundes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kundes} : Updates an existing kunde.
     *
     * @param kunde the kunde to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kunde,
     * or with status {@code 400 (Bad Request)} if the kunde is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kunde couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kundes")
    public ResponseEntity<Kunde> updateKunde(@Valid @RequestBody Kunde kunde) throws URISyntaxException {
        log.debug("REST request to update Kunde : {}", kunde);
        if (kunde.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Kunde result = kundeRepository.save(kunde);
        kundeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kunde.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kundes} : get all the kundes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kundes in body.
     */
    @GetMapping("/kundes")
    public ResponseEntity<List<Kunde>> getAllKundes(Pageable pageable) {
        log.debug("REST request to get a page of Kundes");
        Page<Kunde> page = kundeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kundes/:id} : get the "id" kunde.
     *
     * @param id the id of the kunde to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kunde, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kundes/{id}")
    public ResponseEntity<Kunde> getKunde(@PathVariable Long id) {
        log.debug("REST request to get Kunde : {}", id);
        Optional<Kunde> kunde = kundeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kunde);
    }

    /**
     * {@code DELETE  /kundes/:id} : delete the "id" kunde.
     *
     * @param id the id of the kunde to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kundes/{id}")
    public ResponseEntity<Void> deleteKunde(@PathVariable Long id) {
        log.debug("REST request to delete Kunde : {}", id);
        kundeRepository.deleteById(id);
        kundeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/kundes?query=:query} : search for the kunde corresponding
     * to the query.
     *
     * @param query the query of the kunde search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/kundes")
    public ResponseEntity<List<Kunde>> searchKundes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Kundes for query {}", query);
        Page<Kunde> page = kundeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
