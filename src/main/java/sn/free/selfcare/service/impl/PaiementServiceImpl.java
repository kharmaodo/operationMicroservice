package sn.free.selfcare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.free.selfcare.domain.Paiement;
import sn.free.selfcare.repository.PaiementRepository;
import sn.free.selfcare.repository.search.PaiementSearchRepository;
import sn.free.selfcare.service.PaiementService;
import sn.free.selfcare.service.dto.CreditValuePurchasedKpiDTO;
import sn.free.selfcare.service.dto.PaiementDTO;
import sn.free.selfcare.service.mapper.PaiementMapper;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementServiceImpl.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    private final PaiementSearchRepository paiementSearchRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository, PaiementMapper paiementMapper, PaiementSearchRepository paiementSearchRepository) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
        this.paiementSearchRepository = paiementSearchRepository;
    }

    @Override
    public PaiementDTO save(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement = paiementRepository.save(paiement);
        PaiementDTO result = paiementMapper.toDto(paiement);
        paiementSearchRepository.save(paiement);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAll() {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll().stream()
            .map(paiementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the paiements where Adjustment is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAllWhereAdjustmentIsNull() {
        log.debug("Request to get all paiements where Adjustment is null");
        return StreamSupport
            .stream(paiementRepository.findAll().spliterator(), false)
            .filter(paiement -> paiement.getAdjustment() == null)
            .map(paiementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementDTO> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id)
            .map(paiementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
        paiementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> search(String query) {
        log.debug("Request to search Paiements for query {}", query);
        return StreamSupport
            .stream(paiementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(paiementMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<PaiementDTO> findOneByTransactionId(String transactionId) {
        log.debug("Request to get Paiement : {}", transactionId);
        return paiementRepository.findOneByTransactionId(transactionId)
            .map(paiementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditValuePurchasedKpiDTO> getCreditValuePurchasedPerMonth(int year) {
        List<CreditValuePurchasedKpiDTO> kpiDTOs = new ArrayList<>();
        for (Month month : Month.values()) {
            CreditValuePurchasedKpiDTO kpiDTO = new CreditValuePurchasedKpiDTO();
            kpiDTO.setMonthNumber(month.getValue());
            kpiDTO.setMonthName(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            kpiDTO.setCreditValuePurchased(paiementRepository.getCreditValuePurchasedPerMonth(year, month.getValue()));
            kpiDTOs.add(kpiDTO);
        }
        return kpiDTOs.stream().sorted().collect(Collectors.toList());
    }
}
