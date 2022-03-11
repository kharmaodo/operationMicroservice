package sn.free.selfcare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.service.PaymentMethod;
import sn.free.selfcare.service.dto.PaymentRequestDTO;

import java.util.Map;

@Component
public class InvoicePaymentMethodImpl implements PaymentMethod<PaymentRequestDTO> {

    private final Logger log = LoggerFactory.getLogger(InvoicePaymentMethodImpl.class);

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public InvoicePaymentMethodImpl(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Map pay(PaymentRequestDTO paymentRequest) {
        log.info("Start Invoice PaymentMethod with request: {}", paymentRequest);
        String paymentUrl = applicationProperties.getPayment().getUrl();
        log.info("Payment URL: {}", paymentUrl);
        Map paymentResponse = null;
        if (paymentUrl != null) {
            // Complete payment request info
            paymentRequest.setB2bmsisdn(applicationProperties.getPayment().getB2bmsisdn());
            paymentRequest.setB2buserName(applicationProperties.getPayment().getB2buserName());
            paymentRequest.setB2bPassword(applicationProperties.getPayment().getB2bPassword());
            log.info("Payment request: {}", paymentRequest);
            HttpEntity<PaymentRequestDTO> requestEntity = new HttpEntity<>(paymentRequest, new HttpHeaders());
            paymentResponse = restTemplate.postForEntity(applicationProperties.getPayment().getUrl(), requestEntity, Map.class).getBody();
        }
        log.info("Payment response: {}", paymentResponse);
        log.info("End Invoice PaymentMethod");
        return paymentResponse;
    }
}
