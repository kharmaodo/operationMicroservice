package sn.free.selfcare.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.free.selfcare.service.SmsNotificationService;
import sn.free.selfcare.service.dto.NotificationDTO;

import javax.validation.Valid;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.PeriodeEnvoi}.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private final SmsNotificationService smsNotificationService;

    public NotificationResource(SmsNotificationService smsNotificationService) {
        this.smsNotificationService = smsNotificationService;
    }

    @PostMapping("/notification/sms")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        log.debug("REST request to send sms notification: {}", notificationDTO);
        smsNotificationService.sendSms(notificationDTO);
        return ResponseEntity.ok().build();
    }
}
