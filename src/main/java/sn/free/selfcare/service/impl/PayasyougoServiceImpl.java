package sn.free.selfcare.service.impl;

import sn.free.selfcare.service.PayasyougoService;
import sn.free.selfcare.domain.Payasyougo;
import sn.free.selfcare.repository.PayasyougoRepository;
import sn.free.selfcare.repository.search.PayasyougoSearchRepository;
import sn.free.selfcare.service.dto.PayasyougoDTO;
import sn.free.selfcare.service.mapper.PayasyougoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Payasyougo}.
 */
@Service
@Transactional
public class PayasyougoServiceImpl implements PayasyougoService {

    private final Logger log = LoggerFactory.getLogger(PayasyougoServiceImpl.class);

    private final PayasyougoRepository payasyougoRepository;

    private final PayasyougoMapper payasyougoMapper;

    private final PayasyougoSearchRepository payasyougoSearchRepository;

    public PayasyougoServiceImpl(PayasyougoRepository payasyougoRepository, PayasyougoMapper payasyougoMapper, PayasyougoSearchRepository payasyougoSearchRepository) {
        this.payasyougoRepository = payasyougoRepository;
        this.payasyougoMapper = payasyougoMapper;
        this.payasyougoSearchRepository = payasyougoSearchRepository;
    }

    @Override
    public PayasyougoDTO save(PayasyougoDTO payasyougoDTO) {
        log.debug("Request to save Payasyougo : {}", payasyougoDTO);
        Payasyougo payasyougo = payasyougoMapper.toEntity(payasyougoDTO);
        payasyougo = payasyougoRepository.save(payasyougo);
        PayasyougoDTO result = payasyougoMapper.toDto(payasyougo);
        payasyougoSearchRepository.save(payasyougo);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayasyougoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payasyougos");
        return payasyougoRepository.findAll(pageable)
            .map(payasyougoMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PayasyougoDTO> findOne(Long id) {
        log.debug("Request to get Payasyougo : {}", id);
        return payasyougoRepository.findById(id)
            .map(payasyougoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payasyougo : {}", id);
        payasyougoRepository.deleteById(id);
        payasyougoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayasyougoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payasyougos for query {}", query);
        return payasyougoSearchRepository.search(queryStringQuery(query), pageable)
            .map(payasyougoMapper::toDto);
    }

    @Override
    public Optional<PayasyougoDTO> findOneByTransactionId(String transactionId) {
        log.debug("Request to get Payasyougo : {}", transactionId);
        return payasyougoRepository.findOneByTransactionId(transactionId)
            .map(payasyougoMapper::toDto);
    }
}
