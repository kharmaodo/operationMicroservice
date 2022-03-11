package sn.free.selfcare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

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

import sn.free.selfcare.OperationMicroserviceApp;
import sn.free.selfcare.config.SecurityBeanOverrideConfiguration;
import sn.free.selfcare.domain.Facture;
import sn.free.selfcare.domain.Paiement;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.repository.PaiementRepository;
import sn.free.selfcare.repository.search.PaiementSearchRepository;
import sn.free.selfcare.service.dto.PaiementDTO;
import sn.free.selfcare.service.mapper.PaiementMapper;

/**
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, OperationMicroserviceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaiementResourceIT {

	private static final Long DEFAULT_CLIENT_ID = 1L;
	private static final Long UPDATED_CLIENT_ID = 2L;

	private static final Facture DEFAULT_FACTURE_ID = new Facture();
	private static final Facture UPDATED_FACTURE_ID = new Facture();

	private static final Double DEFAULT_AMOUNT = 1D;
	private static final Double UPDATED_AMOUNT = 2D;

	private static final Instant DEFAULT_DATE_PAIEMENT = Instant.ofEpochMilli(0L);
	private static final Instant UPDATED_DATE_PAIEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

	private static final Integer DEFAULT_TRIALS = 1;
	private static final Integer UPDATED_TRIALS = 2;

	private static final ActionStatus DEFAULT_PAIEMENT_STATUS = ActionStatus.PENDING;
	private static final ActionStatus UPDATED_PAIEMENT_STATUS = ActionStatus.INPROGRESS;

	@Autowired
	private PaiementRepository paiementRepository;

	@Autowired
	private PaiementMapper paiementMapper;

	/**
	 * This repository is mocked in the sn.free.selfcare.repository.search test
	 * package.
	 *
	 * @see sn.free.selfcare.repository.search.PaiementSearchRepositoryMockConfiguration
	 */
	@Autowired
	private PaiementSearchRepository mockPaiementSearchRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restPaiementMockMvc;

	private Paiement paiement;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Paiement createEntity(EntityManager em) {
		Paiement paiement = new Paiement().clientId(DEFAULT_CLIENT_ID).facture(DEFAULT_FACTURE_ID)
				.amount(DEFAULT_AMOUNT).datePaiement(DEFAULT_DATE_PAIEMENT).trials(DEFAULT_TRIALS)
				.paiementStatus(DEFAULT_PAIEMENT_STATUS);
		return paiement;
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Paiement createUpdatedEntity(EntityManager em) {
		Paiement paiement = new Paiement().clientId(UPDATED_CLIENT_ID).facture(UPDATED_FACTURE_ID)
				.amount(UPDATED_AMOUNT).datePaiement(UPDATED_DATE_PAIEMENT).trials(UPDATED_TRIALS)
				.paiementStatus(UPDATED_PAIEMENT_STATUS);
		return paiement;
	}

	@BeforeEach
	public void initTest() {
		paiement = createEntity(em);
	}

	@Test
	@Transactional
	public void createPaiement() throws Exception {
		int databaseSizeBeforeCreate = paiementRepository.findAll().size();
		// Create the Paiement
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
		restPaiementMockMvc.perform(post("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isCreated());

		// Validate the Paiement in the database
		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeCreate + 1);
		Paiement testPaiement = paiementList.get(paiementList.size() - 1);
		assertThat(testPaiement.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
		assertThat(testPaiement.getFacture()).isEqualTo(DEFAULT_FACTURE_ID);
		assertThat(testPaiement.getAmount()).isEqualTo(DEFAULT_AMOUNT);
		assertThat(testPaiement.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
		assertThat(testPaiement.getTrials()).isEqualTo(DEFAULT_TRIALS);
		assertThat(testPaiement.getPaiementStatus()).isEqualTo(DEFAULT_PAIEMENT_STATUS);

		// Validate the Paiement in Elasticsearch
		verify(mockPaiementSearchRepository, times(1)).save(testPaiement);
	}

	@Test
	@Transactional
	public void createPaiementWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = paiementRepository.findAll().size();

		// Create the Paiement with an existing ID
		paiement.setId(1L);
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPaiementMockMvc.perform(post("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isBadRequest());

		// Validate the Paiement in the database
		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeCreate);

		// Validate the Paiement in Elasticsearch
		verify(mockPaiementSearchRepository, times(0)).save(paiement);
	}

	@Test
	@Transactional
	public void checkClientIdIsRequired() throws Exception {
		int databaseSizeBeforeTest = paiementRepository.findAll().size();
		// set the field null
		paiement.setClientId(null);

		// Create the Paiement, which fails.
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

		restPaiementMockMvc.perform(post("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isBadRequest());

		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkAmountIsRequired() throws Exception {
		int databaseSizeBeforeTest = paiementRepository.findAll().size();
		// set the field null
		paiement.setAmount(null);

		// Create the Paiement, which fails.
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

		restPaiementMockMvc.perform(post("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isBadRequest());

		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkPaiementStatusIsRequired() throws Exception {
		int databaseSizeBeforeTest = paiementRepository.findAll().size();
		// set the field null
		paiement.setPaiementStatus(null);

		// Create the Paiement, which fails.
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

		restPaiementMockMvc.perform(post("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isBadRequest());

		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllPaiements() throws Exception {
		// Initialize the database
		paiementRepository.saveAndFlush(paiement);

		// Get all the paiementList
		restPaiementMockMvc.perform(get("/api/paiements?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
				.andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
				// .andExpect(jsonPath("$.[*].factureId").value(hasItem(DEFAULT_FACTURE_ID.intValue())))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
				.andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
				.andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())));
	}

	@Test
	@Transactional
	public void getPaiement() throws Exception {
		// Initialize the database
		paiementRepository.saveAndFlush(paiement);

		// Get the paiement
		restPaiementMockMvc.perform(get("/api/paiements/{id}", paiement.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
				.andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID))
				// .andExpect(jsonPath("$.factureId").value(DEFAULT_FACTURE_ID.intValue()))
				.andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
				.andExpect(jsonPath("$.trials").value(DEFAULT_TRIALS))
				.andExpect(jsonPath("$.paiementStatus").value(DEFAULT_PAIEMENT_STATUS.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingPaiement() throws Exception {
		// Get the paiement
		restPaiementMockMvc.perform(get("/api/paiements/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePaiement() throws Exception {
		// Initialize the database
		paiementRepository.saveAndFlush(paiement);

		int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

		// Update the paiement
		Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).get();
		// Disconnect from session so that the updates on updatedPaiement are not
		// directly saved in db
		em.detach(updatedPaiement);
		updatedPaiement.clientId(UPDATED_CLIENT_ID).facture(UPDATED_FACTURE_ID).amount(UPDATED_AMOUNT)
				.datePaiement(UPDATED_DATE_PAIEMENT).trials(UPDATED_TRIALS).paiementStatus(UPDATED_PAIEMENT_STATUS);
		PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

		restPaiementMockMvc.perform(put("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isOk());

		// Validate the Paiement in the database
		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
		Paiement testPaiement = paiementList.get(paiementList.size() - 1);
		assertThat(testPaiement.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
		assertThat(testPaiement.getFacture()).isEqualTo(UPDATED_FACTURE_ID);
		assertThat(testPaiement.getAmount()).isEqualTo(UPDATED_AMOUNT);
		assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
		assertThat(testPaiement.getTrials()).isEqualTo(UPDATED_TRIALS);
		assertThat(testPaiement.getPaiementStatus()).isEqualTo(UPDATED_PAIEMENT_STATUS);

		// Validate the Paiement in Elasticsearch
		verify(mockPaiementSearchRepository, times(1)).save(testPaiement);
	}

	@Test
	@Transactional
	public void updateNonExistingPaiement() throws Exception {
		int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

		// Create the Paiement
		PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restPaiementMockMvc.perform(put("/api/paiements").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paiementDTO))).andExpect(status().isBadRequest());

		// Validate the Paiement in the database
		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

		// Validate the Paiement in Elasticsearch
		verify(mockPaiementSearchRepository, times(0)).save(paiement);
	}

	@Test
	@Transactional
	public void deletePaiement() throws Exception {
		// Initialize the database
		paiementRepository.saveAndFlush(paiement);

		int databaseSizeBeforeDelete = paiementRepository.findAll().size();

		// Delete the paiement
		restPaiementMockMvc
				.perform(
						delete("/api/paiements/{id}", paiement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<Paiement> paiementList = paiementRepository.findAll();
		assertThat(paiementList).hasSize(databaseSizeBeforeDelete - 1);

		// Validate the Paiement in Elasticsearch
		verify(mockPaiementSearchRepository, times(1)).deleteById(paiement.getId());
	}

	@Test
	@Transactional
	public void searchPaiement() throws Exception {
		// Configure the mock search repository
		// Initialize the database
		paiementRepository.saveAndFlush(paiement);
		when(mockPaiementSearchRepository.search(queryStringQuery("id:" + paiement.getId())))
				.thenReturn(Collections.singletonList(paiement));

		// Search the paiement
		restPaiementMockMvc.perform(get("/api/_search/paiements?query=id:" + paiement.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
				.andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
				// .andExpect(jsonPath("$.[*].factureId").value(hasItem(DEFAULT_FACTURE_ID.intValue())))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
				.andExpect(jsonPath("$.[*].trials").value(hasItem(DEFAULT_TRIALS)))
				.andExpect(jsonPath("$.[*].paiementStatus").value(hasItem(DEFAULT_PAIEMENT_STATUS.toString())));
	}
}
