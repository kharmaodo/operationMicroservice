package sn.free.selfcare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.domain.Facture;
import sn.free.selfcare.repository.FactureRepository;
import sn.free.selfcare.repository.search.FactureSearchRepository;
import sn.free.selfcare.service.FactureService;
import sn.free.selfcare.service.FileTransferService;
import sn.free.selfcare.service.dto.AmountPaidBillKpiDTO;
import sn.free.selfcare.service.dto.FactureDTO;
import sn.free.selfcare.service.dto.NumberPaidBillKpiDTO;
import sn.free.selfcare.service.mapper.FactureMapper;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    private final FactureSearchRepository factureSearchRepository;

    private final ApplicationProperties applicationProperties;

    @Autowired
    private FileTransferService fileTransferService;

    public FactureServiceImpl(FactureRepository factureRepository, FactureMapper factureMapper, FactureSearchRepository factureSearchRepository, ApplicationProperties applicationProperties) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.factureSearchRepository = factureSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public FactureDTO save(FactureDTO factureDTO) {
        final long start = System.currentTimeMillis();
        log.debug("Request to save Facture : {}", factureDTO);
        if (factureDTO.getPath() == null) {
            factureDTO.setPath("");
        }

        Facture facture = factureMapper.toEntity(factureDTO);
        facture = factureRepository.save(facture);
        FactureDTO result = factureMapper.toDto(facture);
        factureSearchRepository.save(facture);

        log.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDTO> findAll() {
        log.debug("Request to get all Factures");
        return factureRepository.findAll().stream()
            .map(factureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FactureDTO> findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findById(id)
            .map(factureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
        factureSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureDTO> search(String query) {
        log.debug("Request to search Factures for query {}", query);
        return StreamSupport
            .stream(factureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(factureMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactureDTO> findFacturesByClientAndQuery(String clientId, String query, Pageable pageable) {

        Page<Facture> page;
        if (query != null && !query.isEmpty()) {
            page = factureRepository.findFacturesByClientAndQuery(clientId, "%" + query + "%", pageable);
        } else {
            page = factureRepository.findFacturesByClient(clientId, pageable);
        }
        return page.map(factureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NumberPaidBillKpiDTO> getNumberOfPaidBillsPerMonth(int year) {
        List<NumberPaidBillKpiDTO> kpiDTOs = new ArrayList<>();
        for (Month month : Month.values()) {
            NumberPaidBillKpiDTO kpiDTO = new NumberPaidBillKpiDTO();
            kpiDTO.setMonthNumber(month.getValue());
            kpiDTO.setMonthName(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            kpiDTO.setNumberOfPaidBills(factureRepository.getNumberOfPaidBillsPerMonth(year, month.getValue()));
            kpiDTOs.add(kpiDTO);
        }
        return kpiDTOs.stream().sorted().collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmountPaidBillKpiDTO> getAmountOfPaidBillsPerMonth(int year) {
        List<AmountPaidBillKpiDTO> kpiDTOs = new ArrayList<>();
        for (Month month : Month.values()) {
            AmountPaidBillKpiDTO kpiDTO = new AmountPaidBillKpiDTO();
            kpiDTO.setMonthNumber(month.getValue());
            kpiDTO.setMonthName(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            kpiDTO.setAmountOfPaidBills(factureRepository.getAmountOfPaidBillsPerMonth(year, month.getValue()));
            kpiDTOs.add(kpiDTO);
        }
        return kpiDTOs.stream().sorted().collect(Collectors.toList());
    }
}
