package sn.free.selfcare.service.impl;

import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.domain.Adjustment;
import sn.free.selfcare.repository.AdjustmentRepository;
import sn.free.selfcare.repository.search.AdjustmentSearchRepository;
import sn.free.selfcare.service.dto.AdjustmentDTO;
import sn.free.selfcare.service.mapper.AdjustmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Adjustment}.
 */
@Service
@Transactional
public class AdjustmentServiceImpl implements AdjustmentService {

    private final Logger log = LoggerFactory.getLogger(AdjustmentServiceImpl.class);

    private final AdjustmentRepository adjustmentRepository;

    private final AdjustmentMapper adjustmentMapper;

    private final AdjustmentSearchRepository adjustmentSearchRepository;

    public AdjustmentServiceImpl(AdjustmentRepository adjustmentRepository, AdjustmentMapper adjustmentMapper, AdjustmentSearchRepository adjustmentSearchRepository) {
        this.adjustmentRepository = adjustmentRepository;
        this.adjustmentMapper = adjustmentMapper;
        this.adjustmentSearchRepository = adjustmentSearchRepository;
    }

    @Override
    public AdjustmentDTO save(AdjustmentDTO adjustmentDTO) {
        log.debug("Request to save Adjustment : {}", adjustmentDTO);
        Adjustment adjustment = adjustmentMapper.toEntity(adjustmentDTO);
        adjustment = adjustmentRepository.save(adjustment);
        AdjustmentDTO result = adjustmentMapper.toDto(adjustment);
        adjustmentSearchRepository.save(adjustment);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdjustmentDTO> findAll() {
        log.debug("Request to get all Adjustments");
        return adjustmentRepository.findAll().stream()
            .map(adjustmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AdjustmentDTO> findOne(Long id) {
        log.debug("Request to get Adjustment : {}", id);
        return adjustmentRepository.findById(id)
            .map(adjustmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Adjustment : {}", id);
        adjustmentRepository.deleteById(id);
        adjustmentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdjustmentDTO> search(String query) {
        log.debug("Request to search Adjustments for query {}", query);
        return StreamSupport
            .stream(adjustmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(adjustmentMapper::toDto)
        .collect(Collectors.toList());
    }
}
