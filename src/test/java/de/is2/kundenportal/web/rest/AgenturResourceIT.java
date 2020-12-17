package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Agentur;
import de.is2.kundenportal.repository.AgenturRepository;
import de.is2.kundenportal.repository.search.AgenturSearchRepository;

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

/**
 * Integration tests for the {@link AgenturResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AgenturResourceIT {

    private static final String DEFAULT_AGENTURNUMMER = "AAAAAAAAAA";
    private static final String UPDATED_AGENTURNUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONNR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONNR = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AgenturRepository agenturRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.AgenturSearchRepositoryMockConfiguration
     */
    @Autowired
    private AgenturSearchRepository mockAgenturSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgenturMockMvc;

    private Agentur agentur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agentur createEntity(EntityManager em) {
        Agentur agentur = new Agentur()
            .agenturnummer(DEFAULT_AGENTURNUMMER)
            .name(DEFAULT_NAME)
            .adresse(DEFAULT_ADRESSE)
            .email(DEFAULT_EMAIL)
            .telefonnr(DEFAULT_TELEFONNR)
            .created(DEFAULT_CREATED);
        return agentur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agentur createUpdatedEntity(EntityManager em) {
        Agentur agentur = new Agentur()
            .agenturnummer(UPDATED_AGENTURNUMMER)
            .name(UPDATED_NAME)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telefonnr(UPDATED_TELEFONNR)
            .created(UPDATED_CREATED);
        return agentur;
    }

    @BeforeEach
    public void initTest() {
        agentur = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentur() throws Exception {
        int databaseSizeBeforeCreate = agenturRepository.findAll().size();
        // Create the Agentur
        restAgenturMockMvc.perform(post("/api/agenturs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agentur)))
            .andExpect(status().isCreated());

        // Validate the Agentur in the database
        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeCreate + 1);
        Agentur testAgentur = agenturList.get(agenturList.size() - 1);
        assertThat(testAgentur.getAgenturnummer()).isEqualTo(DEFAULT_AGENTURNUMMER);
        assertThat(testAgentur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgentur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgentur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAgentur.getTelefonnr()).isEqualTo(DEFAULT_TELEFONNR);
        assertThat(testAgentur.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Agentur in Elasticsearch
        verify(mockAgenturSearchRepository, times(1)).save(testAgentur);
    }

    @Test
    @Transactional
    public void createAgenturWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agenturRepository.findAll().size();

        // Create the Agentur with an existing ID
        agentur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgenturMockMvc.perform(post("/api/agenturs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agentur)))
            .andExpect(status().isBadRequest());

        // Validate the Agentur in the database
        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeCreate);

        // Validate the Agentur in Elasticsearch
        verify(mockAgenturSearchRepository, times(0)).save(agentur);
    }


    @Test
    @Transactional
    public void checkAgenturnummerIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenturRepository.findAll().size();
        // set the field null
        agentur.setAgenturnummer(null);

        // Create the Agentur, which fails.


        restAgenturMockMvc.perform(post("/api/agenturs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agentur)))
            .andExpect(status().isBadRequest());

        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgenturs() throws Exception {
        // Initialize the database
        agenturRepository.saveAndFlush(agentur);

        // Get all the agenturList
        restAgenturMockMvc.perform(get("/api/agenturs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentur.getId().intValue())))
            .andExpect(jsonPath("$.[*].agenturnummer").value(hasItem(DEFAULT_AGENTURNUMMER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefonnr").value(hasItem(DEFAULT_TELEFONNR)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getAgentur() throws Exception {
        // Initialize the database
        agenturRepository.saveAndFlush(agentur);

        // Get the agentur
        restAgenturMockMvc.perform(get("/api/agenturs/{id}", agentur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agentur.getId().intValue()))
            .andExpect(jsonPath("$.agenturnummer").value(DEFAULT_AGENTURNUMMER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefonnr").value(DEFAULT_TELEFONNR))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAgentur() throws Exception {
        // Get the agentur
        restAgenturMockMvc.perform(get("/api/agenturs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentur() throws Exception {
        // Initialize the database
        agenturRepository.saveAndFlush(agentur);

        int databaseSizeBeforeUpdate = agenturRepository.findAll().size();

        // Update the agentur
        Agentur updatedAgentur = agenturRepository.findById(agentur.getId()).get();
        // Disconnect from session so that the updates on updatedAgentur are not directly saved in db
        em.detach(updatedAgentur);
        updatedAgentur
            .agenturnummer(UPDATED_AGENTURNUMMER)
            .name(UPDATED_NAME)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telefonnr(UPDATED_TELEFONNR)
            .created(UPDATED_CREATED);

        restAgenturMockMvc.perform(put("/api/agenturs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentur)))
            .andExpect(status().isOk());

        // Validate the Agentur in the database
        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeUpdate);
        Agentur testAgentur = agenturList.get(agenturList.size() - 1);
        assertThat(testAgentur.getAgenturnummer()).isEqualTo(UPDATED_AGENTURNUMMER);
        assertThat(testAgentur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgentur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgentur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgentur.getTelefonnr()).isEqualTo(UPDATED_TELEFONNR);
        assertThat(testAgentur.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Agentur in Elasticsearch
        verify(mockAgenturSearchRepository, times(1)).save(testAgentur);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentur() throws Exception {
        int databaseSizeBeforeUpdate = agenturRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenturMockMvc.perform(put("/api/agenturs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agentur)))
            .andExpect(status().isBadRequest());

        // Validate the Agentur in the database
        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Agentur in Elasticsearch
        verify(mockAgenturSearchRepository, times(0)).save(agentur);
    }

    @Test
    @Transactional
    public void deleteAgentur() throws Exception {
        // Initialize the database
        agenturRepository.saveAndFlush(agentur);

        int databaseSizeBeforeDelete = agenturRepository.findAll().size();

        // Delete the agentur
        restAgenturMockMvc.perform(delete("/api/agenturs/{id}", agentur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agentur> agenturList = agenturRepository.findAll();
        assertThat(agenturList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Agentur in Elasticsearch
        verify(mockAgenturSearchRepository, times(1)).deleteById(agentur.getId());
    }

    @Test
    @Transactional
    public void searchAgentur() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        agenturRepository.saveAndFlush(agentur);
        when(mockAgenturSearchRepository.search(queryStringQuery("id:" + agentur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(agentur), PageRequest.of(0, 1), 1));

        // Search the agentur
        restAgenturMockMvc.perform(get("/api/_search/agenturs?query=id:" + agentur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentur.getId().intValue())))
            .andExpect(jsonPath("$.[*].agenturnummer").value(hasItem(DEFAULT_AGENTURNUMMER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefonnr").value(hasItem(DEFAULT_TELEFONNR)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
