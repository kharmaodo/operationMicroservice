package sn.free.selfcare.web.rest;

import sn.free.selfcare.OperationMicroserviceApp;
import sn.free.selfcare.config.SecurityBeanOverrideConfiguration;
import sn.free.selfcare.domain.PeriodeEnvoi;
import sn.free.selfcare.repository.PeriodeEnvoiRepository;
import sn.free.selfcare.repository.search.PeriodeEnvoiSearchRepository;
import sn.free.selfcare.service.PeriodeEnvoiService;
import sn.free.selfcare.service.dto.PeriodeEnvoiDTO;
import sn.free.selfcare.service.mapper.PeriodeEnvoiMapper;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.free.selfcare.domain.enumeration.ObjectStatus;
/**
 * Integration tests for the {@link PeriodeEnvoiResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, OperationMicroserviceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PeriodeEnvoiResourceIT {

    private static final String DEFAULT_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_EXPRESSION = "BBBBBBBBBB";

    private static final Long DEFAULT_GROUPE_ID = 1L;
    private static final Long UPDATED_GROUPE_ID = 2L;

    private static final ObjectStatus DEFAULT_STATUS = ObjectStatus.ACTIVE;
    private static final ObjectStatus UPDATED_STATUS = ObjectStatus.INACTIVE;

    @Autowired
    private PeriodeEnvoiRepository periodeEnvoiRepository;

    @Autowired
    private PeriodeEnvoiMapper periodeEnvoiMapper;

    @Autowired
    private PeriodeEnvoiService periodeEnvoiService;

    /**
     * This repository is mocked in the sn.free.selfcare.repository.search test package.
     *
     * @see sn.free.selfcare.repository.search.PeriodeEnvoiSearchRepositoryMockConfiguration
     */
    @Autowired
    private PeriodeEnvoiSearchRepository mockPeriodeEnvoiSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodeEnvoiMockMvc;

    private PeriodeEnvoi periodeEnvoi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeEnvoi createEntity(EntityManager em) {
        PeriodeEnvoi periodeEnvoi = new PeriodeEnvoi()
            .expression(DEFAULT_EXPRESSION)
            .groupeId(DEFAULT_GROUPE_ID)
            .status(DEFAULT_STATUS);
        return periodeEnvoi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeEnvoi createUpdatedEntity(EntityManager em) {
        PeriodeEnvoi periodeEnvoi = new PeriodeEnvoi()
            .expression(UPDATED_EXPRESSION)
            .groupeId(UPDATED_GROUPE_ID)
            .status(UPDATED_STATUS);
        return periodeEnvoi;
    }

    @BeforeEach
    public void initTest() {
        periodeEnvoi = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodeEnvoi() throws Exception {
        int databaseSizeBeforeCreate = periodeEnvoiRepository.findAll().size();
        // Create the PeriodeEnvoi
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);
        restPeriodeEnvoiMockMvc.perform(post("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isCreated());

        // Validate the PeriodeEnvoi in the database
        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodeEnvoi testPeriodeEnvoi = periodeEnvoiList.get(periodeEnvoiList.size() - 1);
        assertThat(testPeriodeEnvoi.getExpression()).isEqualTo(DEFAULT_EXPRESSION);
        assertThat(testPeriodeEnvoi.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testPeriodeEnvoi.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the PeriodeEnvoi in Elasticsearch
        verify(mockPeriodeEnvoiSearchRepository, times(1)).save(testPeriodeEnvoi);
    }

    @Test
    @Transactional
    public void createPeriodeEnvoiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodeEnvoiRepository.findAll().size();

        // Create the PeriodeEnvoi with an existing ID
        periodeEnvoi.setId(1L);
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodeEnvoiMockMvc.perform(post("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodeEnvoi in the database
        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeCreate);

        // Validate the PeriodeEnvoi in Elasticsearch
        verify(mockPeriodeEnvoiSearchRepository, times(0)).save(periodeEnvoi);
    }


    @Test
    @Transactional
    public void checkExpressionIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodeEnvoiRepository.findAll().size();
        // set the field null
        periodeEnvoi.setExpression(null);

        // Create the PeriodeEnvoi, which fails.
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);


        restPeriodeEnvoiMockMvc.perform(post("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isBadRequest());

        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodeEnvoiRepository.findAll().size();
        // set the field null
        periodeEnvoi.setGroupeId(null);

        // Create the PeriodeEnvoi, which fails.
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);


        restPeriodeEnvoiMockMvc.perform(post("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isBadRequest());

        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodeEnvoiRepository.findAll().size();
        // set the field null
        periodeEnvoi.setStatus(null);

        // Create the PeriodeEnvoi, which fails.
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);


        restPeriodeEnvoiMockMvc.perform(post("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isBadRequest());

        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriodeEnvois() throws Exception {
        // Initialize the database
        periodeEnvoiRepository.saveAndFlush(periodeEnvoi);

        // Get all the periodeEnvoiList
        restPeriodeEnvoiMockMvc.perform(get("/api/periode-envois?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodeEnvoi.getId().intValue())))
            .andExpect(jsonPath("$.[*].expression").value(hasItem(DEFAULT_EXPRESSION)))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getPeriodeEnvoi() throws Exception {
        // Initialize the database
        periodeEnvoiRepository.saveAndFlush(periodeEnvoi);

        // Get the periodeEnvoi
        restPeriodeEnvoiMockMvc.perform(get("/api/periode-envois/{id}", periodeEnvoi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periodeEnvoi.getId().intValue()))
            .andExpect(jsonPath("$.expression").value(DEFAULT_EXPRESSION))
            .andExpect(jsonPath("$.groupeId").value(DEFAULT_GROUPE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPeriodeEnvoi() throws Exception {
        // Get the periodeEnvoi
        restPeriodeEnvoiMockMvc.perform(get("/api/periode-envois/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodeEnvoi() throws Exception {
        // Initialize the database
        periodeEnvoiRepository.saveAndFlush(periodeEnvoi);

        int databaseSizeBeforeUpdate = periodeEnvoiRepository.findAll().size();

        // Update the periodeEnvoi
        PeriodeEnvoi updatedPeriodeEnvoi = periodeEnvoiRepository.findById(periodeEnvoi.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodeEnvoi are not directly saved in db
        em.detach(updatedPeriodeEnvoi);
        updatedPeriodeEnvoi
            .expression(UPDATED_EXPRESSION)
            .groupeId(UPDATED_GROUPE_ID)
            .status(UPDATED_STATUS);
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(updatedPeriodeEnvoi);

        restPeriodeEnvoiMockMvc.perform(put("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isOk());

        // Validate the PeriodeEnvoi in the database
        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeUpdate);
        PeriodeEnvoi testPeriodeEnvoi = periodeEnvoiList.get(periodeEnvoiList.size() - 1);
        assertThat(testPeriodeEnvoi.getExpression()).isEqualTo(UPDATED_EXPRESSION);
        assertThat(testPeriodeEnvoi.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testPeriodeEnvoi.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the PeriodeEnvoi in Elasticsearch
        verify(mockPeriodeEnvoiSearchRepository, times(1)).save(testPeriodeEnvoi);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodeEnvoi() throws Exception {
        int databaseSizeBeforeUpdate = periodeEnvoiRepository.findAll().size();

        // Create the PeriodeEnvoi
        PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodeEnvoiMockMvc.perform(put("/api/periode-envois").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeEnvoiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodeEnvoi in the database
        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PeriodeEnvoi in Elasticsearch
        verify(mockPeriodeEnvoiSearchRepository, times(0)).save(periodeEnvoi);
    }

    @Test
    @Transactional
    public void deletePeriodeEnvoi() throws Exception {
        // Initialize the database
        periodeEnvoiRepository.saveAndFlush(periodeEnvoi);

        int databaseSizeBeforeDelete = periodeEnvoiRepository.findAll().size();

        // Delete the periodeEnvoi
        restPeriodeEnvoiMockMvc.perform(delete("/api/periode-envois/{id}", periodeEnvoi.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodeEnvoi> periodeEnvoiList = periodeEnvoiRepository.findAll();
        assertThat(periodeEnvoiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PeriodeEnvoi in Elasticsearch
        verify(mockPeriodeEnvoiSearchRepository, times(1)).deleteById(periodeEnvoi.getId());
    }

    @Test
    @Transactional
    public void searchPeriodeEnvoi() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        periodeEnvoiRepository.saveAndFlush(periodeEnvoi);
        when(mockPeriodeEnvoiSearchRepository.search(queryStringQuery("id:" + periodeEnvoi.getId())))
            .thenReturn(Collections.singletonList(periodeEnvoi));

        // Search the periodeEnvoi
        restPeriodeEnvoiMockMvc.perform(get("/api/_search/periode-envois?query=id:" + periodeEnvoi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodeEnvoi.getId().intValue())))
            .andExpect(jsonPath("$.[*].expression").value(hasItem(DEFAULT_EXPRESSION)))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
