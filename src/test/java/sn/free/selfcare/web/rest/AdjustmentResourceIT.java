package sn.free.selfcare.web.rest;

import sn.free.selfcare.OperationMicroserviceApp;
import sn.free.selfcare.config.SecurityBeanOverrideConfiguration;
import sn.free.selfcare.domain.Adjustment;
import sn.free.selfcare.repository.AdjustmentRepository;
import sn.free.selfcare.repository.search.AdjustmentSearchRepository;
import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.service.dto.AdjustmentDTO;
import sn.free.selfcare.service.mapper.AdjustmentMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import sn.free.selfcare.domain.enumeration.AdjustmentType;
import sn.free.selfcare.domain.enumeration.ActionStatus;
/**
 * Integration tests for the {@link AdjustmentResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, OperationMicroserviceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AdjustmentResourceIT {

    private static final Long DEFAULT_CLIENT_ID = 1L;
    private static final Long UPDATED_CLIENT_ID = 2L;

    private static final Long DEFAULT_GROUPE_ID = 1L;
    private static final Long UPDATED_GROUPE_ID = 2L;

    private static final String DEFAULT_TARGET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_CREDIT = 1D;
    private static final Double UPDATED_CREDIT = 2D;

    private static final Double DEFAULT_SMS = 1D;
    private static final Double UPDATED_SMS = 2D;

    private static final Double DEFAULT_MIN_APPEL = 1D;
    private static final Double UPDATED_MIN_APPEL = 2D;

    private static final Double DEFAULT_GO_DATA = 1D;
    private static final Double UPDATED_GO_DATA = 2D;

    private static final AdjustmentType DEFAULT_TYPE_ADJUSTMENT = AdjustmentType.PERIODIC;
    private static final AdjustmentType UPDATED_TYPE_ADJUSTMENT = AdjustmentType.MANUAL;

    private static final Instant DEFAULT_DATE_ADJUSTMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADJUSTMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TRIALS = 1;
    private static final Integer UPDATED_TRIALS = 2;

    private static final ActionStatus DEFAULT_STATUS = ActionStatus.PENDING;
    private static final ActionStatus UPDATED_STATUS = ActionStatus.INPROGRESS;

    @Autowired
    private AdjustmentRepository adjustmentRepository;

    @Autowired
    private AdjustmentMapper adjustmentMapper;

    @Autowired
    private AdjustmentService adjustmentService;

    /**
     * This repository is mocked in the sn.free.selfcare.repository.search test package.
     *
     * @see sn.free.selfcare.repository.search.AdjustmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private AdjustmentSearchRepository mockAdjustmentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdjustmentMockMvc;

    private Adjustment adjustment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjustment createEntity(EntityManager em) {
        Adjustment adjustment = new Adjustment()
            .clientId(DEFAULT_CLIENT_ID)
            .groupeId(DEFAULT_GROUPE_ID)
            .targetNumber(DEFAULT_TARGET_NUMBER)
            .credit(DEFAULT_CREDIT)
            .sms(DEFAULT_SMS)
            .minAppel(DEFAULT_MIN_APPEL)
            .goData(DEFAULT_GO_DATA)
            .typeAdjustment(DEFAULT_TYPE_ADJUSTMENT)
            .dateAdjustment(DEFAULT_DATE_ADJUSTMENT)
            .trials(DEFAULT_TRIALS)
            .status(DEFAULT_STATUS);
        return adjustment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjustment createUpdatedEntity(EntityManager em) {
        Adjustment adjustment = new Adjustment()
            .clientId(UPDATED_CLIENT_ID)
            .groupeId(UPDATED_GROUPE_ID)
            .targetNumber(UPDATED_TARGET_NUMBER)
            .credit(UPDATED_CREDIT)
            .sms(UPDATED_SMS)
            .minAppel(UPDATED_MIN_APPEL)
            .goData(UPDATED_GO_DATA)
            .typeAdjustment(UPDATED_TYPE_ADJUSTMENT)
            .dateAdjustment(UPDATED_DATE_ADJUSTMENT)
            .trials(UPDATED_TRIALS)
            .status(UPDATED_STATUS);
        return adjustment;
    }

    @BeforeEach
    public void initTest() {
        adjustment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdjustment() throws Exception {
        int databaseSizeBeforeCreate = adjustmentRepository.findAll().size();
        // Create the Adjustment
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);
        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Adjustment in the database
        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeCreate + 1);
        Adjustment testAdjustment = adjustmentList.get(adjustmentList.size() - 1);
        assertThat(testAdjustment.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testAdjustment.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testAdjustment.getTargetNumber()).isEqualTo(DEFAULT_TARGET_NUMBER);
        assertThat(testAdjustment.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testAdjustment.getSms()).isEqualTo(DEFAULT_SMS);
        assertThat(testAdjustment.getMinAppel()).isEqualTo(DEFAULT_MIN_APPEL);
        assertThat(testAdjustment.getGoData()).isEqualTo(DEFAULT_GO_DATA);
        assertThat(testAdjustment.getTypeAdjustment()).isEqualTo(DEFAULT_TYPE_ADJUSTMENT);
        assertThat(testAdjustment.getDateAdjustment()).isEqualTo(DEFAULT_DATE_ADJUSTMENT);
        assertThat(testAdjustment.getTrials()).isEqualTo(DEFAULT_TRIALS);
        assertThat(testAdjustment.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Adjustment in Elasticsearch
        verify(mockAdjustmentSearchRepository, times(1)).save(testAdjustment);
    }

    @Test
    @Transactional
    public void createAdjustmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adjustmentRepository.findAll().size();

        // Create the Adjustment with an existing ID
        adjustment.setId(1L);
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adjustment in the database
        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Adjustment in Elasticsearch
        verify(mockAdjustmentSearchRepository, times(0)).save(adjustment);
    }


    @Test
    @Transactional
    public void checkTargetNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = adjustmentRepository.findAll().size();
        // set the field null
        adjustment.setTargetNumber(null);

        // Create the Adjustment, which fails.
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);


        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeAdjustmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = adjustmentRepository.findAll().size();
        // set the field null
        adjustment.setTypeAdjustment(null);

        // Create the Adjustment, which fails.
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);


        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAdjustmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = adjustmentRepository.findAll().size();
        // set the field null
        adjustment.setDateAdjustment(null);

        // Create the Adjustment, which fails.
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);


        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTrialsIsRequired() throws Exception {
        int databaseSizeBeforeTest = adjustmentRepository.findAll().size();
        // set the field null
        adjustment.setTrials(null);

        // Create the Adjustment, which fails.
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);


        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = adjustmentRepository.findAll().size();
        // set the field null
        adjustment.setStatus(null);

        // Create the Adjustment, which fails.
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);


        restAdjustmentMockMvc.perform(post("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdjustments() throws Exception {
        // Initialize the database
        adjustmentRepository.saveAndFlush(adjustment);

        // Get all the adjustmentList
        restAdjustmentMockMvc.perform(get("/api/adjustments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetNumber").value(hasItem(DEFAULT_TARGET_NUMBER)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].sms").value(hasItem(DEFAULT_SMS.doubleValue())))
            .andExpect(jsonPath("$.[*].minAppel").value(hasItem(DEFAULT_MIN_APPEL.doubleValue())))
            .andExpect(jsonPath("$.[*].goData").value(hasItem(DEFAULT_GO_DATA.doubleValue())))
            .andExpect(jsonPath("$.[*].typeAdjustment").value(hasItem(DEFAULT_TYPE_ADJUSTMENT.toString())))
            .andExpect(jsonPath("$.[*].dateAdjustment").value(hasItem(DEFAULT_DATE_ADJUSTMENT.toString())))
            .andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getAdjustment() throws Exception {
        // Initialize the database
        adjustmentRepository.saveAndFlush(adjustment);

        // Get the adjustment
        restAdjustmentMockMvc.perform(get("/api/adjustments/{id}", adjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adjustment.getId().intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.intValue()))
            .andExpect(jsonPath("$.groupeId").value(DEFAULT_GROUPE_ID.intValue()))
            .andExpect(jsonPath("$.targetNumber").value(DEFAULT_TARGET_NUMBER))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.sms").value(DEFAULT_SMS.doubleValue()))
            .andExpect(jsonPath("$.minAppel").value(DEFAULT_MIN_APPEL.doubleValue()))
            .andExpect(jsonPath("$.goData").value(DEFAULT_GO_DATA.doubleValue()))
            .andExpect(jsonPath("$.typeAdjustment").value(DEFAULT_TYPE_ADJUSTMENT.toString()))
            .andExpect(jsonPath("$.dateAdjustment").value(DEFAULT_DATE_ADJUSTMENT.toString()))
            .andExpect(jsonPath("$.trials").value(DEFAULT_TRIALS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAdjustment() throws Exception {
        // Get the adjustment
        restAdjustmentMockMvc.perform(get("/api/adjustments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdjustment() throws Exception {
        // Initialize the database
        adjustmentRepository.saveAndFlush(adjustment);

        int databaseSizeBeforeUpdate = adjustmentRepository.findAll().size();

        // Update the adjustment
        Adjustment updatedAdjustment = adjustmentRepository.findById(adjustment.getId()).get();
        // Disconnect from session so that the updates on updatedAdjustment are not directly saved in db
        em.detach(updatedAdjustment);
        updatedAdjustment
            .clientId(UPDATED_CLIENT_ID)
            .groupeId(UPDATED_GROUPE_ID)
            .targetNumber(UPDATED_TARGET_NUMBER)
            .credit(UPDATED_CREDIT)
            .sms(UPDATED_SMS)
            .minAppel(UPDATED_MIN_APPEL)
            .goData(UPDATED_GO_DATA)
            .typeAdjustment(UPDATED_TYPE_ADJUSTMENT)
            .dateAdjustment(UPDATED_DATE_ADJUSTMENT)
            .trials(UPDATED_TRIALS)
            .status(UPDATED_STATUS);
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(updatedAdjustment);

        restAdjustmentMockMvc.perform(put("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isOk());

        // Validate the Adjustment in the database
        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeUpdate);
        Adjustment testAdjustment = adjustmentList.get(adjustmentList.size() - 1);
        assertThat(testAdjustment.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testAdjustment.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testAdjustment.getTargetNumber()).isEqualTo(UPDATED_TARGET_NUMBER);
        assertThat(testAdjustment.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testAdjustment.getSms()).isEqualTo(UPDATED_SMS);
        assertThat(testAdjustment.getMinAppel()).isEqualTo(UPDATED_MIN_APPEL);
        assertThat(testAdjustment.getGoData()).isEqualTo(UPDATED_GO_DATA);
        assertThat(testAdjustment.getTypeAdjustment()).isEqualTo(UPDATED_TYPE_ADJUSTMENT);
        assertThat(testAdjustment.getDateAdjustment()).isEqualTo(UPDATED_DATE_ADJUSTMENT);
        assertThat(testAdjustment.getTrials()).isEqualTo(UPDATED_TRIALS);
        assertThat(testAdjustment.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Adjustment in Elasticsearch
        verify(mockAdjustmentSearchRepository, times(1)).save(testAdjustment);
    }

    @Test
    @Transactional
    public void updateNonExistingAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = adjustmentRepository.findAll().size();

        // Create the Adjustment
        AdjustmentDTO adjustmentDTO = adjustmentMapper.toDto(adjustment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdjustmentMockMvc.perform(put("/api/adjustments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adjustmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adjustment in the database
        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Adjustment in Elasticsearch
        verify(mockAdjustmentSearchRepository, times(0)).save(adjustment);
    }

    @Test
    @Transactional
    public void deleteAdjustment() throws Exception {
        // Initialize the database
        adjustmentRepository.saveAndFlush(adjustment);

        int databaseSizeBeforeDelete = adjustmentRepository.findAll().size();

        // Delete the adjustment
        restAdjustmentMockMvc.perform(delete("/api/adjustments/{id}", adjustment.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adjustment> adjustmentList = adjustmentRepository.findAll();
        assertThat(adjustmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Adjustment in Elasticsearch
        verify(mockAdjustmentSearchRepository, times(1)).deleteById(adjustment.getId());
    }

    @Test
    @Transactional
    public void searchAdjustment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        adjustmentRepository.saveAndFlush(adjustment);
        when(mockAdjustmentSearchRepository.search(queryStringQuery("id:" + adjustment.getId())))
            .thenReturn(Collections.singletonList(adjustment));

        // Search the adjustment
        restAdjustmentMockMvc.perform(get("/api/_search/adjustments?query=id:" + adjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetNumber").value(hasItem(DEFAULT_TARGET_NUMBER)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].sms").value(hasItem(DEFAULT_SMS.doubleValue())))
            .andExpect(jsonPath("$.[*].minAppel").value(hasItem(DEFAULT_MIN_APPEL.doubleValue())))
            .andExpect(jsonPath("$.[*].goData").value(hasItem(DEFAULT_GO_DATA.doubleValue())))
            .andExpect(jsonPath("$.[*].typeAdjustment").value(hasItem(DEFAULT_TYPE_ADJUSTMENT.toString())))
            .andExpect(jsonPath("$.[*].dateAdjustment").value(hasItem(DEFAULT_DATE_ADJUSTMENT.toString())))
            .andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
