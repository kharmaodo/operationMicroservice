package sn.free.selfcare.web.rest;

import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;
import sn.free.selfcare.service.dto.AdjustmentDTO;

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
 * REST controller for managing {@link sn.free.selfcare.domain.Adjustment}.
 */
@RestController
@RequestMapping("/api")
public class AdjustmentResource {

    private final Logger log = LoggerFactory.getLogger(AdjustmentResource.class);

    private static final String ENTITY_NAME = "operationMicroserviceAdjustment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdjustmentService adjustmentService;

    public AdjustmentResource(AdjustmentService adjustmentService) {
        this.adjustmentService = adjustmentService;
    }

    /**
     * {@code POST  /adjustments} : Create a new adjustment.
     *
     * @param adjustmentDTO the adjustmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adjustmentDTO, or with status {@code 400 (Bad Request)} if the adjustment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adjustments")
    public ResponseEntity<AdjustmentDTO> createAdjustment(@Valid @RequestBody AdjustmentDTO adjustmentDTO) throws URISyntaxException {
        log.debug("REST request to save Adjustment : {}", adjustmentDTO);
        if (adjustmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new adjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdjustmentDTO result = adjustmentService.save(adjustmentDTO);
        return ResponseEntity.created(new URI("/api/adjustments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adjustments} : Updates an existing adjustment.
     *
     * @param adjustmentDTO the adjustmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adjustmentDTO,
     * or with status {@code 400 (Bad Request)} if the adjustmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adjustmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adjustments")
    public ResponseEntity<AdjustmentDTO> updateAdjustment(@Valid @RequestBody AdjustmentDTO adjustmentDTO) throws URISyntaxException {
        log.debug("REST request to update Adjustment : {}", adjustmentDTO);
        if (adjustmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdjustmentDTO result = adjustmentService.save(adjustmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adjustmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /adjustments} : get all the adjustments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adjustments in body.
     */
    @GetMapping("/adjustments")
    public List<AdjustmentDTO> getAllAdjustments() {
        log.debug("REST request to get all Adjustments");
        return adjustmentService.findAll();
    }

    /**
     * {@code GET  /adjustments/:id} : get the "id" adjustment.
     *
     * @param id the id of the adjustmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adjustmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adjustments/{id}")
    public ResponseEntity<AdjustmentDTO> getAdjustment(@PathVariable Long id) {
        log.debug("REST request to get Adjustment : {}", id);
        Optional<AdjustmentDTO> adjustmentDTO = adjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adjustmentDTO);
    }

    /**
     * {@code DELETE  /adjustments/:id} : delete the "id" adjustment.
     *
     * @param id the id of the adjustmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adjustments/{id}")
    public ResponseEntity<Void> deleteAdjustment(@PathVariable Long id) {
        log.debug("REST request to delete Adjustment : {}", id);
        adjustmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/adjustments?query=:query} : search for the adjustment corresponding
     * to the query.
     *
     * @param query the query of the adjustment search.
     * @return the result of the search.
     */
    @GetMapping("/_search/adjustments")
    public List<AdjustmentDTO> searchAdjustments(@RequestParam String query) {
        log.debug("REST request to search Adjustments for query {}", query);
        return adjustmentService.search(query);
    }
}
