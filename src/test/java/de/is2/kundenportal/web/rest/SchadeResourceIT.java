package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Schade;
import de.is2.kundenportal.repository.SchadeRepository;
import de.is2.kundenportal.repository.search.SchadeSearchRepository;

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

/**
 * Integration tests for the {@link SchadeResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SchadeResourceIT {

    private static final String DEFAULT_VSNR = "AAAAAAAAAA";
    private static final String UPDATED_VSNR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ANHANG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANHANG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANHANG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANHANG_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SchadeRepository schadeRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.SchadeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SchadeSearchRepository mockSchadeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchadeMockMvc;

    private Schade schade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schade createEntity(EntityManager em) {
        Schade schade = new Schade()
            .vsnr(DEFAULT_VSNR)
            .text(DEFAULT_TEXT)
            .anhang(DEFAULT_ANHANG)
            .anhangContentType(DEFAULT_ANHANG_CONTENT_TYPE)
            .created(DEFAULT_CREATED);
        return schade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schade createUpdatedEntity(EntityManager em) {
        Schade schade = new Schade()
            .vsnr(UPDATED_VSNR)
            .text(UPDATED_TEXT)
            .anhang(UPDATED_ANHANG)
            .anhangContentType(UPDATED_ANHANG_CONTENT_TYPE)
            .created(UPDATED_CREATED);
        return schade;
    }

    @BeforeEach
    public void initTest() {
        schade = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchade() throws Exception {
        int databaseSizeBeforeCreate = schadeRepository.findAll().size();
        // Create the Schade
        restSchadeMockMvc.perform(post("/api/schades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(schade)))
            .andExpect(status().isCreated());

        // Validate the Schade in the database
        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeCreate + 1);
        Schade testSchade = schadeList.get(schadeList.size() - 1);
        assertThat(testSchade.getVsnr()).isEqualTo(DEFAULT_VSNR);
        assertThat(testSchade.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSchade.getAnhang()).isEqualTo(DEFAULT_ANHANG);
        assertThat(testSchade.getAnhangContentType()).isEqualTo(DEFAULT_ANHANG_CONTENT_TYPE);
        assertThat(testSchade.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Schade in Elasticsearch
        verify(mockSchadeSearchRepository, times(1)).save(testSchade);
    }

    @Test
    @Transactional
    public void createSchadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schadeRepository.findAll().size();

        // Create the Schade with an existing ID
        schade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchadeMockMvc.perform(post("/api/schades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(schade)))
            .andExpect(status().isBadRequest());

        // Validate the Schade in the database
        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Schade in Elasticsearch
        verify(mockSchadeSearchRepository, times(0)).save(schade);
    }


    @Test
    @Transactional
    public void checkVsnrIsRequired() throws Exception {
        int databaseSizeBeforeTest = schadeRepository.findAll().size();
        // set the field null
        schade.setVsnr(null);

        // Create the Schade, which fails.


        restSchadeMockMvc.perform(post("/api/schades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(schade)))
            .andExpect(status().isBadRequest());

        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchades() throws Exception {
        // Initialize the database
        schadeRepository.saveAndFlush(schade);

        // Get all the schadeList
        restSchadeMockMvc.perform(get("/api/schades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schade.getId().intValue())))
            .andExpect(jsonPath("$.[*].vsnr").value(hasItem(DEFAULT_VSNR)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].anhangContentType").value(hasItem(DEFAULT_ANHANG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anhang").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANHANG))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getSchade() throws Exception {
        // Initialize the database
        schadeRepository.saveAndFlush(schade);

        // Get the schade
        restSchadeMockMvc.perform(get("/api/schades/{id}", schade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schade.getId().intValue()))
            .andExpect(jsonPath("$.vsnr").value(DEFAULT_VSNR))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.anhangContentType").value(DEFAULT_ANHANG_CONTENT_TYPE))
            .andExpect(jsonPath("$.anhang").value(Base64Utils.encodeToString(DEFAULT_ANHANG)))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSchade() throws Exception {
        // Get the schade
        restSchadeMockMvc.perform(get("/api/schades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchade() throws Exception {
        // Initialize the database
        schadeRepository.saveAndFlush(schade);

        int databaseSizeBeforeUpdate = schadeRepository.findAll().size();

        // Update the schade
        Schade updatedSchade = schadeRepository.findById(schade.getId()).get();
        // Disconnect from session so that the updates on updatedSchade are not directly saved in db
        em.detach(updatedSchade);
        updatedSchade
            .vsnr(UPDATED_VSNR)
            .text(UPDATED_TEXT)
            .anhang(UPDATED_ANHANG)
            .anhangContentType(UPDATED_ANHANG_CONTENT_TYPE)
            .created(UPDATED_CREATED);

        restSchadeMockMvc.perform(put("/api/schades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchade)))
            .andExpect(status().isOk());

        // Validate the Schade in the database
        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeUpdate);
        Schade testSchade = schadeList.get(schadeList.size() - 1);
        assertThat(testSchade.getVsnr()).isEqualTo(UPDATED_VSNR);
        assertThat(testSchade.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSchade.getAnhang()).isEqualTo(UPDATED_ANHANG);
        assertThat(testSchade.getAnhangContentType()).isEqualTo(UPDATED_ANHANG_CONTENT_TYPE);
        assertThat(testSchade.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Schade in Elasticsearch
        verify(mockSchadeSearchRepository, times(1)).save(testSchade);
    }

    @Test
    @Transactional
    public void updateNonExistingSchade() throws Exception {
        int databaseSizeBeforeUpdate = schadeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchadeMockMvc.perform(put("/api/schades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(schade)))
            .andExpect(status().isBadRequest());

        // Validate the Schade in the database
        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Schade in Elasticsearch
        verify(mockSchadeSearchRepository, times(0)).save(schade);
    }

    @Test
    @Transactional
    public void deleteSchade() throws Exception {
        // Initialize the database
        schadeRepository.saveAndFlush(schade);

        int databaseSizeBeforeDelete = schadeRepository.findAll().size();

        // Delete the schade
        restSchadeMockMvc.perform(delete("/api/schades/{id}", schade.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Schade> schadeList = schadeRepository.findAll();
        assertThat(schadeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Schade in Elasticsearch
        verify(mockSchadeSearchRepository, times(1)).deleteById(schade.getId());
    }

    @Test
    @Transactional
    public void searchSchade() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        schadeRepository.saveAndFlush(schade);
        when(mockSchadeSearchRepository.search(queryStringQuery("id:" + schade.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(schade), PageRequest.of(0, 1), 1));

        // Search the schade
        restSchadeMockMvc.perform(get("/api/_search/schades?query=id:" + schade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schade.getId().intValue())))
            .andExpect(jsonPath("$.[*].vsnr").value(hasItem(DEFAULT_VSNR)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].anhangContentType").value(hasItem(DEFAULT_ANHANG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anhang").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANHANG))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
