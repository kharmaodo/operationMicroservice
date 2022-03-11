package sn.free.selfcare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.free.selfcare.domain.SeuilEmploye;
import sn.free.selfcare.repository.SeuilEmployeRepository;
import sn.free.selfcare.service.SeuilEmployeService;
import sn.free.selfcare.service.dto.SeuilEmployeDTO;
import sn.free.selfcare.service.mapper.SeuilEmployeMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link SeuilEmploye}.
 */
@Service
@Transactional
public class SeuilEmployeServiceImpl implements SeuilEmployeService {

    private final Logger log = LoggerFactory.getLogger(SeuilEmployeServiceImpl.class);

    private final SeuilEmployeRepository seuilEmployeRepository;

    private final SeuilEmployeMapper seuilEmployeMapper;

    public SeuilEmployeServiceImpl(SeuilEmployeRepository seuilEmployeRepository, SeuilEmployeMapper seuilEmployeMapper) {
        this.seuilEmployeRepository = seuilEmployeRepository;
        this.seuilEmployeMapper = seuilEmployeMapper;
    }

    @Override
    public SeuilEmployeDTO save(SeuilEmployeDTO seuilEmployeDTO) {
        log.debug("Request to save SeuilEmploye : {}", seuilEmployeDTO);
        SeuilEmploye seuilEmploye = seuilEmployeMapper.toEntity(seuilEmployeDTO);
        seuilEmploye = seuilEmployeRepository.save(seuilEmploye);
        SeuilEmployeDTO result = seuilEmployeMapper.toDto(seuilEmploye);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeuilEmployeDTO> findAll() {
        log.debug("Request to get all Lignes");
        return seuilEmployeRepository.findAll().stream()
            .map(seuilEmployeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the seuilEmployes of alert.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SeuilEmployeDTO> findAllByAlertId(Long alertId) {
        log.debug("Request to get all seuilEmployes of alert {}", alertId);
        return StreamSupport
            .stream(seuilEmployeRepository.findAllByAlertId(alertId).spliterator(), false)
            .map(seuilEmployeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeuilEmployeDTO> findOne(Long id) {
        log.debug("Request to get SeuilEmploye : {}", id);
        return seuilEmployeRepository.findById(id)
            .map(seuilEmployeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SeuilEmploye : {}", id);
        seuilEmployeRepository.deleteById(id);
    }

}
