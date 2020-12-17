package de.is2.kundenportal.web.rest;

import de.is2.kundenportal.KundenportalApp;
import de.is2.kundenportal.domain.Kunde;
import de.is2.kundenportal.repository.KundeRepository;
import de.is2.kundenportal.repository.search.KundeSearchRepository;

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

import de.is2.kundenportal.domain.enumeration.Anrede;
/**
 * Integration tests for the {@link KundeResource} REST controller.
 */
@SpringBootTest(classes = KundenportalApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class KundeResourceIT {

    private static final Anrede DEFAULT_ANREDE = Anrede.HERR;
    private static final Anrede UPDATED_ANREDE = Anrede.FRAU;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VORNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_STRASSE = "AAAAAAAAAA";
    private static final String UPDATED_STRASSE = "BBBBBBBBBB";

    private static final String DEFAULT_PLZORT = "AAAAAAAAAA";
    private static final String UPDATED_PLZORT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONNR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONNR = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private KundeRepository kundeRepository;

    /**
     * This repository is mocked in the de.is2.kundenportal.repository.search test package.
     *
     * @see de.is2.kundenportal.repository.search.KundeSearchRepositoryMockConfiguration
     */
    @Autowired
    private KundeSearchRepository mockKundeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKundeMockMvc;

    private Kunde kunde;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kunde createEntity(EntityManager em) {
        Kunde kunde = new Kunde()
            .anrede(DEFAULT_ANREDE)
            .name(DEFAULT_NAME)
            .vorname(DEFAULT_VORNAME)
            .email(DEFAULT_EMAIL)
            .strasse(DEFAULT_STRASSE)
            .plzort(DEFAULT_PLZORT)
            .telefonnr(DEFAULT_TELEFONNR)
            .iban(DEFAULT_IBAN)
            .created(DEFAULT_CREATED);
        return kunde;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kunde createUpdatedEntity(EntityManager em) {
        Kunde kunde = new Kunde()
            .anrede(UPDATED_ANREDE)
            .name(UPDATED_NAME)
            .vorname(UPDATED_VORNAME)
            .email(UPDATED_EMAIL)
            .strasse(UPDATED_STRASSE)
            .plzort(UPDATED_PLZORT)
            .telefonnr(UPDATED_TELEFONNR)
            .iban(UPDATED_IBAN)
            .created(UPDATED_CREATED);
        return kunde;
    }

    @BeforeEach
    public void initTest() {
        kunde = createEntity(em);
    }

    @Test
    @Transactional
    public void createKunde() throws Exception {
        int databaseSizeBeforeCreate = kundeRepository.findAll().size();
        // Create the Kunde
        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isCreated());

        // Validate the Kunde in the database
        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeCreate + 1);
        Kunde testKunde = kundeList.get(kundeList.size() - 1);
        assertThat(testKunde.getAnrede()).isEqualTo(DEFAULT_ANREDE);
        assertThat(testKunde.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKunde.getVorname()).isEqualTo(DEFAULT_VORNAME);
        assertThat(testKunde.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testKunde.getStrasse()).isEqualTo(DEFAULT_STRASSE);
        assertThat(testKunde.getPlzort()).isEqualTo(DEFAULT_PLZORT);
        assertThat(testKunde.getTelefonnr()).isEqualTo(DEFAULT_TELEFONNR);
        assertThat(testKunde.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testKunde.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Kunde in Elasticsearch
        verify(mockKundeSearchRepository, times(1)).save(testKunde);
    }

    @Test
    @Transactional
    public void createKundeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kundeRepository.findAll().size();

        // Create the Kunde with an existing ID
        kunde.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        // Validate the Kunde in the database
        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Kunde in Elasticsearch
        verify(mockKundeSearchRepository, times(0)).save(kunde);
    }


    @Test
    @Transactional
    public void checkAnredeIsRequired() throws Exception {
        int databaseSizeBeforeTest = kundeRepository.findAll().size();
        // set the field null
        kunde.setAnrede(null);

        // Create the Kunde, which fails.


        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = kundeRepository.findAll().size();
        // set the field null
        kunde.setName(null);

        // Create the Kunde, which fails.


        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVornameIsRequired() throws Exception {
        int databaseSizeBeforeTest = kundeRepository.findAll().size();
        // set the field null
        kunde.setVorname(null);

        // Create the Kunde, which fails.


        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = kundeRepository.findAll().size();
        // set the field null
        kunde.setEmail(null);

        // Create the Kunde, which fails.


        restKundeMockMvc.perform(post("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKundes() throws Exception {
        // Initialize the database
        kundeRepository.saveAndFlush(kunde);

        // Get all the kundeList
        restKundeMockMvc.perform(get("/api/kundes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kunde.getId().intValue())))
            .andExpect(jsonPath("$.[*].anrede").value(hasItem(DEFAULT_ANREDE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].plzort").value(hasItem(DEFAULT_PLZORT)))
            .andExpect(jsonPath("$.[*].telefonnr").value(hasItem(DEFAULT_TELEFONNR)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getKunde() throws Exception {
        // Initialize the database
        kundeRepository.saveAndFlush(kunde);

        // Get the kunde
        restKundeMockMvc.perform(get("/api/kundes/{id}", kunde.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kunde.getId().intValue()))
            .andExpect(jsonPath("$.anrede").value(DEFAULT_ANREDE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.vorname").value(DEFAULT_VORNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.strasse").value(DEFAULT_STRASSE))
            .andExpect(jsonPath("$.plzort").value(DEFAULT_PLZORT))
            .andExpect(jsonPath("$.telefonnr").value(DEFAULT_TELEFONNR))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingKunde() throws Exception {
        // Get the kunde
        restKundeMockMvc.perform(get("/api/kundes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKunde() throws Exception {
        // Initialize the database
        kundeRepository.saveAndFlush(kunde);

        int databaseSizeBeforeUpdate = kundeRepository.findAll().size();

        // Update the kunde
        Kunde updatedKunde = kundeRepository.findById(kunde.getId()).get();
        // Disconnect from session so that the updates on updatedKunde are not directly saved in db
        em.detach(updatedKunde);
        updatedKunde
            .anrede(UPDATED_ANREDE)
            .name(UPDATED_NAME)
            .vorname(UPDATED_VORNAME)
            .email(UPDATED_EMAIL)
            .strasse(UPDATED_STRASSE)
            .plzort(UPDATED_PLZORT)
            .telefonnr(UPDATED_TELEFONNR)
            .iban(UPDATED_IBAN)
            .created(UPDATED_CREATED);

        restKundeMockMvc.perform(put("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKunde)))
            .andExpect(status().isOk());

        // Validate the Kunde in the database
        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeUpdate);
        Kunde testKunde = kundeList.get(kundeList.size() - 1);
        assertThat(testKunde.getAnrede()).isEqualTo(UPDATED_ANREDE);
        assertThat(testKunde.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKunde.getVorname()).isEqualTo(UPDATED_VORNAME);
        assertThat(testKunde.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testKunde.getStrasse()).isEqualTo(UPDATED_STRASSE);
        assertThat(testKunde.getPlzort()).isEqualTo(UPDATED_PLZORT);
        assertThat(testKunde.getTelefonnr()).isEqualTo(UPDATED_TELEFONNR);
        assertThat(testKunde.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testKunde.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Kunde in Elasticsearch
        verify(mockKundeSearchRepository, times(1)).save(testKunde);
    }

    @Test
    @Transactional
    public void updateNonExistingKunde() throws Exception {
        int databaseSizeBeforeUpdate = kundeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKundeMockMvc.perform(put("/api/kundes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kunde)))
            .andExpect(status().isBadRequest());

        // Validate the Kunde in the database
        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Kunde in Elasticsearch
        verify(mockKundeSearchRepository, times(0)).save(kunde);
    }

    @Test
    @Transactional
    public void deleteKunde() throws Exception {
        // Initialize the database
        kundeRepository.saveAndFlush(kunde);

        int databaseSizeBeforeDelete = kundeRepository.findAll().size();

        // Delete the kunde
        restKundeMockMvc.perform(delete("/api/kundes/{id}", kunde.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kunde> kundeList = kundeRepository.findAll();
        assertThat(kundeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Kunde in Elasticsearch
        verify(mockKundeSearchRepository, times(1)).deleteById(kunde.getId());
    }

    @Test
    @Transactional
    public void searchKunde() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        kundeRepository.saveAndFlush(kunde);
        when(mockKundeSearchRepository.search(queryStringQuery("id:" + kunde.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(kunde), PageRequest.of(0, 1), 1));

        // Search the kunde
        restKundeMockMvc.perform(get("/api/_search/kundes?query=id:" + kunde.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kunde.getId().intValue())))
            .andExpect(jsonPath("$.[*].anrede").value(hasItem(DEFAULT_ANREDE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].strasse").value(hasItem(DEFAULT_STRASSE)))
            .andExpect(jsonPath("$.[*].plzort").value(hasItem(DEFAULT_PLZORT)))
            .andExpect(jsonPath("$.[*].telefonnr").value(hasItem(DEFAULT_TELEFONNR)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
}
