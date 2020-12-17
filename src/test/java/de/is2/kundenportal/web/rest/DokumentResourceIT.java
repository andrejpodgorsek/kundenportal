package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Dokument;
import de.is2.kundenportal.repository.DokumentRepository;
import de.is2.kundenportal.repository.search.DokumentSearchRepository;

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
import org.springframework.util.Base64Utils;
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

import de.is2.kundenportal.domain.enumeration.DokumentStatus;
/**
 * Integration tests for the {@link DokumentResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DokumentResourceIT {

    private static final String DEFAULT_NUMMER = "AAAAAAAAAA";
    private static final String UPDATED_NUMMER = "BBBBBBBBBB";

    private static final DokumentStatus DEFAULT_DOKUMENT = DokumentStatus.ANGEBOT;
    private static final DokumentStatus UPDATED_DOKUMENT = DokumentStatus.ANTRAG;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DokumentRepository dokumentRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.DokumentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DokumentSearchRepository mockDokumentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDokumentMockMvc;

    private Dokument dokument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dokument createEntity(EntityManager em) {
        Dokument dokument = new Dokument()
            .nummer(DEFAULT_NUMMER)
            .dokument(DEFAULT_DOKUMENT)
            .text(DEFAULT_TEXT)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE)
            .created(DEFAULT_CREATED);
        return dokument;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dokument createUpdatedEntity(EntityManager em) {
        Dokument dokument = new Dokument()
            .nummer(UPDATED_NUMMER)
            .dokument(UPDATED_DOKUMENT)
            .text(UPDATED_TEXT)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .created(UPDATED_CREATED);
        return dokument;
    }

    @BeforeEach
    public void initTest() {
        dokument = createEntity(em);
    }

    @Test
    @Transactional
    public void createDokument() throws Exception {
        int databaseSizeBeforeCreate = dokumentRepository.findAll().size();
        // Create the Dokument
        restDokumentMockMvc.perform(post("/api/dokuments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dokument)))
            .andExpect(status().isCreated());

        // Validate the Dokument in the database
        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeCreate + 1);
        Dokument testDokument = dokumentList.get(dokumentList.size() - 1);
        assertThat(testDokument.getNummer()).isEqualTo(DEFAULT_NUMMER);
        assertThat(testDokument.getDokument()).isEqualTo(DEFAULT_DOKUMENT);
        assertThat(testDokument.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDokument.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testDokument.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testDokument.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Dokument in Elasticsearch
        verify(mockDokumentSearchRepository, times(1)).save(testDokument);
    }

    @Test
    @Transactional
    public void createDokumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dokumentRepository.findAll().size();

        // Create the Dokument with an existing ID
        dokument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDokumentMockMvc.perform(post("/api/dokuments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dokument)))
            .andExpect(status().isBadRequest());

        // Validate the Dokument in the database
        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dokument in Elasticsearch
        verify(mockDokumentSearchRepository, times(0)).save(dokument);
    }


    @Test
    @Transactional
    public void checkNummerIsRequired() throws Exception {
        int databaseSizeBeforeTest = dokumentRepository.findAll().size();
        // set the field null
        dokument.setNummer(null);

        // Create the Dokument, which fails.


        restDokumentMockMvc.perform(post("/api/dokuments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dokument)))
            .andExpect(status().isBadRequest());

        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDokuments() throws Exception {
        // Initialize the database
        dokumentRepository.saveAndFlush(dokument);

        // Get all the dokumentList
        restDokumentMockMvc.perform(get("/api/dokuments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dokument.getId().intValue())))
            .andExpect(jsonPath("$.[*].nummer").value(hasItem(DEFAULT_NUMMER)))
            .andExpect(jsonPath("$.[*].dokument").value(hasItem(DEFAULT_DOKUMENT.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getDokument() throws Exception {
        // Initialize the database
        dokumentRepository.saveAndFlush(dokument);

        // Get the dokument
        restDokumentMockMvc.perform(get("/api/dokuments/{id}", dokument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dokument.getId().intValue()))
            .andExpect(jsonPath("$.nummer").value(DEFAULT_NUMMER))
            .andExpect(jsonPath("$.dokument").value(DEFAULT_DOKUMENT.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDokument() throws Exception {
        // Get the dokument
        restDokumentMockMvc.perform(get("/api/dokuments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDokument() throws Exception {
        // Initialize the database
        dokumentRepository.saveAndFlush(dokument);

        int databaseSizeBeforeUpdate = dokumentRepository.findAll().size();

        // Update the dokument
        Dokument updatedDokument = dokumentRepository.findById(dokument.getId()).get();
        // Disconnect from session so that the updates on updatedDokument are not directly saved in db
        em.detach(updatedDokument);
        updatedDokument
            .nummer(UPDATED_NUMMER)
            .dokument(UPDATED_DOKUMENT)
            .text(UPDATED_TEXT)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .created(UPDATED_CREATED);

        restDokumentMockMvc.perform(put("/api/dokuments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDokument)))
            .andExpect(status().isOk());

        // Validate the Dokument in the database
        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeUpdate);
        Dokument testDokument = dokumentList.get(dokumentList.size() - 1);
        assertThat(testDokument.getNummer()).isEqualTo(UPDATED_NUMMER);
        assertThat(testDokument.getDokument()).isEqualTo(UPDATED_DOKUMENT);
        assertThat(testDokument.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDokument.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testDokument.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testDokument.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Dokument in Elasticsearch
        verify(mockDokumentSearchRepository, times(1)).save(testDokument);
    }

    @Test
    @Transactional
    public void updateNonExistingDokument() throws Exception {
        int databaseSizeBeforeUpdate = dokumentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDokumentMockMvc.perform(put("/api/dokuments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dokument)))
            .andExpect(status().isBadRequest());

        // Validate the Dokument in the database
        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dokument in Elasticsearch
        verify(mockDokumentSearchRepository, times(0)).save(dokument);
    }

    @Test
    @Transactional
    public void deleteDokument() throws Exception {
        // Initialize the database
        dokumentRepository.saveAndFlush(dokument);

        int databaseSizeBeforeDelete = dokumentRepository.findAll().size();

        // Delete the dokument
        restDokumentMockMvc.perform(delete("/api/dokuments/{id}", dokument.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dokument> dokumentList = dokumentRepository.findAll();
        assertThat(dokumentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dokument in Elasticsearch
        verify(mockDokumentSearchRepository, times(1)).deleteById(dokument.getId());
    }

    @Test
    @Transactional
    public void searchDokument() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dokumentRepository.saveAndFlush(dokument);
        when(mockDokumentSearchRepository.search(queryStringQuery("id:" + dokument.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dokument), PageRequest.of(0, 1), 1));

        // Search the dokument
        restDokumentMockMvc.perform(get("/api/_search/dokuments?query=id:" + dokument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dokument.getId().intValue())))
            .andExpect(jsonPath("$.[*].nummer").value(hasItem(DEFAULT_NUMMER)))
            .andExpect(jsonPath("$.[*].dokument").value(hasItem(DEFAULT_DOKUMENT.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
