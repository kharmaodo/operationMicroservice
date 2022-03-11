package sn.free.selfcare.web.rest;

import com.jcraft.jsch.SftpException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.service.FactureService;
import sn.free.selfcare.service.FileTransferService;
import sn.free.selfcare.service.dto.FactureDTO;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.Facture}.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private static final String ENTITY_NAME = "operationMicroserviceFacture";
    private final Logger log = LoggerFactory.getLogger(FactureResource.class);
    private final FactureService factureService;
    private final FileTransferService fileTransferService;
    private final ApplicationProperties applicationProperties;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public FactureResource(FactureService factureService, FileTransferService fileTransferService, ApplicationProperties applicationProperties) {
        this.factureService = factureService;
        this.fileTransferService = fileTransferService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param factureDTO the factureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factureDTO, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factures")
    public ResponseEntity<FactureDTO> createFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.info("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures} : Updates an existing facture.
     *
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureDTO,
     * or with status {@code 400 (Bad Request)} if the factureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factures")
    public ResponseEntity<FactureDTO> updateFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("/factures")
    public List<FactureDTO> getAllFactures() {
        log.debug("REST request to get all Factures");
        return factureService.findAll();
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Optional<FactureDTO> factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/factures?query=:query} : search for the facture corresponding
     * to the query.
     *
     * @param query the query of the facture search.
     * @return the result of the search.
     */
    @GetMapping("/_search/factures")
    public List<FactureDTO> searchFactures(@RequestParam String query) {
        log.debug("REST request to search Factures for query {}", query);
        return factureService.search(query);
    }

    @GetMapping("/client/{clientId}/factures")
    public ResponseEntity<List<FactureDTO>> getFacturesByClientAndQuery(@PathVariable String clientId, @RequestParam(required = false) String query, Pageable pageable) {
        log.debug("REST request to search Factures for query {} for clientId {}", query, clientId);
        Page<FactureDTO> page = factureService.findFacturesByClientAndQuery(clientId, query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factures/{factureId}/download} : download invoice file.
     *
     * @param id the factureId.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the incoive file in body.
     */
    @GetMapping("/factures/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        try {
            log.debug("Downloading facture id : {}", id);
            Optional<FactureDTO> optionalFactureDTO = factureService.findOne(id);
            if (!optionalFactureDTO.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            String factureRootPah = applicationProperties.getFactureConf().getRootPath();
            log.debug("factureRootPah = {}", factureRootPah);

            FactureDTO factureDTO = optionalFactureDTO.get();
            log.debug("factureDTO = {}", factureDTO);

            String path = factureDTO.getPath();
            Path fullPath = Paths.get(factureRootPah, path);

            String filename = fullPath.toFile().getName();
            log.debug("filename = {}", filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-disposition", "attachment; filename=" + filename);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).body(fileTransferService.downloadFromFtp(path).get());

        } catch (SftpException | IOException ex) {
            log.error("Error download file", ex);
            throw new BadRequestAlertException("Invoice download failed!", ENTITY_NAME, ENTITY_NAME + ".fileUnfound");
        } catch (InterruptedException | ExecutionException ex) {
            log.error("Error download file", ex);
            throw new BadRequestAlertException("Invoice download failed!", ENTITY_NAME, ENTITY_NAME + ".downloadInterrupted");
        } catch (Exception ex) {
            log.error("Error download file", ex);
            throw new BadRequestAlertException("Invoice download failed!", ENTITY_NAME, ENTITY_NAME + ".downloadFailed");
        }
    }

    /**
     * {@code POST  /factures/upload} : upload invoice file.
     *
     * @param id the factureId.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     * incoive file in body.
     */
    @PostMapping("/factures/uploadFile")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        if (fileTransferService.uploadFile(file, path)) {
            return ResponseEntity.ok().body(Boolean.TRUE);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
    }
}
