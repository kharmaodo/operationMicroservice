package sn.free.selfcare.web.rest;

import sn.free.selfcare.OperationMicroserviceApp;
import sn.free.selfcare.config.SecurityBeanOverrideConfiguration;
import sn.free.selfcare.domain.Payasyougo;
import sn.free.selfcare.repository.PayasyougoRepository;
import sn.free.selfcare.repository.search.PayasyougoSearchRepository;
import sn.free.selfcare.service.PayasyougoService;
import sn.free.selfcare.service.dto.PayasyougoDTO;
import sn.free.selfcare.service.mapper.PayasyougoMapper;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.free.selfcare.domain.enumeration.ActionStatus;
/**
 * Integration tests for the {@link PayasyougoResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, OperationMicroserviceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PayasyougoResourceIT {

    private static final String DEFAULT_NUMERO_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CLIENT = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_MSISDN = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_MSISDN = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_PAIEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TRIALS = 1;
    private static final Integer UPDATED_TRIALS = 2;

    private static final ActionStatus DEFAULT_PAIEMENT_STATUS = ActionStatus.PENDING;
    private static final ActionStatus UPDATED_PAIEMENT_STATUS = ActionStatus.INPROGRESS;

    private static final String DEFAULT_PAIEMENT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_PAIEMENT_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private PayasyougoRepository payasyougoRepository;

    @Autowired
    private PayasyougoMapper payasyougoMapper;

    @Autowired
    private PayasyougoService payasyougoService;

    /**
     * This repository is mocked in the sn.free.selfcare.repository.search test package.
     *
     * @see sn.free.selfcare.repository.search.PayasyougoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayasyougoSearchRepository mockPayasyougoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayasyougoMockMvc;

    private Payasyougo payasyougo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payasyougo createEntity(EntityManager em) {
        Payasyougo payasyougo = new Payasyougo()
            .numeroClient(DEFAULT_NUMERO_CLIENT)
            .amount(DEFAULT_AMOUNT)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .customerMsisdn(DEFAULT_CUSTOMER_MSISDN)
            .customerAccount(DEFAULT_CUSTOMER_ACCOUNT)
            .currency(DEFAULT_CURRENCY)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .trials(DEFAULT_TRIALS)
            .paiementStatus(DEFAULT_PAIEMENT_STATUS)
            .paiementMessage(DEFAULT_PAIEMENT_MESSAGE);
        return payasyougo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payasyougo createUpdatedEntity(EntityManager em) {
        Payasyougo payasyougo = new Payasyougo()
            .numeroClient(UPDATED_NUMERO_CLIENT)
            .amount(UPDATED_AMOUNT)
            .countryCode(UPDATED_COUNTRY_CODE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .customerMsisdn(UPDATED_CUSTOMER_MSISDN)
            .customerAccount(UPDATED_CUSTOMER_ACCOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionId(UPDATED_TRANSACTION_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .trials(UPDATED_TRIALS)
            .paiementStatus(UPDATED_PAIEMENT_STATUS)
            .paiementMessage(UPDATED_PAIEMENT_MESSAGE);
        return payasyougo;
    }

    @BeforeEach
    public void initTest() {
        payasyougo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayasyougo() throws Exception {
        int databaseSizeBeforeCreate = payasyougoRepository.findAll().size();
        // Create the Payasyougo
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);
        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isCreated());

        // Validate the Payasyougo in the database
        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeCreate + 1);
        Payasyougo testPayasyougo = payasyougoList.get(payasyougoList.size() - 1);
        assertThat(testPayasyougo.getNumeroClient()).isEqualTo(DEFAULT_NUMERO_CLIENT);
        assertThat(testPayasyougo.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayasyougo.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testPayasyougo.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testPayasyougo.getCustomerMsisdn()).isEqualTo(DEFAULT_CUSTOMER_MSISDN);
        assertThat(testPayasyougo.getCustomerAccount()).isEqualTo(DEFAULT_CUSTOMER_ACCOUNT);
        assertThat(testPayasyougo.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testPayasyougo.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPayasyougo.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayasyougo.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPayasyougo.getTrials()).isEqualTo(DEFAULT_TRIALS);
        assertThat(testPayasyougo.getPaiementStatus()).isEqualTo(DEFAULT_PAIEMENT_STATUS);
        assertThat(testPayasyougo.getPaiementMessage()).isEqualTo(DEFAULT_PAIEMENT_MESSAGE);

        // Validate the Payasyougo in Elasticsearch
        verify(mockPayasyougoSearchRepository, times(1)).save(testPayasyougo);
    }

    @Test
    @Transactional
    public void createPayasyougoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payasyougoRepository.findAll().size();

        // Create the Payasyougo with an existing ID
        payasyougo.setId(1L);
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payasyougo in the database
        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payasyougo in Elasticsearch
        verify(mockPayasyougoSearchRepository, times(0)).save(payasyougo);
    }


    @Test
    @Transactional
    public void checkNumeroClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setNumeroClient(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setAmount(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setInvoiceNumber(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerMsisdnIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setCustomerMsisdn(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setCustomerAccount(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setTransactionId(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaiementStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = payasyougoRepository.findAll().size();
        // set the field null
        payasyougo.setPaiementStatus(null);

        // Create the Payasyougo, which fails.
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);


        restPayasyougoMockMvc.perform(post("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayasyougos() throws Exception {
        // Initialize the database
        payasyougoRepository.saveAndFlush(payasyougo);

        // Get all the payasyougoList
        restPayasyougoMockMvc.perform(get("/api/payasyougos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payasyougo.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroClient").value(hasItem(DEFAULT_NUMERO_CLIENT)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].customerMsisdn").value(hasItem(DEFAULT_CUSTOMER_MSISDN)))
            .andExpect(jsonPath("$.[*].customerAccount").value(hasItem(DEFAULT_CUSTOMER_ACCOUNT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
            .andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paiementMessage").value(hasItem(DEFAULT_PAIEMENT_MESSAGE)));
    }
    
    @Test
    @Transactional
    public void getPayasyougo() throws Exception {
        // Initialize the database
        payasyougoRepository.saveAndFlush(payasyougo);

        // Get the payasyougo
        restPayasyougoMockMvc.perform(get("/api/payasyougos/{id}", payasyougo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payasyougo.getId().intValue()))
            .andExpect(jsonPath("$.numeroClient").value(DEFAULT_NUMERO_CLIENT))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.customerMsisdn").value(DEFAULT_CUSTOMER_MSISDN))
            .andExpect(jsonPath("$.customerAccount").value(DEFAULT_CUSTOMER_ACCOUNT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.trials").value(DEFAULT_TRIALS))
            .andExpect(jsonPath("$.paiementStatus").value(DEFAULT_PAIEMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paiementMessage").value(DEFAULT_PAIEMENT_MESSAGE));
    }
    @Test
    @Transactional
    public void getNonExistingPayasyougo() throws Exception {
        // Get the payasyougo
        restPayasyougoMockMvc.perform(get("/api/payasyougos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayasyougo() throws Exception {
        // Initialize the database
        payasyougoRepository.saveAndFlush(payasyougo);

        int databaseSizeBeforeUpdate = payasyougoRepository.findAll().size();

        // Update the payasyougo
        Payasyougo updatedPayasyougo = payasyougoRepository.findById(payasyougo.getId()).get();
        // Disconnect from session so that the updates on updatedPayasyougo are not directly saved in db
        em.detach(updatedPayasyougo);
        updatedPayasyougo
            .numeroClient(UPDATED_NUMERO_CLIENT)
            .amount(UPDATED_AMOUNT)
            .countryCode(UPDATED_COUNTRY_CODE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .customerMsisdn(UPDATED_CUSTOMER_MSISDN)
            .customerAccount(UPDATED_CUSTOMER_ACCOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionId(UPDATED_TRANSACTION_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .trials(UPDATED_TRIALS)
            .paiementStatus(UPDATED_PAIEMENT_STATUS)
            .paiementMessage(UPDATED_PAIEMENT_MESSAGE);
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(updatedPayasyougo);

        restPayasyougoMockMvc.perform(put("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isOk());

        // Validate the Payasyougo in the database
        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeUpdate);
        Payasyougo testPayasyougo = payasyougoList.get(payasyougoList.size() - 1);
        assertThat(testPayasyougo.getNumeroClient()).isEqualTo(UPDATED_NUMERO_CLIENT);
        assertThat(testPayasyougo.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayasyougo.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testPayasyougo.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testPayasyougo.getCustomerMsisdn()).isEqualTo(UPDATED_CUSTOMER_MSISDN);
        assertThat(testPayasyougo.getCustomerAccount()).isEqualTo(UPDATED_CUSTOMER_ACCOUNT);
        assertThat(testPayasyougo.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testPayasyougo.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPayasyougo.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayasyougo.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPayasyougo.getTrials()).isEqualTo(UPDATED_TRIALS);
        assertThat(testPayasyougo.getPaiementStatus()).isEqualTo(UPDATED_PAIEMENT_STATUS);
        assertThat(testPayasyougo.getPaiementMessage()).isEqualTo(UPDATED_PAIEMENT_MESSAGE);

        // Validate the Payasyougo in Elasticsearch
        verify(mockPayasyougoSearchRepository, times(1)).save(testPayasyougo);
    }

    @Test
    @Transactional
    public void updateNonExistingPayasyougo() throws Exception {
        int databaseSizeBeforeUpdate = payasyougoRepository.findAll().size();

        // Create the Payasyougo
        PayasyougoDTO payasyougoDTO = payasyougoMapper.toDto(payasyougo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayasyougoMockMvc.perform(put("/api/payasyougos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payasyougoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payasyougo in the database
        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payasyougo in Elasticsearch
        verify(mockPayasyougoSearchRepository, times(0)).save(payasyougo);
    }

    @Test
    @Transactional
    public void deletePayasyougo() throws Exception {
        // Initialize the database
        payasyougoRepository.saveAndFlush(payasyougo);

        int databaseSizeBeforeDelete = payasyougoRepository.findAll().size();

        // Delete the payasyougo
        restPayasyougoMockMvc.perform(delete("/api/payasyougos/{id}", payasyougo.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payasyougo> payasyougoList = payasyougoRepository.findAll();
        assertThat(payasyougoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payasyougo in Elasticsearch
        verify(mockPayasyougoSearchRepository, times(1)).deleteById(payasyougo.getId());
    }

    @Test
    @Transactional
    public void searchPayasyougo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        payasyougoRepository.saveAndFlush(payasyougo);
        when(mockPayasyougoSearchRepository.search(queryStringQuery("id:" + payasyougo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payasyougo), PageRequest.of(0, 1), 1));

        // Search the payasyougo
        restPayasyougoMockMvc.perform(get("/api/_search/payasyougos?query=id:" + payasyougo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payasyougo.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroClient").value(hasItem(DEFAULT_NUMERO_CLIENT)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].customerMsisdn").value(hasItem(DEFAULT_CUSTOMER_MSISDN)))
            .andExpect(jsonPath("$.[*].customerAccount").value(hasItem(DEFAULT_CUSTOMER_ACCOUNT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
            .andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paiementMessage").value(hasItem(DEFAULT_PAIEMENT_MESSAGE)));
    }
}
