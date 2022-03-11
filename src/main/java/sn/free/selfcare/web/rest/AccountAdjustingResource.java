package sn.free.selfcare.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.HeaderUtil;
import sn.free.selfcare.apiclient.service.AdjustAccountService;
import sn.free.selfcare.apiclient.service.BalanceInfoService;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.domain.enumeration.AdjustmentType;
import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.service.dto.AdjustOrderDTO;
import sn.free.selfcare.service.dto.AdjustmentDTO;
import sn.free.selfcare.service.dto.NotificationDTO;
import sn.free.selfcare.service.dto.SimpleAdjustOrderDTO;
import sn.free.selfcare.service.messaging.JmsDestinationName;
import sn.free.selfcare.web.rest.errors.AccountAdjustingException;
import sn.free.selfcare.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.PeriodeEnvoi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountAdjustingResource {

    private final Logger log = LoggerFactory.getLogger(AccountAdjustingResource.class);

    @Autowired
    private AdjustAccountService adjustAccountService;

    @Autowired
    private BalanceInfoService balanceInfoService;

    @Autowired
    private AdjustmentService adjustmentService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${services.balanceInfo.url}")
    private String balanceInfoURL;

    @Value("${services.adjustAccount.url}")
    private String adjustAccountURL;

    private static String replaceLast(String text, String search, String replacement) {
        int pos = text.lastIndexOf(search);
        if (pos > -1) {
            return text.substring(0, pos) + replacement + text.substring(pos + search.length(), text.length());
        } else {
            return text;
        }
    }

    @PostConstruct
    public void init() {
        log.info("Init -> Start");

        log.info("balanceInfoURL = {}", balanceInfoURL);
        this.balanceInfoService.setBalanceInfoURL(balanceInfoURL);

        log.info("adjustAccountURL = {}", adjustAccountURL);
        this.adjustAccountService.setAdjustAccountURL(adjustAccountURL);

        log.info("Init -> End");
    }

    @PostMapping("/adjust")
    public ResponseEntity<Void> adjustAccount(@Valid @RequestBody AdjustOrderDTO adjustOrderDTO)
        throws URISyntaxException, AccountAdjustingException, Exception {
        log.debug("REST request to adjust account : {}", adjustOrderDTO);
        if(adjustOrderDTO.getDestinataire().length()==15) {
        	adjustOrderDTO.setDestinataire(adjustOrderDTO.getDestinataire().substring(3));
        	log.debug("===============REST request to adjust account : {}", adjustOrderDTO);
        }
        AdjustmentDTO adjustmentDTO = this.adjustmentService.save(prepareAdjustmentDTO(adjustOrderDTO));

        try {

            if (adjustOrderDTO.getPrix() > this.balanceInfoService.getBalanceInfo(adjustOrderDTO.getNumeroClient()).getCredit()) {
                log.warn("Client's virtual number {} does not have enough credit.", adjustOrderDTO.getNumeroClient());
                adjustmentDTO.setStatus(ActionStatus.FAILED);
                adjustmentDTO.setAdjustmentMessage("Error individual sharing resource: Solde insuffisant!");
                this.adjustmentService.save(adjustmentDTO);
                throw new AccountAdjustingException("Solde insuffisant.", "partageRessource.soldeInsuffisant");
            }

            if (adjustOrderDTO.getCredit() != null && adjustOrderDTO.getCredit() != 0) {
                log.info("==================== AVANT adjustCreditAccount  =================================");
                this.adjustAccountService.adjustCreditAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getCredit());
                log.info("==================== FIN adjustCreditAccount  =================================");
            }

            if (adjustOrderDTO.getSms() != null && adjustOrderDTO.getSms() != 0) {
                log.info("==================== AVANT adjustSMSAccount  =================================");
                this.adjustAccountService.adjustSMSAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getSms());
                log.info("==================== FIN adjustSMSAccount  =================================");
            }

            if (adjustOrderDTO.getMinAppel() != null && adjustOrderDTO.getMinAppel() != 0) {
                log.info("==================== AVANT adjustAppelAccount  =================================");
                this.adjustAccountService
                    .adjustAppelAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getMinAppel());
                log.info("==================== FIN adjustAppelAccount  =================================");
            }

            if (adjustOrderDTO.getData() != null && adjustOrderDTO.getData() != 0) {
                log.info("==================== AVANT adjustDataAccount  =================================");
                this.adjustAccountService.adjustDataAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getData());
                log.info("==================== FIN adjustDataAccount  =================================");
            }

            if (!this.adjustAccountService.adjustCreditFromWallet(adjustOrderDTO.getNumeroClient(), -adjustOrderDTO.getPrix(), "2518")) {
                log.info("==================== AVANT adjustCreditFromWallet  =================================");
                this.adjustAccountService.adjustCreditFromWallet(adjustOrderDTO.getNumeroClient(), -adjustOrderDTO.getPrix(), "2000");
                log.info("==================== FIN adjustCreditFromWallet  =================================");
            }
                log.info("==================== TEST adjustCreditFromWallet  ================================= numeroClient = {} prix={}",adjustOrderDTO.getNumeroClient(),adjustOrderDTO.getPrix());


            adjustmentDTO.setStatus(ActionStatus.SUCCESS);
            adjustmentDTO.setAdjustmentMessage("Individual sharing resource sucessfull !");

            this.adjustmentService.save(adjustmentDTO);


            // == create the sms notification dto ==
            NotificationDTO smsNotification = new NotificationDTO();
            smsNotification.setTo(adjustOrderDTO.getDestinataire());
            smsNotification.setText(createSmsContent(adjustOrderDTO));

            // == put the sms notification dto in Messaging Queue for processing ==
            jmsTemplate.convertAndSend(JmsDestinationName.SMS_NOTIFICATION_QUEUE, smsNotification);

            // == create the email notification dto ==
            NotificationDTO emailNotification = new NotificationDTO();
            emailNotification.setTo(adjustOrderDTO.getEmailAdminClient());
            StringBuilder sharingRecapMessage = new StringBuilder("Bonjour Cher(e) Client(e), <br/><br/>Veuillez trouver ci-dessous, le recapitulatif de votre partage: <br/>");
            sharingRecapMessage.append(createEmailContent(adjustOrderDTO) + "<br/>");
            emailNotification.setText(sharingRecapMessage.toString());
            emailNotification.setSubject("Partage ponctuel de ressource");

            // == put the email notification dto in Messaging Queue for processing ==
            jmsTemplate.convertAndSend(JmsDestinationName.EMAIL_NOTIFICATION_QUEUE, emailNotification);

            /**
             * TODO: add code that will adjust from wallet 2518 or 2000 depending on which
             * has credit, and will rollback in case debit fails
             */
            log.info("==================== AVANT FIN METHODE ADJUST   =================================");
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, applicationName + ".shareResource.shareSuccess", adjustOrderDTO.getEmailAdminClient()))
                .build();

        } catch (Exception e) {
            log.info("==================== DEBUTE EXCEPTION ADJUST"+e.getMessage()+ " =================================");
            System.out.println(e.getStackTrace());
        	//TODO : MD : Audit : Log instead of e.getStackTrace()
            adjustmentDTO.setStatus(ActionStatus.FAILED);
            adjustmentDTO.setAdjustmentMessage("Error global sharing resource: " + e.getMessage());
            adjustmentDTO = adjustmentService.save(adjustmentDTO);

            // == create the email notification dto ==
            NotificationDTO emailNotification = new NotificationDTO();
            emailNotification.setTo(adjustOrderDTO.getEmailAdminClient());
            StringBuilder sharingRecapMessage = new StringBuilder("Bonjour Cher(e) Client(e), <br/><br/>Veuillez trouver ci-dessous, le recapitulatif de votre partage: <br/>");
            sharingRecapMessage.append("Echec du partage sur la ligne " + adjustOrderDTO.getDestinataire() + " pour cause: " + adjustmentDTO.getAdjustmentMessage() + "<br/>");
            emailNotification.setText(sharingRecapMessage.toString());
            emailNotification.setSubject("Partage ponctuel de ressource");

            // == put the email notification dto in Messaging Queue for processing ==
            jmsTemplate.convertAndSend(JmsDestinationName.EMAIL_NOTIFICATION_QUEUE, emailNotification);

            /**
             * TODO: add code that will adjust from wallet 2518 or 2000 depending on which
             * has credit, and will rollback in case debit fails
             */

            throw new BadRequestAlertException("Client sharing resources failed!", "shareFailure", "shareResource.shareFailure");
        }
    }

    @PostMapping("/adjust/simple")
    public ResponseEntity<Void> simpleAdjustAccount(@Valid @RequestBody SimpleAdjustOrderDTO simpleAdjustOrderDTO) {
        log.debug("REST request to do simple adjust account : {}", simpleAdjustOrderDTO);
        try {
            if (simpleAdjustOrderDTO.getStock() != null && simpleAdjustOrderDTO.getStock() != 0) {
                this.adjustAccountService.adjustCreditAccount(simpleAdjustOrderDTO.getNumeroClient(), simpleAdjustOrderDTO.getStock());
            }

            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, applicationName + ".payasyougo.adjustSuccess", simpleAdjustOrderDTO.getNumeroClient()))
                .build();
        } catch (Exception e) {
            throw new BadRequestAlertException("Client account adjustment failed! Cause: " + e.getMessage() + "!", "adjustFailure", "payasyougo.adjustFailure");
        }
    }

    @PostMapping("/adjust/all")
    public ResponseEntity<Void> shareResources(@Valid @RequestBody List<AdjustOrderDTO> adjustOrderDTOList) {
        log.debug("REST request to shareResources : {}", adjustOrderDTOList);
        StringBuilder sharingRecapMessage = new StringBuilder("Bonjour Cher(e) Client(e), <br/><br/>Veuillez trouver ci-dessous, le recapitulatif de votre partage: <br/>");
        String emailAdminClient = (adjustOrderDTOList.size() > 0) ? adjustOrderDTOList.get(0).getEmailAdminClient() : "";
        log.info("==================== EMAIL_adminClient adjustALL="+ emailAdminClient +" =================================");
        adjustOrderDTOList.stream().forEach(order -> {
            AdjustmentDTO savedOrder = adjustAccountResource(order);
            if (savedOrder.getStatus().equals(ActionStatus.SUCCESS)) {
                // == send sms notification when success ==
                log.info("==================== AVANT NOTIFICATION CLIENT ADJUST_ALL =================================");
                NotificationDTO smsNotification = new NotificationDTO();
                smsNotification.setTo(order.getDestinataire());
                smsNotification.setText(createSmsContent(order));

                // == put the sms notification dto in Messaging Queue for processing ==
                jmsTemplate.convertAndSend(JmsDestinationName.SMS_NOTIFICATION_QUEUE, smsNotification);

                sharingRecapMessage.append(createEmailContent(order) + "<br/>");
            } else {
                log.info("==================== Echec du partage sur la ligne ADJUST_ALL =================================");
                sharingRecapMessage.append("Echec du partage sur la ligne " + order.getDestinataire() + " pour cause: " + savedOrder.getAdjustmentMessage() + "<br/>");
            }
        });

        // == create the email notification dto ==
        NotificationDTO emailNotification = new NotificationDTO();
        emailNotification.setTo(emailAdminClient);
        emailNotification.setText(sharingRecapMessage.toString());
        emailNotification.setSubject("Partage global de ressource");

        // == put the email notification dto in Messaging Queue for processing ==
        jmsTemplate.convertAndSend(JmsDestinationName.EMAIL_NOTIFICATION_QUEUE, emailNotification);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, applicationName + ".shareResource.shareSuccess", emailAdminClient))
            .build();
    }

    private String createSmsContent(AdjustOrderDTO order) {
        StringBuilder message = new StringBuilder("Vous avez recu ");
        Double credit = order.getCredit() != null ? order.getCredit() : 0;
        Double sms = order.getSms() != null ? order.getSms() : 0;
        Double minAppel = order.getMinAppel() != null ? order.getMinAppel() : 0;
        Double data = order.getData() != null ? order.getData() : 0;
        List<String> resources = new ArrayList<String>();
        if (credit != 0) {
            resources.add(credit + " FCFA de credit");
        }
        if (sms != 0) {
            resources.add(sms + " SMS");
        }
        if (minAppel != 0) {
            resources.add(minAppel + " minutes d'appel");
        }
        if (data != 0) {
            resources.add(data + " Go de connexion");
        }
        String resourcesStr = String.join(", ", resources);
        message.append(replaceLast(resourcesStr, ", ", " et "));

        return message.toString();
    }

    private String createEmailContent(AdjustOrderDTO order) {
        StringBuilder message = new StringBuilder("La ligne " + order.getDestinataire() + " a reçu ");
        Double credit = order.getCredit() != null ? order.getCredit() : 0;
        Double sms = order.getSms() != null ? order.getSms() : 0;
        Double minAppel = order.getMinAppel() != null ? order.getMinAppel() : 0;
        Double data = order.getData() != null ? order.getData() : 0;
        List<String> resources = new ArrayList<String>();
        if (credit != 0) {
            resources.add(credit + " FCFA de credit");
        }
        if (sms != 0) {
            resources.add(sms + " SMS");
        }
        if (minAppel != 0) {
            resources.add(minAppel + " minutes d'appel");
        }
        if (data != 0) {
            resources.add(data + " Go de connexion.");
        }
        String resourcesStr = String.join(", ", resources);
        message.append(replaceLast(resourcesStr, ", ", " et "));

        return message.toString();
    }

    private AdjustmentDTO adjustAccountResource(AdjustOrderDTO adjustOrderDTO) {

        AdjustmentDTO adjustmentDTO = this.adjustmentService.save(prepareAdjustmentDTO(adjustOrderDTO));
        try {
            if (adjustOrderDTO.getPrix() > this.balanceInfoService.getBalanceInfo(adjustOrderDTO.getNumeroClient()).getCredit()) {
                log.warn("Client's virtual number {} does not have enough credit.", adjustOrderDTO.getNumeroClient());
                adjustmentDTO.setStatus(ActionStatus.FAILED);
                adjustmentDTO.setAdjustmentMessage("Error global sharing resource: Solde insuffisant!");
            }

            if (adjustOrderDTO.getCredit() != null && adjustOrderDTO.getCredit() != 0)
                this.adjustAccountService.adjustCreditAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getCredit());
            if (adjustOrderDTO.getSms() != null && adjustOrderDTO.getSms() != 0)
                this.adjustAccountService.adjustSMSAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getSms());
            if (adjustOrderDTO.getMinAppel() != null && adjustOrderDTO.getMinAppel() != 0) this.adjustAccountService
                .adjustAppelAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getMinAppel());
            if (adjustOrderDTO.getData() != null && adjustOrderDTO.getData() != 0)
                this.adjustAccountService.adjustDataAccount(adjustOrderDTO.getDestinataire(), adjustOrderDTO.getData());

            if (!this.adjustAccountService.adjustCreditFromWallet(adjustOrderDTO.getNumeroClient(),
                -adjustOrderDTO.getPrix(), "2518"))
                this.adjustAccountService.adjustCreditFromWallet(adjustOrderDTO.getNumeroClient(),
                    -adjustOrderDTO.getPrix(), "2000");

            adjustmentDTO.setStatus(ActionStatus.SUCCESS);
            adjustmentDTO.setAdjustmentMessage("Global sharing resource successful !");

            adjustmentDTO = adjustmentService.save(adjustmentDTO);

            /**
             * TODO: add code that will adjust from wallet 2518 or 2000 depending on which
             * has credit, and will rollback in case debit fails
             */
            return adjustmentDTO;
        } catch (Exception e) {
        	//TODO : MD : Audit : Mettre des loggueurs et non des printStackTrace
            e.printStackTrace();
            adjustmentDTO.setStatus(ActionStatus.FAILED);
            adjustmentDTO.setAdjustmentMessage("Error global sharing resource: " + e.getMessage());
            adjustmentDTO = adjustmentService.save(adjustmentDTO);
            return adjustmentDTO;
        }
    }

    private AdjustmentDTO prepareAdjustmentDTO(AdjustOrderDTO adjustOrderDTO) {
        AdjustmentDTO adjustmentDTO = new AdjustmentDTO();
        adjustmentDTO.setClientId(adjustOrderDTO.getClientId());
        adjustmentDTO.setClientCode(adjustOrderDTO.getClientCode());
        adjustmentDTO.setNumeroVirtuel(adjustOrderDTO.getNumeroClient());
        adjustmentDTO.setTargetNumber(adjustOrderDTO.getDestinataire());
        adjustmentDTO.setCredit(adjustOrderDTO.getCredit());
        adjustmentDTO.setMinAppel(adjustOrderDTO.getMinAppel());
        adjustmentDTO.setSms(adjustOrderDTO.getSms());
        adjustmentDTO.setGoData(adjustOrderDTO.getData());
        adjustmentDTO.setPrice(-adjustOrderDTO.getPrix());
        adjustmentDTO.setDateAdjustment(Instant.now());
        adjustmentDTO.setTypeAdjustment(AdjustmentType.MANUAL);
        adjustmentDTO.setTrials(1);
        adjustmentDTO.setStatus(ActionStatus.INPROGRESS);
        return adjustmentDTO;
    }
}
