package sn.free.selfcare.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sn.free.selfcare.domain.PeriodeEnvoi;
import sn.free.selfcare.domain.enumeration.ObjectStatus;
import sn.free.selfcare.repository.PeriodeEnvoiRepository;
import sn.free.selfcare.repository.search.PeriodeEnvoiSearchRepository;
import sn.free.selfcare.service.PeriodeEnvoiService;
import sn.free.selfcare.service.dto.PeriodeEnvoiDTO;
import sn.free.selfcare.service.mapper.PeriodeEnvoiMapper;
import sn.free.selfcare.service.period.PeriodTaskManager;

/**
 * Service Implementation for managing {@link PeriodeEnvoi}.
 */
@Service
@Transactional
public class PeriodeEnvoiServiceImpl implements PeriodeEnvoiService {

	private final Logger log = LoggerFactory.getLogger(PeriodeEnvoiServiceImpl.class);

	private final PeriodeEnvoiRepository periodeEnvoiRepository;

	private final PeriodeEnvoiMapper periodeEnvoiMapper;

	private final PeriodeEnvoiSearchRepository periodeEnvoiSearchRepository;

	@Autowired
	private PeriodTaskManager periodTaskManager;

	public PeriodeEnvoiServiceImpl(PeriodeEnvoiRepository periodeEnvoiRepository, PeriodeEnvoiMapper periodeEnvoiMapper,
			PeriodeEnvoiSearchRepository periodeEnvoiSearchRepository) {
		this.periodeEnvoiRepository = periodeEnvoiRepository;
		this.periodeEnvoiMapper = periodeEnvoiMapper;
		this.periodeEnvoiSearchRepository = periodeEnvoiSearchRepository;
	}

	@Override
	public PeriodeEnvoiDTO save(PeriodeEnvoiDTO periodeEnvoiDTO) {
		log.debug("Request to save PeriodeEnvoi : {}", periodeEnvoiDTO);
		PeriodeEnvoi periodeEnvoi = periodeEnvoiMapper.toEntity(periodeEnvoiDTO);
		// delete all current group's precedent PeriodeEnvoi
		List<PeriodeEnvoi> groupPeriodeEnvois = periodeEnvoiRepository.findAllByGroupeId(periodeEnvoiDTO.getGroupeId());
		if (groupPeriodeEnvois != null) {
			periodeEnvoiRepository.deleteAll(groupPeriodeEnvois);
		}
		periodeEnvoi = periodeEnvoiRepository.save(periodeEnvoi);
		PeriodeEnvoiDTO result = periodeEnvoiMapper.toDto(periodeEnvoi);
		periodeEnvoiSearchRepository.save(periodeEnvoi);

		this.periodTaskManager.createAdjustmentJob(periodeEnvoi);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PeriodeEnvoiDTO> findAll() {
		log.debug("Request to get all PeriodeEnvois");
		return periodeEnvoiRepository.findAll().stream().map(periodeEnvoiMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	@Override
	@Transactional(readOnly = true)
	public List<PeriodeEnvoi> findAllEntities() {
		log.debug("Request to get all PeriodeEnvois");
		return periodeEnvoiRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PeriodeEnvoiDTO> findOne(Long id) {
		log.debug("Request to get PeriodeEnvoi : {}", id);
		return periodeEnvoiRepository.findById(id).map(periodeEnvoiMapper::toDto);
	}

	@Override
	public void delete(Long id) {
		log.debug("Request to delete PeriodeEnvoi : {}", id);
		Optional<PeriodeEnvoi> optionalPeriod = periodeEnvoiRepository.findById(id);
		if (optionalPeriod.isPresent()) {
			PeriodeEnvoi periodeEnvoi = optionalPeriod.get();
			PeriodeEnvoiDTO periodeEnvoiDTO = periodeEnvoiMapper.toDto(periodeEnvoi);
			// procéder à la suppression logique de la période d'envoi
			periodeEnvoiDTO.setStatus(ObjectStatus.ARCHIVED);
			save(periodeEnvoiDTO);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PeriodeEnvoiDTO> search(String query) {
		log.debug("Request to search PeriodeEnvois for query {}", query);
		return StreamSupport.stream(periodeEnvoiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.map(periodeEnvoiMapper::toDto).collect(Collectors.toList());
	}
}
