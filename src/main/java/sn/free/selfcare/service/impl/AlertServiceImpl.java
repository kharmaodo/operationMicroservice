package sn.free.selfcare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.free.selfcare.domain.Alert;
import sn.free.selfcare.domain.enumeration.ObjectStatus;
import sn.free.selfcare.repository.AlertRepository;
import sn.free.selfcare.repository.search.AlertSearchRepository;
import sn.free.selfcare.service.AlertService;
import sn.free.selfcare.service.SeuilEmployeService;
import sn.free.selfcare.service.alert.AlertTaskManager;
import sn.free.selfcare.service.dto.AlertDTO;
import sn.free.selfcare.service.dto.SeuilEmployeDTO;
import sn.free.selfcare.service.mapper.AlertMapper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Alert}.
 */
@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    private final AlertSearchRepository alertSearchRepository;

    @Autowired
    private SeuilEmployeService seuilEmployeService;

    @Autowired
    private AlertTaskManager alertTaskManager;

    public AlertServiceImpl(AlertRepository alertRepository, AlertMapper alertMapper, AlertSearchRepository alertSearchRepository) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
        this.alertSearchRepository = alertSearchRepository;
    }

    @Override
    public AlertDTO save(AlertDTO alertDTO) {
        log.debug("Request to save Alert : {}", alertDTO);
        final Long alertDTOId = alertDTO.getId();
        if (alertDTOId != null) {
	        // delete all seuilEmployes linked to this alert
	        List<SeuilEmployeDTO> oldAlertSeuilEmployes = this.seuilEmployeService.findAllByAlertId(alertDTOId);
	        oldAlertSeuilEmployes.forEach(seuilEmploye -> {
				this.seuilEmployeService.delete(seuilEmploye.getId());
			});
        }
        Set<SeuilEmployeDTO> newAlertSeuilEmployes = alertDTO.getSeuilEmployes();
        // reset all seuil employe set linked to this alert
        alertDTO.setSeuilEmployes(new HashSet<SeuilEmployeDTO>());
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        final Long alertId = alert.getId();

        // link new seuilEmployes to this alert
        newAlertSeuilEmployes.forEach(seuilEmploye -> {
        	seuilEmploye.setId(null);
        	seuilEmploye.setAlertId(alertId);
        	this.seuilEmployeService.save(seuilEmploye);
        });
        // reload saved alert
        Optional<Alert> optionalAlert = alertRepository.findById(alertId);
        if (optionalAlert.isPresent()) {
        	alert = optionalAlert.get();
        }
        AlertDTO result = alertMapper.toDto(alert);
        alertSearchRepository.save(alert);

        this.alertTaskManager.createAlertJob(result);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertDTO> findAll() {
        log.debug("Request to get all Lignes");
        return alertRepository.findAll().stream()
            .map(alertMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the alerts of client.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlertDTO> findAllByClientId(Long clientId) {
        log.debug("Request to get all alerts of client {}", clientId);
        return StreamSupport
            .stream(alertRepository.findAllByClientId(clientId).spliterator(), false)
            .map(alertMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the alerts of offre.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlertDTO> findAllByOffreId(Long offreId) {
        log.debug("Request to get all alerts of offre {}", offreId);
        return StreamSupport
            .stream(alertRepository.findAllByOffreId(offreId).spliterator(), false)
            .map(alertMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertDTO> findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        return alertRepository.findById(id)
            .map(alertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        Optional<Alert> optionalAlert = alertRepository.findById(id);
        if (optionalAlert.isPresent()) {
        	Alert alert = optionalAlert.get();
        	// Procéder à la suppression logique
        	AlertDTO alertDTO = alertMapper.toDto(alert);
        	Set<SeuilEmployeDTO> seuilEmployesDTO = alertDTO.getSeuilEmployes();
        	seuilEmployesDTO.parallelStream().forEach(seuilEmployeDTO -> {
        		seuilEmployeDTO.setStatus(ObjectStatus.ARCHIVED);
            });
            // Procéder à la suppression logique de l'alerte
        	alertDTO.setStatus(ObjectStatus.ARCHIVED);
        	save(alertDTO);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertDTO> search(String query) {
        log.debug("Request to search Alerts for query {}", query);
        return StreamSupport
            .stream(alertSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(alertMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteClientAlerts(Long clientId) {
        List<Alert> clientAlerts = alertRepository.findAllByClientId(clientId);
        clientAlerts.parallelStream().forEach(alert -> {
            delete(alert.getId());
        });
    }

}
