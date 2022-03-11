package sn.free.selfcare.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.service.PayasyougoService;
import sn.free.selfcare.service.dto.PayasyougoDTO;
import sn.free.selfcare.service.dto.PaymentRequestDTO;
import sn.free.selfcare.service.impl.PayasyougoPaymentMethodImpl;
import sn.free.selfcare.service.mapper.PaymentRequestMapper;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.Payasyougo}.
 */
@RestController
@RequestMapping("/api")
public class PayasyougoResource {

    private static final String ENTITY_NAME = "operationMicroservicePayasyougo";
    private final Logger log = LoggerFactory.getLogger(PayasyougoResource.class);
    private final PayasyougoService payasyougoService;
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    PayasyougoPaymentMethodImpl paymentMethod;

    @Autowired
    PaymentRequestMapper paymentRequestMapper;

    public PayasyougoResource(ApplicationProperties applicationProperties, PayasyougoService payasyougoService, @Qualifier("restTemplate") RestTemplate restTemplate) {
        this.applicationProperties = applicationProperties;
        this.payasyougoService = payasyougoService;
        this.restTemplate = restTemplate;
    }

    /**
     * {@code POST  /payasyougos} : Create a new payasyougo.
     *
     * @param payasyougoDTO the payasyougoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payasyougoDTO, or with status {@code 400 (Bad Request)} if the payasyougo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payasyougos")
    public ResponseEntity<PayasyougoDTO> createPayasyougo(@Valid @RequestBody PayasyougoDTO payasyougoDTO) throws URISyntaxException {
        log.debug("REST request to save Payasyougo : {}", payasyougoDTO);
        if (payasyougoDTO.getId() != null) {
            throw new BadRequestAlertException("A new payasyougo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayasyougoDTO result = payasyougoService.save(payasyougoDTO);
        return ResponseEntity.created(new URI("/api/payasyougos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payasyougos} : Updates an existing payasyougo.
     *
     * @param payasyougoDTO the payasyougoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payasyougoDTO,
     * or with status {@code 400 (Bad Request)} if the payasyougoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payasyougoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payasyougos")
    public ResponseEntity<PayasyougoDTO> updatePayasyougo(@Valid @RequestBody PayasyougoDTO payasyougoDTO) throws URISyntaxException {
        log.debug("REST request to update Payasyougo : {}", payasyougoDTO);
        if (payasyougoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayasyougoDTO result = payasyougoService.save(payasyougoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payasyougoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payasyougos} : get all the payasyougos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payasyougos in body.
     */
    @GetMapping("/payasyougos")
    public ResponseEntity<List<PayasyougoDTO>> getAllPayasyougos(Pageable pageable) {
        log.debug("REST request to get a page of Payasyougos");
        Page<PayasyougoDTO> page = payasyougoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payasyougos/:id} : get the "id" payasyougo.
     *
     * @param id the id of the payasyougoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payasyougoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payasyougos/{id}")
    public ResponseEntity<PayasyougoDTO> getPayasyougo(@PathVariable Long id) {
        log.debug("REST request to get Payasyougo : {}", id);
        Optional<PayasyougoDTO> payasyougoDTO = payasyougoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payasyougoDTO);
    }

    /**
     * {@code DELETE  /payasyougos/:id} : delete the "id" payasyougo.
     *
     * @param id the id of the payasyougoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payasyougos/{id}")
    public ResponseEntity<Void> deletePayasyougo(@PathVariable Long id) {
        log.debug("REST request to delete Payasyougo : {}", id);
        payasyougoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payasyougos?query=:query} : search for the payasyougo corresponding
     * to the query.
     *
     * @param query    the query of the payasyougo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payasyougos")
    public ResponseEntity<List<PayasyougoDTO>> searchPayasyougos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Payasyougos for query {}", query);
        Page<PayasyougoDTO> page = payasyougoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/payasyougos/pay")
    public ResponseEntity<PayasyougoDTO> createPayasyougoPaymentRequest(@Valid @RequestBody PayasyougoDTO payasyougoDTO) {

        log.debug("REST request to save createPayasyougoPaymentRequest : {}", payasyougoDTO);

        // Map payasyougo object to a paymentRequest object
        PaymentRequestDTO paymentRequestDTO = paymentRequestMapper.payasyougoToPaymentRequest(payasyougoDTO);

        try {
            // Invoking correct payment method
            Map responseMap = paymentMethod.pay(paymentRequestDTO);
            if(responseMap != null) {
                String status = (String) responseMap.get("status");
                if (!ActionStatus.FAILED.toString().equals(status)) {
                    payasyougoDTO.setDatePaiement(Instant.now());
                    payasyougoDTO.setTrials(1);
                    payasyougoDTO.setPaiementStatus(ActionStatus.INPROGRESS);
                    PayasyougoDTO result = payasyougoService.save(payasyougoDTO);
                    return ResponseEntity.created(new URI("/api/payasyougos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } else {
                    String errorCode = (String) responseMap.get("errorCode");
                    throw new BadRequestAlertException("Payment request failed!", "adjustFailure", "payasyougo." + errorCode);
                }
            } else {
                throw new BadRequestAlertException("Payment request failed!", "adjustFailure", "payasyougo.badUrlFormat");
            }
        } catch (Exception e) {
        	//TODO : MD : Audit : Mettre des loggueurs et non des printStackTrace
            e.printStackTrace();
            if (e instanceof URISyntaxException) {
                throw new BadRequestAlertException("Payment request failed!", "adjustFailure", "payasyougo.badUrlFormat");
            } else {
                throw new BadRequestAlertException("Payment request failed!", "adjustFailure", "payasyougo.adjustFailure");
            }
        }
    }
}
