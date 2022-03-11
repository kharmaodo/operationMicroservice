package sn.free.selfcare.service.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import sn.free.selfcare.service.MailService;
import sn.free.selfcare.service.SmsNotificationService;
import sn.free.selfcare.service.dto.NotificationDTO;

@Component
public class NotificationReceiver {

    private final Logger log = LoggerFactory.getLogger(NotificationReceiver.class);

    @Autowired
    private SmsNotificationService smsNotificationService;
    @Autowired
    private MailService mailService;

    @JmsListener(destination = JmsDestinationName.SMS_NOTIFICATION_QUEUE, containerFactory = "myFactory")
    public void receiveSmsMessage(NotificationDTO notification) {
        log.info("SMS Message received <" + notification + ">");
        smsNotificationService.sendSms(notification);
    }

    @JmsListener(destination = JmsDestinationName.EMAIL_NOTIFICATION_QUEUE, containerFactory = "myFactory")
    public void receiveEmailMessage(NotificationDTO notification) {
        log.info("EMAIL Message received <" + notification + ">");
        mailService.sendEmail(notification.getTo(), notification.getSubject(), notification.getText(), false, true);
    }
}
