package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Nachricht;
import de.is2.kundenportal.repository.NachrichtRepository;
import de.is2.kundenportal.repository.search.NachrichtSearchRepository;

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

import de.is2.kundenportal.domain.enumeration.NachrichtStatus;
/**
 * Integration tests for the {@link NachrichtResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class NachrichtResourceIT {

    private static final String DEFAULT_AN = "AAAAAAAAAA";
    private static final String UPDATED_AN = "BBBBBBBBBB";

    private static final String DEFAULT_BETREFF = "AAAAAAAAAA";
    private static final String UPDATED_BETREFF = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ANHANG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANHANG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANHANG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANHANG_CONTENT_TYPE = "image/png";

    private static final NachrichtStatus DEFAULT_STATUS = NachrichtStatus.GELESEN;
    private static final NachrichtStatus UPDATED_STATUS = NachrichtStatus.UNGELESEN;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private NachrichtRepository nachrichtRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.NachrichtSearchRepositoryMockConfiguration
     */
    @Autowired
    private NachrichtSearchRepository mockNachrichtSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNachrichtMockMvc;

    private Nachricht nachricht;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nachricht createEntity(EntityManager em) {
        Nachricht nachricht = new Nachricht()
            .an(DEFAULT_AN)
            .betreff(DEFAULT_BETREFF)
            .text(DEFAULT_TEXT)
            .anhang(DEFAULT_ANHANG)
            .anhangContentType(DEFAULT_ANHANG_CONTENT_TYPE)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED);
        return nachricht;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nachricht createUpdatedEntity(EntityManager em) {
        Nachricht nachricht = new Nachricht()
            .an(UPDATED_AN)
            .betreff(UPDATED_BETREFF)
            .text(UPDATED_TEXT)
            .anhang(UPDATED_ANHANG)
            .anhangContentType(UPDATED_ANHANG_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);
        return nachricht;
    }

    @BeforeEach
    public void initTest() {
        nachricht = createEntity(em);
    }

    @Test
    @Transactional
    public void createNachricht() throws Exception {
        int databaseSizeBeforeCreate = nachrichtRepository.findAll().size();
        // Create the Nachricht
        restNachrichtMockMvc.perform(post("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isCreated());

        // Validate the Nachricht in the database
        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeCreate + 1);
        Nachricht testNachricht = nachrichtList.get(nachrichtList.size() - 1);
        assertThat(testNachricht.getAn()).isEqualTo(DEFAULT_AN);
        assertThat(testNachricht.getBetreff()).isEqualTo(DEFAULT_BETREFF);
        assertThat(testNachricht.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testNachricht.getAnhang()).isEqualTo(DEFAULT_ANHANG);
        assertThat(testNachricht.getAnhangContentType()).isEqualTo(DEFAULT_ANHANG_CONTENT_TYPE);
        assertThat(testNachricht.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNachricht.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Nachricht in Elasticsearch
        verify(mockNachrichtSearchRepository, times(1)).save(testNachricht);
    }

    @Test
    @Transactional
    public void createNachrichtWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nachrichtRepository.findAll().size();

        // Create the Nachricht with an existing ID
        nachricht.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNachrichtMockMvc.perform(post("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isBadRequest());

        // Validate the Nachricht in the database
        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeCreate);

        // Validate the Nachricht in Elasticsearch
        verify(mockNachrichtSearchRepository, times(0)).save(nachricht);
    }


    @Test
    @Transactional
    public void checkAnIsRequired() throws Exception {
        int databaseSizeBeforeTest = nachrichtRepository.findAll().size();
        // set the field null
        nachricht.setAn(null);

        // Create the Nachricht, which fails.


        restNachrichtMockMvc.perform(post("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isBadRequest());

        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBetreffIsRequired() throws Exception {
        int databaseSizeBeforeTest = nachrichtRepository.findAll().size();
        // set the field null
        nachricht.setBetreff(null);

        // Create the Nachricht, which fails.


        restNachrichtMockMvc.perform(post("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isBadRequest());

        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = nachrichtRepository.findAll().size();
        // set the field null
        nachricht.setText(null);

        // Create the Nachricht, which fails.


        restNachrichtMockMvc.perform(post("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isBadRequest());

        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNachrichts() throws Exception {
        // Initialize the database
        nachrichtRepository.saveAndFlush(nachricht);

        // Get all the nachrichtList
        restNachrichtMockMvc.perform(get("/api/nachrichts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nachricht.getId().intValue())))
            .andExpect(jsonPath("$.[*].an").value(hasItem(DEFAULT_AN)))
            .andExpect(jsonPath("$.[*].betreff").value(hasItem(DEFAULT_BETREFF)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].anhangContentType").value(hasItem(DEFAULT_ANHANG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anhang").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANHANG))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getNachricht() throws Exception {
        // Initialize the database
        nachrichtRepository.saveAndFlush(nachricht);

        // Get the nachricht
        restNachrichtMockMvc.perform(get("/api/nachrichts/{id}", nachricht.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nachricht.getId().intValue()))
            .andExpect(jsonPath("$.an").value(DEFAULT_AN))
            .andExpect(jsonPath("$.betreff").value(DEFAULT_BETREFF))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.anhangContentType").value(DEFAULT_ANHANG_CONTENT_TYPE))
            .andExpect(jsonPath("$.anhang").value(Base64Utils.encodeToString(DEFAULT_ANHANG)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingNachricht() throws Exception {
        // Get the nachricht
        restNachrichtMockMvc.perform(get("/api/nachrichts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNachricht() throws Exception {
        // Initialize the database
        nachrichtRepository.saveAndFlush(nachricht);

        int databaseSizeBeforeUpdate = nachrichtRepository.findAll().size();

        // Update the nachricht
        Nachricht updatedNachricht = nachrichtRepository.findById(nachricht.getId()).get();
        // Disconnect from session so that the updates on updatedNachricht are not directly saved in db
        em.detach(updatedNachricht);
        updatedNachricht
            .an(UPDATED_AN)
            .betreff(UPDATED_BETREFF)
            .text(UPDATED_TEXT)
            .anhang(UPDATED_ANHANG)
            .anhangContentType(UPDATED_ANHANG_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restNachrichtMockMvc.perform(put("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNachricht)))
            .andExpect(status().isOk());

        // Validate the Nachricht in the database
        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeUpdate);
        Nachricht testNachricht = nachrichtList.get(nachrichtList.size() - 1);
        assertThat(testNachricht.getAn()).isEqualTo(UPDATED_AN);
        assertThat(testNachricht.getBetreff()).isEqualTo(UPDATED_BETREFF);
        assertThat(testNachricht.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testNachricht.getAnhang()).isEqualTo(UPDATED_ANHANG);
        assertThat(testNachricht.getAnhangContentType()).isEqualTo(UPDATED_ANHANG_CONTENT_TYPE);
        assertThat(testNachricht.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNachricht.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Nachricht in Elasticsearch
        verify(mockNachrichtSearchRepository, times(1)).save(testNachricht);
    }

    @Test
    @Transactional
    public void updateNonExistingNachricht() throws Exception {
        int databaseSizeBeforeUpdate = nachrichtRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNachrichtMockMvc.perform(put("/api/nachrichts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nachricht)))
            .andExpect(status().isBadRequest());

        // Validate the Nachricht in the database
        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nachricht in Elasticsearch
        verify(mockNachrichtSearchRepository, times(0)).save(nachricht);
    }

    @Test
    @Transactional
    public void deleteNachricht() throws Exception {
        // Initialize the database
        nachrichtRepository.saveAndFlush(nachricht);

        int databaseSizeBeforeDelete = nachrichtRepository.findAll().size();

        // Delete the nachricht
        restNachrichtMockMvc.perform(delete("/api/nachrichts/{id}", nachricht.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nachricht> nachrichtList = nachrichtRepository.findAll();
        assertThat(nachrichtList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Nachricht in Elasticsearch
        verify(mockNachrichtSearchRepository, times(1)).deleteById(nachricht.getId());
    }

    @Test
    @Transactional
    public void searchNachricht() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nachrichtRepository.saveAndFlush(nachricht);
        when(mockNachrichtSearchRepository.search(queryStringQuery("id:" + nachricht.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nachricht), PageRequest.of(0, 1), 1));

        // Search the nachricht
        restNachrichtMockMvc.perform(get("/api/_search/nachrichts?query=id:" + nachricht.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nachricht.getId().intValue())))
            .andExpect(jsonPath("$.[*].an").value(hasItem(DEFAULT_AN)))
            .andExpect(jsonPath("$.[*].betreff").value(hasItem(DEFAULT_BETREFF)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].anhangContentType").value(hasItem(DEFAULT_ANHANG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anhang").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANHANG))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
