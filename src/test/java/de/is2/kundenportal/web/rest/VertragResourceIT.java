package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Vertrag;
import de.is2.kundenportal.repository.VertragRepository;
import de.is2.kundenportal.repository.search.VertragSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.is2.kundenportal.domain.enumeration.Sparte;
import de.is2.kundenportal.domain.enumeration.Rhytmus;
/**
 * Integration tests for the {@link VertragResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class VertragResourceIT {

    private static final String DEFAULT_VSNR = "AAAAAAAAAA";
    private static final String UPDATED_VSNR = "BBBBBBBBBB";

    private static final Sparte DEFAULT_SPARTE = Sparte.LV;
    private static final Sparte UPDATED_SPARTE = Sparte.KRANKEN;

    private static final Rhytmus DEFAULT_ZAHLENRHYTMUS = Rhytmus.MONATLICH;
    private static final Rhytmus UPDATED_ZAHLENRHYTMUS = Rhytmus.VIERTERJAERLICH;

    private static final Instant DEFAULT_ANTRAGSDATUM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANTRAGSDATUM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VERSICHERUNGSBEGINN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERSICHERUNGSBEGINN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VertragRepository vertragRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.VertragSearchRepositoryMockConfiguration
     */
    @Autowired
    private VertragSearchRepository mockVertragSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVertragMockMvc;

    private Vertrag vertrag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vertrag createEntity(EntityManager em) {
        Vertrag vertrag = new Vertrag()
            .vsnr(DEFAULT_VSNR)
            .sparte(DEFAULT_SPARTE)
            .zahlenrhytmus(DEFAULT_ZAHLENRHYTMUS)
            .antragsdatum(DEFAULT_ANTRAGSDATUM)
            .versicherungsbeginn(DEFAULT_VERSICHERUNGSBEGINN)
            .iban(DEFAULT_IBAN)
            .created(DEFAULT_CREATED);
        return vertrag;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vertrag createUpdatedEntity(EntityManager em) {
        Vertrag vertrag = new Vertrag()
            .vsnr(UPDATED_VSNR)
            .sparte(UPDATED_SPARTE)
            .zahlenrhytmus(UPDATED_ZAHLENRHYTMUS)
            .antragsdatum(UPDATED_ANTRAGSDATUM)
            .versicherungsbeginn(UPDATED_VERSICHERUNGSBEGINN)
            .iban(UPDATED_IBAN)
            .created(UPDATED_CREATED);
        return vertrag;
    }

    @BeforeEach
    public void initTest() {
        vertrag = createEntity(em);
    }

    @Test
    @Transactional
    public void createVertrag() throws Exception {
        int databaseSizeBeforeCreate = vertragRepository.findAll().size();
        // Create the Vertrag
        restVertragMockMvc.perform(post("/api/vertrags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vertrag)))
            .andExpect(status().isCreated());

        // Validate the Vertrag in the database
        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeCreate + 1);
        Vertrag testVertrag = vertragList.get(vertragList.size() - 1);
        assertThat(testVertrag.getVsnr()).isEqualTo(DEFAULT_VSNR);
        assertThat(testVertrag.getSparte()).isEqualTo(DEFAULT_SPARTE);
        assertThat(testVertrag.getZahlenrhytmus()).isEqualTo(DEFAULT_ZAHLENRHYTMUS);
        assertThat(testVertrag.getAntragsdatum()).isEqualTo(DEFAULT_ANTRAGSDATUM);
        assertThat(testVertrag.getVersicherungsbeginn()).isEqualTo(DEFAULT_VERSICHERUNGSBEGINN);
        assertThat(testVertrag.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testVertrag.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Vertrag in Elasticsearch
        verify(mockVertragSearchRepository, times(1)).save(testVertrag);
    }

    @Test
    @Transactional
    public void createVertragWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vertragRepository.findAll().size();

        // Create the Vertrag with an existing ID
        vertrag.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVertragMockMvc.perform(post("/api/vertrags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vertrag)))
            .andExpect(status().isBadRequest());

        // Validate the Vertrag in the database
        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vertrag in Elasticsearch
        verify(mockVertragSearchRepository, times(0)).save(vertrag);
    }


    @Test
    @Transactional
    public void checkVsnrIsRequired() throws Exception {
        int databaseSizeBeforeTest = vertragRepository.findAll().size();
        // set the field null
        vertrag.setVsnr(null);

        // Create the Vertrag, which fails.


        restVertragMockMvc.perform(post("/api/vertrags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vertrag)))
            .andExpect(status().isBadRequest());

        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVertrags() throws Exception {
        // Initialize the database
        vertragRepository.saveAndFlush(vertrag);

        // Get all the vertragList
        restVertragMockMvc.perform(get("/api/vertrags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vertrag.getId().intValue())))
            .andExpect(jsonPath("$.[*].vsnr").value(hasItem(DEFAULT_VSNR)))
            .andExpect(jsonPath("$.[*].sparte").value(hasItem(DEFAULT_SPARTE.toString())))
            .andExpect(jsonPath("$.[*].zahlenrhytmus").value(hasItem(DEFAULT_ZAHLENRHYTMUS.toString())))
            .andExpect(jsonPath("$.[*].antragsdatum").value(hasItem(DEFAULT_ANTRAGSDATUM.toString())))
            .andExpect(jsonPath("$.[*].versicherungsbeginn").value(hasItem(DEFAULT_VERSICHERUNGSBEGINN.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getVertrag() throws Exception {
        // Initialize the database
        vertragRepository.saveAndFlush(vertrag);

        // Get the vertrag
        restVertragMockMvc.perform(get("/api/vertrags/{id}", vertrag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vertrag.getId().intValue()))
            .andExpect(jsonPath("$.vsnr").value(DEFAULT_VSNR))
            .andExpect(jsonPath("$.sparte").value(DEFAULT_SPARTE.toString()))
            .andExpect(jsonPath("$.zahlenrhytmus").value(DEFAULT_ZAHLENRHYTMUS.toString()))
            .andExpect(jsonPath("$.antragsdatum").value(DEFAULT_ANTRAGSDATUM.toString()))
            .andExpect(jsonPath("$.versicherungsbeginn").value(DEFAULT_VERSICHERUNGSBEGINN.toString()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingVertrag() throws Exception {
        // Get the vertrag
        restVertragMockMvc.perform(get("/api/vertrags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVertrag() throws Exception {
        // Initialize the database
        vertragRepository.saveAndFlush(vertrag);

        int databaseSizeBeforeUpdate = vertragRepository.findAll().size();

        // Update the vertrag
        Vertrag updatedVertrag = vertragRepository.findById(vertrag.getId()).get();
        // Disconnect from session so that the updates on updatedVertrag are not directly saved in db
        em.detach(updatedVertrag);
        updatedVertrag
            .vsnr(UPDATED_VSNR)
            .sparte(UPDATED_SPARTE)
            .zahlenrhytmus(UPDATED_ZAHLENRHYTMUS)
            .antragsdatum(UPDATED_ANTRAGSDATUM)
            .versicherungsbeginn(UPDATED_VERSICHERUNGSBEGINN)
            .iban(UPDATED_IBAN)
            .created(UPDATED_CREATED);

        restVertragMockMvc.perform(put("/api/vertrags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVertrag)))
            .andExpect(status().isOk());

        // Validate the Vertrag in the database
        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeUpdate);
        Vertrag testVertrag = vertragList.get(vertragList.size() - 1);
        assertThat(testVertrag.getVsnr()).isEqualTo(UPDATED_VSNR);
        assertThat(testVertrag.getSparte()).isEqualTo(UPDATED_SPARTE);
        assertThat(testVertrag.getZahlenrhytmus()).isEqualTo(UPDATED_ZAHLENRHYTMUS);
        assertThat(testVertrag.getAntragsdatum()).isEqualTo(UPDATED_ANTRAGSDATUM);
        assertThat(testVertrag.getVersicherungsbeginn()).isEqualTo(UPDATED_VERSICHERUNGSBEGINN);
        assertThat(testVertrag.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testVertrag.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Vertrag in Elasticsearch
        verify(mockVertragSearchRepository, times(1)).save(testVertrag);
    }

    @Test
    @Transactional
    public void updateNonExistingVertrag() throws Exception {
        int databaseSizeBeforeUpdate = vertragRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVertragMockMvc.perform(put("/api/vertrags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vertrag)))
            .andExpect(status().isBadRequest());

        // Validate the Vertrag in the database
        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vertrag in Elasticsearch
        verify(mockVertragSearchRepository, times(0)).save(vertrag);
    }

    @Test
    @Transactional
    public void deleteVertrag() throws Exception {
        // Initialize the database
        vertragRepository.saveAndFlush(vertrag);

        int databaseSizeBeforeDelete = vertragRepository.findAll().size();

        // Delete the vertrag
        restVertragMockMvc.perform(delete("/api/vertrags/{id}", vertrag.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vertrag> vertragList = vertragRepository.findAll();
        assertThat(vertragList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vertrag in Elasticsearch
        verify(mockVertragSearchRepository, times(1)).deleteById(vertrag.getId());
    }

    @Test
    @Transactional
    public void searchVertrag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        vertragRepository.saveAndFlush(vertrag);
        when(mockVertragSearchRepository.search(queryStringQuery("id:" + vertrag.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vertrag), PageRequest.of(0, 1), 1));

        // Search the vertrag
        restVertragMockMvc.perform(get("/api/_search/vertrags?query=id:" + vertrag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vertrag.getId().intValue())))
            .andExpect(jsonPath("$.[*].vsnr").value(hasItem(DEFAULT_VSNR)))
            .andExpect(jsonPath("$.[*].sparte").value(hasItem(DEFAULT_SPARTE.toString())))
            .andExpect(jsonPath("$.[*].zahlenrhytmus").value(hasItem(DEFAULT_ZAHLENRHYTMUS.toString())))
            .andExpect(jsonPath("$.[*].antragsdatum").value(hasItem(DEFAULT_ANTRAGSDATUM.toString())))
            .andExpect(jsonPath("$.[*].versicherungsbeginn").value(hasItem(DEFAULT_VERSICHERUNGSBEGINN.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
