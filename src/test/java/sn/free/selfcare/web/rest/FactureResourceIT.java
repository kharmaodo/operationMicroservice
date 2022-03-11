package sn.free.selfcare.web.rest;

import sn.free.selfcare.OperationMicroserviceApp;
import sn.free.selfcare.config.SecurityBeanOverrideConfiguration;
import sn.free.selfcare.domain.Facture;
import sn.free.selfcare.repository.FactureRepository;
import sn.free.selfcare.repository.search.FactureSearchRepository;
import sn.free.selfcare.service.dto.FactureDTO;
import sn.free.selfcare.service.mapper.FactureMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.free.selfcare.domain.enumeration.ActionStatus;
/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, OperationMicroserviceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FactureResourceIT {

    private static final String DEFAULT_CODE_FACTURE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FACTURE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "1";
    private static final String UPDATED_CLIENT_ID = "2";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Instant DEFAULT_DATE_EMISSION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EMISSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_PAIEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ActionStatus DEFAULT_PAIEMENT_STATUS = ActionStatus.PENDING;
    private static final ActionStatus UPDATED_PAIEMENT_STATUS = ActionStatus.INPROGRESS;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureMapper factureMapper;

    /**
     * This repository is mocked in the sn.free.selfcare.repository.search test package.
     *
     * @see sn.free.selfcare.repository.search.FactureSearchRepositoryMockConfiguration
     */
    @Autowired
    private FactureSearchRepository mockFactureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .codeFacture(DEFAULT_CODE_FACTURE)
            .clientId(DEFAULT_CLIENT_ID)
            .amount(DEFAULT_AMOUNT)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .paiementStatus(DEFAULT_PAIEMENT_STATUS);
        return facture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture facture = new Facture()
            .codeFacture(UPDATED_CODE_FACTURE)
            .clientId(UPDATED_CLIENT_ID)
            .amount(UPDATED_AMOUNT)
            .dateEmission(UPDATED_DATE_EMISSION)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .paiementStatus(UPDATED_PAIEMENT_STATUS);
        return facture;
    }

    @BeforeEach
    public void initTest() {
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();
        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getCodeFacture()).isEqualTo(DEFAULT_CODE_FACTURE);
        assertThat(testFacture.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testFacture.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testFacture.getDateEmission()).isEqualTo(DEFAULT_DATE_EMISSION);
        assertThat(testFacture.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testFacture.getPaiementStatus()).isEqualTo(DEFAULT_PAIEMENT_STATUS);

        // Validate the Facture in Elasticsearch
        verify(mockFactureSearchRepository, times(1)).save(testFacture);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Facture in Elasticsearch
        verify(mockFactureSearchRepository, times(0)).save(facture);
    }


    @Test
    @Transactional
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setClientId(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);


        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setAmount(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);


        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateEmissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateEmission(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);


        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaiementStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setPaiementStatus(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);


        restFactureMockMvc.perform(post("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeFacture").value(hasItem(DEFAULT_CODE_FACTURE)))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.codeFacture").value(DEFAULT_CODE_FACTURE))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.paiementStatus").value(DEFAULT_PAIEMENT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .codeFacture(UPDATED_CODE_FACTURE)
            .clientId(UPDATED_CLIENT_ID)
            .amount(UPDATED_AMOUNT)
            .dateEmission(UPDATED_DATE_EMISSION)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .paiementStatus(UPDATED_PAIEMENT_STATUS);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc.perform(put("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getCodeFacture()).isEqualTo(UPDATED_CODE_FACTURE);
        assertThat(testFacture.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testFacture.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testFacture.getDateEmission()).isEqualTo(UPDATED_DATE_EMISSION);
        assertThat(testFacture.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testFacture.getPaiementStatus()).isEqualTo(UPDATED_PAIEMENT_STATUS);

        // Validate the Facture in Elasticsearch
        verify(mockFactureSearchRepository, times(1)).save(testFacture);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc.perform(put("/api/factures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Facture in Elasticsearch
        verify(mockFactureSearchRepository, times(0)).save(facture);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Delete the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Facture in Elasticsearch
        verify(mockFactureSearchRepository, times(1)).deleteById(facture.getId());
    }

    @Test
    @Transactional
    public void searchFacture() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        when(mockFactureSearchRepository.search(queryStringQuery("id:" + facture.getId())))
            .thenReturn(Collections.singletonList(facture));

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeFacture").value(hasItem(DEFAULT_CODE_FACTURE)))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())));
    }
}
