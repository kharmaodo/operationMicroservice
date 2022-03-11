package sn.free.selfcare.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sn.free.selfcare.apiclient.service.AdjustAccountService;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.domain.enumeration.AdjustmentType;
import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.service.FactureService;
import sn.free.selfcare.service.PaiementService;
import sn.free.selfcare.service.PayasyougoService;
import sn.free.selfcare.service.dto.*;
import sn.free.selfcare.service.impl.InvoicePaymentMethodImpl;
import sn.free.selfcare.service.impl.PayasyougoPaymentMethodImpl;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.Paiement}.
 */
@RestController
@RequestMapping("/api")
public class PaiementResource {

    private static final String ENTITY_NAME = "operationMicroservicePaiement";
    private final Logger log = LoggerFactory.getLogger(PaiementResource.class);
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;
    private final FactureService factureService;
    private final PaiementService paiementService;
    @Autowired
    PayasyougoService payasyougoService;
    @Autowired
    AdjustAccountService adjustAccountService;
    @Autowired
    AdjustmentService adjustmentService;

    @Autowired
    InvoicePaymentMethodImpl invoicePaymentMethod;

    @Autowired
    PayasyougoPaymentMethodImpl payasyougoPaymentMethod;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${services.adjustAccount.url}")
    private String adjustAccountURL;

    public PaiementResource(ApplicationProperties applicationProperties, FactureService factureService, PaiementService paiementService, @Qualifier("restTemplate") RestTemplate restTemplate) {
        this.applicationProperties = applicationProperties;
        this.factureService = factureService;
        this.paiementService = paiementService;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        log.info("Init -> Start");

        log.info("adjustAccountURL = {}", adjustAccountURL);
        this.adjustAccountService.setAdjustAccountURL(adjustAccountURL);

        log.info("Init -> End");
    }

    /**
     * {@code POST  /paiements} : Create a new paiement.
     *
     * @param paiementDTO the paiementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementDTO, or with status {@code 400 (Bad Request)} if the paiement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiements")
    public ResponseEntity<PaiementDTO> createPaiement(@Valid @RequestBody PaiementDTO paiementDTO) throws URISyntaxException {
        log.info("REST request to save Paiement : {}", paiementDTO);
        if (paiementDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementDTO result = paiementService.save(paiementDTO);
        return ResponseEntity.created(new URI("/api/paiements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiements} : Updates an existing paiement.
     *
     * @param paiementDTO the paiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementDTO,
     * or with status {@code 400 (Bad Request)} if the paiementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiements")
    public ResponseEntity<PaiementDTO> updatePaiement(@Valid @RequestBody PaiementDTO paiementDTO) throws URISyntaxException {
        log.info("REST request to update Paiement : {}", paiementDTO);
        if (paiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaiementDTO result = paiementService.save(paiementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paiements} : get all the paiements.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiements in body.
     */
    @GetMapping("/paiements")
    public List<PaiementDTO> getAllPaiements(@RequestParam(required = false) String filter) {
        if ("adjustment-is-null".equals(filter)) {
            log.debug("REST request to get all Paiements where adjustment is null");
            return paiementService.findAllWhereAdjustmentIsNull();
        }
        log.debug("REST request to get all Paiements");
        return paiementService.findAll();
    }

    /**
     * {@code GET  /paiements/:id} : get the "id" paiement.
     *
     * @param id the id of the paiementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiements/{id}")
    public ResponseEntity<PaiementDTO> getPaiement(@PathVariable Long id) {
        log.debug("REST request to get Paiement : {}", id);
        Optional<PaiementDTO> paiementDTO = paiementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementDTO);
    }

    /**
     * {@code DELETE  /paiements/:id} : delete the "id" paiement.
     *
     * @param id the id of the paiementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiements/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        log.debug("REST request to delete Paiement : {}", id);
        paiementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/paiements?query=:query} : search for the paiement corresponding
     * to the query.
     *
     * @param query the query of the paiement search.
     * @return the result of the search.
     */
    @GetMapping("/_search/paiements")
    public List<PaiementDTO> searchPaiements(@RequestParam String query) {
        log.debug("REST request to search Paiements for query {}", query);
        return paiementService.search(query);
    }


    /**
     * {@code POST  /invoicePaymentRequest} : Create a new payment request.
     *
     * @param paymentRequestDTO the paymentRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentRequestDTO, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoicePaymentRequest/{factureId}")
    public ResponseEntity<PaiementDTO> createPaymentRequest(@PathVariable Long factureId, @Valid @RequestBody PaymentRequestDTO paymentRequestDTO) throws URISyntaxException {
        log.info("REST request to save Payment request : {}", paymentRequestDTO);
        Map responseMap = invoicePaymentMethod.pay(paymentRequestDTO);
        if (responseMap != null) {
            String status = (String) responseMap.get("status");
            if (!ActionStatus.FAILED.toString().equals(status)) {
                PaiementDTO paiementDTO = new PaiementDTO();
                paiementDTO.setClientId(paymentRequestDTO.getClientId());
                paiementDTO.setClientCode(paymentRequestDTO.getCustomerAccount());
                paiementDTO.setNumeroVirtuel(paymentRequestDTO.getNumeroVirtuel());
                paiementDTO.setNumeroFreemoney(paymentRequestDTO.getCustomermsisdn());
                paiementDTO.setFactureId(factureId);
                paiementDTO.setTransactionId(paymentRequestDTO.getExternaltransactionid());
                paiementDTO.setAmount(paymentRequestDTO.getAmount());
                paiementDTO.setDatePaiement(Instant.now());
                paiementDTO.setTrials(1);
                paiementDTO.setPaiementStatus(ActionStatus.INPROGRESS);

//                paiementDTO.setExternalTransactionId(transactionid);
                paiementDTO.setExternalTransactionId(extractExternalTransationId(responseMap));

                PaiementDTO result = paiementService.save(paiementDTO);
                Optional<FactureDTO> optionalFactureDTO = factureService.findOne(factureId);
                if (optionalFactureDTO.isPresent()) {
                    FactureDTO factureDTO = optionalFactureDTO.get();
                    factureDTO.setPaiementStatus(ActionStatus.INPROGRESS);
                    factureService.save(factureDTO);
                }
                return ResponseEntity.created(new URI("/api/paiements/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } else {
                throw new BadRequestAlertException("Payment request failed", ENTITY_NAME, "paymentRequestFailed");
            }
        } else {
            throw new BadRequestAlertException("Payment request failed", ENTITY_NAME, "paymentRequestFailed");
        }
    }

    @PostMapping("/paiements/payasyougo")
    public ResponseEntity<PaiementDTO> createPayasyougoPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        log.info("REST request to save Payment request : {}", paymentRequestDTO);
        try {
            Map responseMap = payasyougoPaymentMethod.pay(paymentRequestDTO);
            if (responseMap != null) {
                String status = (String) responseMap.get("status");
//                String transactionid = (String) responseMap.get("transactionid");
//                log.info("eeee transactionid = {}", transactionid);
                if (!ActionStatus.FAILED.toString().equals(status)) {

                    AdjustmentDTO adjustmentDTO = adjustmentService.save(prepareAdjustmentDTO(paymentRequestDTO));

                    PaiementDTO paiementDTO = new PaiementDTO();
                    paiementDTO.setClientId(paymentRequestDTO.getClientId());
                    paiementDTO.setClientCode(paymentRequestDTO.getCustomerAccount());
                    paiementDTO.setNumeroVirtuel(paymentRequestDTO.getNumeroVirtuel());
                    paiementDTO.setNumeroFreemoney(paymentRequestDTO.getCustomermsisdn());
                    paiementDTO.setTransactionId(paymentRequestDTO.getExternaltransactionid());
                    paiementDTO.setAmount(paymentRequestDTO.getAmount());
                    paiementDTO.setDatePaiement(Instant.now());
                    paiementDTO.setTrials(1);
                    paiementDTO.setPaiementStatus(ActionStatus.INPROGRESS);
                    paiementDTO.setAdjustmentId(adjustmentDTO.getId());

//                    paiementDTO.setExternalTransactionId(transactionid);
                    paiementDTO.setExternalTransactionId(extractExternalTransationId(responseMap));

                    PaiementDTO result = paiementService.save(paiementDTO);

                    return ResponseEntity.created(new URI("/api/paiements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "operationMicroservicePayasyougo", result.getClientCode().toString()))
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

    /**
     * {@code POST  /invoicePaymentCallback} : payment callback.
     *
     * @param paymentCallbackDTO the paymentCallbackDTO.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentRequestDTO, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoicePaymentCallback")
    public ResponseEntity<PaiementDTO> paymentCallback(@Valid @RequestBody PaymentCallbackDTO paymentCallbackDTO) throws URISyntaxException {
        log.info("REST request to save PaymentCallback request : {}", paymentCallbackDTO);

        String transactionId = paymentCallbackDTO.getExternalId();
        String status = paymentCallbackDTO.getStatus();
        Double amount = paymentCallbackDTO.getAmount();

        Optional<PaiementDTO> optionalPaiementDTO = paiementService.findOneByTransactionId(transactionId);

        if (optionalPaiementDTO.isPresent()) {
            PaiementDTO paiementDTO = optionalPaiementDTO.get();
            if (paiementDTO.getFactureId() != null) {
                Optional<FactureDTO> optionalFactureDTO = factureService.findOne(paiementDTO.getFactureId());
                if (optionalFactureDTO.isPresent()) {
                    FactureDTO factureDTO = optionalFactureDTO.get();
                    if ("APPROVED".equals(status) && factureDTO.getAmount().equals(amount)) {
                        factureDTO.setPaiementStatus(ActionStatus.SUCCESS);
                        factureDTO.setDatePaiement(Instant.now());
                        paiementDTO.setPaiementStatus(ActionStatus.SUCCESS);
                        paiementDTO.setPaymentMessage("Invoice payment successful !");
                    } else {
                        factureDTO.setPaiementStatus(ActionStatus.FAILED);
                        paiementDTO.setPaiementStatus(ActionStatus.FAILED);
                        if(!"APPROVED".equals(status)) {
                            paiementDTO.setPaymentMessage("Invoice payment error: payment api response status FAILED !");
                        } else if (!factureDTO.getAmount().equals(amount)) {
                            paiementDTO.setPaymentMessage("Invoice payment error: distinct amounts !");
                        } else {
                            paiementDTO.setPaymentMessage("Invoice payment error !");
                        }
                    }
                    factureService.save(factureDTO);
                    PaiementDTO result = paiementService.save(paiementDTO);
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementDTO.getId().toString()))
                        .body(result);
                }
            } else {
                if (paiementDTO.getAdjustmentId() != null) {
                    Optional<AdjustmentDTO> optionalAdjustmentDTO = adjustmentService.findOne(paiementDTO.getAdjustmentId());
                    if (optionalAdjustmentDTO.isPresent()) {
                        AdjustmentDTO adjustmentDTO = optionalAdjustmentDTO.get();
                        if ("APPROVED".equals(status) && adjustmentDTO.getPrice().equals(amount)) {
                            SimpleAdjustOrderDTO order = extractOrderInfo(paiementDTO);
                            try {
                                simpleAdjustAccount(order);
                                adjustmentDTO.setStatus(ActionStatus.SUCCESS);
                                adjustmentDTO.setAdjustmentMessage("Pay as you go adjustment successful !");
                                paiementDTO.setPaymentMessage("Pay as you go payment successful !");
                                paiementDTO.setPaiementStatus(ActionStatus.SUCCESS);
                            } catch (Exception e) {
                            	//TODO : MD : Audit : Mettre des loggueurs et non des printStackTrace
                                e.printStackTrace();
                                adjustmentDTO.setStatus(ActionStatus.FAILED);
                                adjustmentDTO.setAdjustmentMessage("Pay as you go error when adjusting client account !");
                                paiementDTO.setPaiementStatus(ActionStatus.FAILED);
                                paiementDTO.setPaymentMessage("Pay as you go error when adjusting client account !");
                            }
                        } else {
                            adjustmentDTO.setStatus(ActionStatus.FAILED);
                            paiementDTO.setPaiementStatus(ActionStatus.FAILED);
                            if (!"APPROVED".equals(status)) {
                                adjustmentDTO.setAdjustmentMessage("Pay as you go error: payment not approved by payment API !");
                                paiementDTO.setPaymentMessage("Pay as you go error: payment not approved by payment API !");
                            } else if (!adjustmentDTO.getPrice().equals(amount)) {
                                adjustmentDTO.setAdjustmentMessage("Pay as you go error: distinct amounts !");
                                paiementDTO.setPaymentMessage("Pay as you go error: distinct amounts !");
                            } else {
                                adjustmentDTO.setAdjustmentMessage("Pay as you go error !");
                                paiementDTO.setPaymentMessage("Pay as you go error !");
                            }
                        }
                        paiementService.save(paiementDTO);
                        adjustmentService.save(adjustmentDTO);
                        return ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementDTO.getClientCode().toString()))
                            .body(paiementDTO);
                    }
                }
            }
        }
        throw new BadRequestAlertException("Payment callback failed", ENTITY_NAME, "paymentCallbackFailed");
    }

    private SimpleAdjustOrderDTO extractOrderInfo(PaiementDTO paymentRequestDTO) {
        SimpleAdjustOrderDTO simpleAdjustOrderDTO = new SimpleAdjustOrderDTO();
        simpleAdjustOrderDTO.setNumeroClient(paymentRequestDTO.getNumeroVirtuel());
        simpleAdjustOrderDTO.setStock(paymentRequestDTO.getAmount());
        return simpleAdjustOrderDTO;
    }

    private void simpleAdjustAccount(SimpleAdjustOrderDTO simpleAdjustOrderDTO) throws Exception {
        log.info("REST request to do simple adjust account : {}", simpleAdjustOrderDTO);
        if (simpleAdjustOrderDTO.getStock() != null && simpleAdjustOrderDTO.getStock() != 0) {
            this.adjustAccountService.adjustCreditAccount(simpleAdjustOrderDTO.getNumeroClient(), simpleAdjustOrderDTO.getStock());
        }
    }

    private AdjustmentDTO prepareAdjustmentDTO(PaymentRequestDTO paymentRequestDTO) {
        AdjustmentDTO adjustmentDTO = new AdjustmentDTO();
        adjustmentDTO.setClientId(paymentRequestDTO.getClientId());
        adjustmentDTO.setClientCode(paymentRequestDTO.getCustomerAccount());
        adjustmentDTO.setNumeroVirtuel(paymentRequestDTO.getNumeroVirtuel());
        adjustmentDTO.setNumeroFreemoney(paymentRequestDTO.getCustomermsisdn());
        adjustmentDTO.setTargetNumber(paymentRequestDTO.getNumeroVirtuel());
        adjustmentDTO.setPrice(paymentRequestDTO.getAmount());
        adjustmentDTO.setDateAdjustment(Instant.now());
        adjustmentDTO.setTypeAdjustment(AdjustmentType.MANUAL);
        adjustmentDTO.setTrials(1);
        adjustmentDTO.setStatus(ActionStatus.INPROGRESS);
        return adjustmentDTO;
    }

    private String extractExternalTransationId(Map responseMap) {
        try {
            String externalTransactionId = null;
            if(responseMap.get("root") != null) {
                Map root = (Map) responseMap.get("root");
                if(root.get("transactionid") != null) {
                    externalTransactionId = (String) root.get("transactionid");
                }
            }
            return externalTransactionId;
        } catch (Exception e) {
            return null;
        }
    }
}
