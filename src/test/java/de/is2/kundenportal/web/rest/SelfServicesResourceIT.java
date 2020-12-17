package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.SelfServices;
import de.is2.kundenportal.repository.SelfServicesRepository;
import de.is2.kundenportal.repository.search.SelfServicesSearchRepository;

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

import de.is2.kundenportal.domain.enumeration.ServicesTyp;
import de.is2.kundenportal.domain.enumeration.ServicesStatus;
/**
 * Integration tests for the {@link SelfServicesResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SelfServicesResourceIT {

    private static final ServicesTyp DEFAULT_SERVICE_TYP = ServicesTyp.ADRESSDATEN_AENDERN;
    private static final ServicesTyp UPDATED_SERVICE_TYP = ServicesTyp.BANKVERBINDUNG_AENDERN;

    private static final ServicesStatus DEFAULT_STATUS = ServicesStatus.GEPLANT;
    private static final ServicesStatus UPDATED_STATUS = ServicesStatus.EINGEREICHT;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATEI = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATEI = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATEI_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATEI_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SelfServicesRepository selfServicesRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.SelfServicesSearchRepositoryMockConfiguration
     */
    @Autowired
    private SelfServicesSearchRepository mockSelfServicesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSelfServicesMockMvc;

    private SelfServices selfServices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelfServices createEntity(EntityManager em) {
        SelfServices selfServices = new SelfServices()
            .serviceTyp(DEFAULT_SERVICE_TYP)
            .status(DEFAULT_STATUS)
            .text(DEFAULT_TEXT)
            .datei(DEFAULT_DATEI)
            .dateiContentType(DEFAULT_DATEI_CONTENT_TYPE)
            .created(DEFAULT_CREATED);
        return selfServices;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelfServices createUpdatedEntity(EntityManager em) {
        SelfServices selfServices = new SelfServices()
            .serviceTyp(UPDATED_SERVICE_TYP)
            .status(UPDATED_STATUS)
            .text(UPDATED_TEXT)
            .datei(UPDATED_DATEI)
            .dateiContentType(UPDATED_DATEI_CONTENT_TYPE)
            .created(UPDATED_CREATED);
        return selfServices;
    }

    @BeforeEach
    public void initTest() {
        selfServices = createEntity(em);
    }

    @Test
    @Transactional
    public void createSelfServices() throws Exception {
        int databaseSizeBeforeCreate = selfServicesRepository.findAll().size();
        // Create the SelfServices
        restSelfServicesMockMvc.perform(post("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(selfServices)))
            .andExpect(status().isCreated());

        // Validate the SelfServices in the database
        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeCreate + 1);
        SelfServices testSelfServices = selfServicesList.get(selfServicesList.size() - 1);
        assertThat(testSelfServices.getServiceTyp()).isEqualTo(DEFAULT_SERVICE_TYP);
        assertThat(testSelfServices.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSelfServices.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSelfServices.getDatei()).isEqualTo(DEFAULT_DATEI);
        assertThat(testSelfServices.getDateiContentType()).isEqualTo(DEFAULT_DATEI_CONTENT_TYPE);
        assertThat(testSelfServices.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the SelfServices in Elasticsearch
        verify(mockSelfServicesSearchRepository, times(1)).save(testSelfServices);
    }

    @Test
    @Transactional
    public void createSelfServicesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = selfServicesRepository.findAll().size();

        // Create the SelfServices with an existing ID
        selfServices.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelfServicesMockMvc.perform(post("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(selfServices)))
            .andExpect(status().isBadRequest());

        // Validate the SelfServices in the database
        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeCreate);

        // Validate the SelfServices in Elasticsearch
        verify(mockSelfServicesSearchRepository, times(0)).save(selfServices);
    }


    @Test
    @Transactional
    public void checkServiceTypIsRequired() throws Exception {
        int databaseSizeBeforeTest = selfServicesRepository.findAll().size();
        // set the field null
        selfServices.setServiceTyp(null);

        // Create the SelfServices, which fails.


        restSelfServicesMockMvc.perform(post("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(selfServices)))
            .andExpect(status().isBadRequest());

        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = selfServicesRepository.findAll().size();
        // set the field null
        selfServices.setStatus(null);

        // Create the SelfServices, which fails.


        restSelfServicesMockMvc.perform(post("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(selfServices)))
            .andExpect(status().isBadRequest());

        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSelfServices() throws Exception {
        // Initialize the database
        selfServicesRepository.saveAndFlush(selfServices);

        // Get all the selfServicesList
        restSelfServicesMockMvc.perform(get("/api/self-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selfServices.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceTyp").value(hasItem(DEFAULT_SERVICE_TYP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].dateiContentType").value(hasItem(DEFAULT_DATEI_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].datei").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATEI))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getSelfServices() throws Exception {
        // Initialize the database
        selfServicesRepository.saveAndFlush(selfServices);

        // Get the selfServices
        restSelfServicesMockMvc.perform(get("/api/self-services/{id}", selfServices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(selfServices.getId().intValue()))
            .andExpect(jsonPath("$.serviceTyp").value(DEFAULT_SERVICE_TYP.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.dateiContentType").value(DEFAULT_DATEI_CONTENT_TYPE))
            .andExpect(jsonPath("$.datei").value(Base64Utils.encodeToString(DEFAULT_DATEI)))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSelfServices() throws Exception {
        // Get the selfServices
        restSelfServicesMockMvc.perform(get("/api/self-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSelfServices() throws Exception {
        // Initialize the database
        selfServicesRepository.saveAndFlush(selfServices);

        int databaseSizeBeforeUpdate = selfServicesRepository.findAll().size();

        // Update the selfServices
        SelfServices updatedSelfServices = selfServicesRepository.findById(selfServices.getId()).get();
        // Disconnect from session so that the updates on updatedSelfServices are not directly saved in db
        em.detach(updatedSelfServices);
        updatedSelfServices
            .serviceTyp(UPDATED_SERVICE_TYP)
            .status(UPDATED_STATUS)
            .text(UPDATED_TEXT)
            .datei(UPDATED_DATEI)
            .dateiContentType(UPDATED_DATEI_CONTENT_TYPE)
            .created(UPDATED_CREATED);

        restSelfServicesMockMvc.perform(put("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSelfServices)))
            .andExpect(status().isOk());

        // Validate the SelfServices in the database
        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeUpdate);
        SelfServices testSelfServices = selfServicesList.get(selfServicesList.size() - 1);
        assertThat(testSelfServices.getServiceTyp()).isEqualTo(UPDATED_SERVICE_TYP);
        assertThat(testSelfServices.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSelfServices.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSelfServices.getDatei()).isEqualTo(UPDATED_DATEI);
        assertThat(testSelfServices.getDateiContentType()).isEqualTo(UPDATED_DATEI_CONTENT_TYPE);
        assertThat(testSelfServices.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the SelfServices in Elasticsearch
        verify(mockSelfServicesSearchRepository, times(1)).save(testSelfServices);
    }

    @Test
    @Transactional
    public void updateNonExistingSelfServices() throws Exception {
        int databaseSizeBeforeUpdate = selfServicesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelfServicesMockMvc.perform(put("/api/self-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(selfServices)))
            .andExpect(status().isBadRequest());

        // Validate the SelfServices in the database
        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SelfServices in Elasticsearch
        verify(mockSelfServicesSearchRepository, times(0)).save(selfServices);
    }

    @Test
    @Transactional
    public void deleteSelfServices() throws Exception {
        // Initialize the database
        selfServicesRepository.saveAndFlush(selfServices);

        int databaseSizeBeforeDelete = selfServicesRepository.findAll().size();

        // Delete the selfServices
        restSelfServicesMockMvc.perform(delete("/api/self-services/{id}", selfServices.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SelfServices> selfServicesList = selfServicesRepository.findAll();
        assertThat(selfServicesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SelfServices in Elasticsearch
        verify(mockSelfServicesSearchRepository, times(1)).deleteById(selfServices.getId());
    }

    @Test
    @Transactional
    public void searchSelfServices() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        selfServicesRepository.saveAndFlush(selfServices);
        when(mockSelfServicesSearchRepository.search(queryStringQuery("id:" + selfServices.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(selfServices), PageRequest.of(0, 1), 1));

        // Search the selfServices
        restSelfServicesMockMvc.perform(get("/api/_search/self-services?query=id:" + selfServices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selfServices.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceTyp").value(hasItem(DEFAULT_SERVICE_TYP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].dateiContentType").value(hasItem(DEFAULT_DATEI_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].datei").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATEI))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
