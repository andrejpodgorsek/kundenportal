package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.domain.SelfServices;
import de.is2.kundenportal.repository.SelfServicesRepository;
import de.is2.kundenportal.repository.search.SelfServicesSearchRepository;
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
 * REST controller for managing {@link de.is2.kundenportal.domain.SelfServices}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SelfServicesResource {

    private final Logger log = LoggerFactory.getLogger(SelfServicesResource.class);

    private static final String ENTITY_NAME = "selfServices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SelfServicesRepository selfServicesRepository;

    private final SelfServicesSearchRepository selfServicesSearchRepository;

    public SelfServicesResource(SelfServicesRepository selfServicesRepository, SelfServicesSearchRepository selfServicesSearchRepository) {
        this.selfServicesRepository = selfServicesRepository;
        this.selfServicesSearchRepository = selfServicesSearchRepository;
    }

    /**
     * {@code POST  /self-services} : Create a new selfServices.
     *
     * @param selfServices the selfServices to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new selfServices, or with status {@code 400 (Bad Request)} if the selfServices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/self-services")
    public ResponseEntity<SelfServices> createSelfServices(@Valid @RequestBody SelfServices selfServices) throws URISyntaxException {
        log.debug("REST request to save SelfServices : {}", selfServices);
        if (selfServices.getId() != null) {
            throw new BadRequestAlertException("A new selfServices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SelfServices result = selfServicesRepository.save(selfServices);
        selfServicesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/self-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /self-services} : Updates an existing selfServices.
     *
     * @param selfServices the selfServices to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selfServices,
     * or with status {@code 400 (Bad Request)} if the selfServices is not valid,
     * or with status {@code 500 (Internal Server Error)} if the selfServices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/self-services")
    public ResponseEntity<SelfServices> updateSelfServices(@Valid @RequestBody SelfServices selfServices) throws URISyntaxException {
        log.debug("REST request to update SelfServices : {}", selfServices);
        if (selfServices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SelfServices result = selfServicesRepository.save(selfServices);
        selfServicesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, selfServices.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /self-services} : get all the selfServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of selfServices in body.
     */
    @GetMapping("/self-services")
    public ResponseEntity<List<SelfServices>> getAllSelfServices(Pageable pageable) {
        log.debug("REST request to get a page of SelfServices");
        Page<SelfServices> page = selfServicesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /self-services/:id} : get the "id" selfServices.
     *
     * @param id the id of the selfServices to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the selfServices, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/self-services/{id}")
    public ResponseEntity<SelfServices> getSelfServices(@PathVariable Long id) {
        log.debug("REST request to get SelfServices : {}", id);
        Optional<SelfServices> selfServices = selfServicesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(selfServices);
    }

    /**
     * {@code DELETE  /self-services/:id} : delete the "id" selfServices.
     *
     * @param id the id of the selfServices to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/self-services/{id}")
    public ResponseEntity<Void> deleteSelfServices(@PathVariable Long id) {
        log.debug("REST request to delete SelfServices : {}", id);
        selfServicesRepository.deleteById(id);
        selfServicesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/self-services?query=:query} : search for the selfServices corresponding
     * to the query.
     *
     * @param query the query of the selfServices search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/self-services")
    public ResponseEntity<List<SelfServices>> searchSelfServices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SelfServices for query {}", query);
        Page<SelfServices> page = selfServicesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
