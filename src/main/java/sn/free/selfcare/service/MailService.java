package sn.free.selfcare.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
//TODO : MD : Audit : Utilitaire Presque dupliqué : MailService
@Service
public class MailService {

	private final Logger log = LoggerFactory.getLogger(MailService.class);

	private final JHipsterProperties jHipsterProperties;

	private final JavaMailSender javaMailSender;

	public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
			MessageSource messageSource, SpringTemplateEngine templateEngine) {

		this.jHipsterProperties = jHipsterProperties;
		this.javaMailSender = javaMailSender;
	}

	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
				isHtml, to, subject, content);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			message.setText(content, isHtml);
			javaMailSender.send(mimeMessage);
			log.debug("Sent email to User '{}'", to);
		}
		catch (MailException | MessagingException e) {
			log.warn("Email could not be sent to user '{}'", to, e);
		}
	}


	@Async
	public void sendStockAlertEmail(String username, String numeroVirtuel, String stock, String seuilStock) {
		log.debug("Sending Stock alert email to '{}'", username);
		sendEmail(username, "B2BSelfcare - Stock Alert", "Le crédit ("+ stock + " FCFA) sur votre numéro virtuel " + numeroVirtuel + " dépasse " + seuilStock + " FCFA.", false, false);
	}
}
