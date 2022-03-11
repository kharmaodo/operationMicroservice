package sn.free.selfcare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.free.selfcare.service.dto.NotificationDTO;

import java.util.Base64;
import java.util.List;
import java.util.Map;
//TODO : MD : Audit : Utilitaire Presque dupliqué
@Service
public class SmsNotificationService {

    private final Logger log = LoggerFactory.getLogger(SmsNotificationService.class);

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Value("${app.smsNotification.username}")
    private String smsNotificationUsername;

    @Value("${app.smsNotification.password}")
    private String smsNotificationPassword;

    @Value("${app.smsNotification.url}")
    private String smsNotificationURL;

    public boolean sendSms(NotificationDTO notificationDTO) {
        this.log.debug("Sending SMS with content : {}", notificationDTO);
        boolean result = false;
        if (notificationDTO != null && notificationDTO.getTo() != null && notificationDTO.getText() != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(smsNotificationUsername);
            buffer.append(":");
            buffer.append(smsNotificationPassword);
            byte[] encodedAsBytes = buffer.toString().getBytes();
            String base64Encoded = Base64.getMimeEncoder().encodeToString(encodedAsBytes);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Basic ".concat(base64Encoded));
            httpHeaders.add("Content-Type", "application/json");
            httpHeaders.add("Accept", "application/json");
            HttpEntity<NotificationDTO> requestEntity = new HttpEntity<NotificationDTO>(notificationDTO, httpHeaders);
            ResponseEntity<Map> response = restTemplate.postForEntity(smsNotificationURL, requestEntity, Map.class);
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                List messages = (List) response.getBody().get("messages");
                this.log.debug("Messages : {} ", messages);
                if (messages != null && messages.size() > 0) {
                    Map message = (Map) messages.get(0);
                    Map status = (Map) message.get("status");
                    int groupId = (int) status.get("groupId"); // PENDING group id
                    if (groupId == 1) {
                        result = true;
                    }
                }
            }
            this.log.debug("Send SMS to recipient : {} - sent : {} ", notificationDTO.getTo(), result);
        }
        return result;
    }
}
