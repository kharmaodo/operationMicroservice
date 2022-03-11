package sn.free.selfcare.web.rest;

import sn.free.selfcare.service.PeriodeEnvoiService;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;
import sn.free.selfcare.service.dto.PeriodeEnvoiDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.PeriodeEnvoi}.
 */
@RestController
@RequestMapping("/api")
public class PeriodeEnvoiResource {

    private final Logger log = LoggerFactory.getLogger(PeriodeEnvoiResource.class);

    private static final String ENTITY_NAME = "operationMicroservicePeriodeEnvoi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodeEnvoiService periodeEnvoiService;

    public PeriodeEnvoiResource(PeriodeEnvoiService periodeEnvoiService) {
        this.periodeEnvoiService = periodeEnvoiService;
    }

    /**
     * {@code POST  /periode-envois} : Create a new periodeEnvoi.
     *
     * @param periodeEnvoiDTO the periodeEnvoiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodeEnvoiDTO, or with status {@code 400 (Bad Request)} if the periodeEnvoi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periode-envois")
    public ResponseEntity<PeriodeEnvoiDTO> createPeriodeEnvoi(@Valid @RequestBody PeriodeEnvoiDTO periodeEnvoiDTO) throws URISyntaxException {
        log.debug("REST request to save PeriodeEnvoi : {}", periodeEnvoiDTO);
        if (periodeEnvoiDTO.getId() != null) {
            throw new BadRequestAlertException("A new periodeEnvoi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodeEnvoiDTO result = periodeEnvoiService.save(periodeEnvoiDTO);
        return ResponseEntity.created(new URI("/api/periode-envois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /periode-envois} : Updates an existing periodeEnvoi.
     *
     * @param periodeEnvoiDTO the periodeEnvoiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodeEnvoiDTO,
     * or with status {@code 400 (Bad Request)} if the periodeEnvoiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodeEnvoiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periode-envois")
    public ResponseEntity<PeriodeEnvoiDTO> updatePeriodeEnvoi(@Valid @RequestBody PeriodeEnvoiDTO periodeEnvoiDTO) throws URISyntaxException {
        log.debug("REST request to update PeriodeEnvoi : {}", periodeEnvoiDTO);
        if (periodeEnvoiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodeEnvoiDTO result = periodeEnvoiService.save(periodeEnvoiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodeEnvoiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /periode-envois} : get all the periodeEnvois.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodeEnvois in body.
     */
    @GetMapping("/periode-envois")
    public List<PeriodeEnvoiDTO> getAllPeriodeEnvois() {
        log.debug("REST request to get all PeriodeEnvois");
        return periodeEnvoiService.findAll();
    }

    /**
     * {@code GET  /periode-envois/:id} : get the "id" periodeEnvoi.
     *
     * @param id the id of the periodeEnvoiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodeEnvoiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periode-envois/{id}")
    public ResponseEntity<PeriodeEnvoiDTO> getPeriodeEnvoi(@PathVariable Long id) {
        log.debug("REST request to get PeriodeEnvoi : {}", id);
        Optional<PeriodeEnvoiDTO> periodeEnvoiDTO = periodeEnvoiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodeEnvoiDTO);
    }

    /**
     * {@code DELETE  /periode-envois/:id} : delete the "id" periodeEnvoi.
     *
     * @param id the id of the periodeEnvoiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periode-envois/{id}")
    public ResponseEntity<Void> deletePeriodeEnvoi(@PathVariable Long id) {
        log.debug("REST request to delete PeriodeEnvoi : {}", id);
        periodeEnvoiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/periode-envois?query=:query} : search for the periodeEnvoi corresponding
     * to the query.
     *
     * @param query the query of the periodeEnvoi search.
     * @return the result of the search.
     */
    @GetMapping("/_search/periode-envois")
    public List<PeriodeEnvoiDTO> searchPeriodeEnvois(@RequestParam String query) {
        log.debug("REST request to search PeriodeEnvois for query {}", query);
        return periodeEnvoiService.search(query);
    }
}
