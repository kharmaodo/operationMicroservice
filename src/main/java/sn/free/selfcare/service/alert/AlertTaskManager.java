package sn.free.selfcare.service.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import sn.free.selfcare.apiclient.service.BalanceInfoService;
import sn.free.selfcare.config.ApplicationProperties;
import sn.free.selfcare.domain.enumeration.ObjectStatus;
import sn.free.selfcare.service.AlertService;
import sn.free.selfcare.service.MailService;
import sn.free.selfcare.service.SmsNotificationService;
import sn.free.selfcare.service.dto.AlertDTO;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * This component manages the jobs scheduling, allowing for automatic alert
 * to be executed without human intervention.
 *
 * @author MSF
 */
@Component
public class AlertTaskManager {

    private static Map<Long, ScheduledFuture<?>> scheduledJobs = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(AlertTaskManager.class);
    private final ApplicationProperties applicationProperties;
    @Autowired
    MailService mailService;
    @Value("${services.balanceInfo.url}")
    private String balanceInfoURL;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private AlertService alertService;
    @Autowired
    private BalanceInfoService balanceInfoService;
    @Autowired
    private SmsNotificationService smsNotificationService;

    public AlertTaskManager(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void jobsInitialization() {
        this.log.debug("Initializing automatic alert jobs.");
        setServicesURL();
        if (AlertTaskManager.scheduledJobs.isEmpty())
            this.alertService.findAll().forEach(alertDTO -> this.createAlertJob(alertDTO));
    }

    public void setServicesURL() {
        log.info("setServicesURL -> Start");

        log.info("balanceInfoURL = {}", balanceInfoURL);
        this.balanceInfoService.setBalanceInfoURL(balanceInfoURL);

        log.info("setServicesURL -> End");
    }

    public void createAlertJob(AlertDTO alertDTO) {
        this.cancelAlertJob(alertDTO.getId());
        String employeAlertExpression = applicationProperties.getEmployeAlert().getExpression();
        if (!ObjectStatus.ARCHIVED.equals(alertDTO.getStatus())) {
            this.log.debug("Property application.employeAlert.expression : {}", employeAlertExpression);
            ScheduledFuture<?> job = this.taskScheduler.schedule(
                new AutomaticAlert(balanceInfoService, mailService, smsNotificationService, alertDTO),
                new CronTrigger(employeAlertExpression));
            AlertTaskManager.scheduledJobs.put(alertDTO.getId(), job);
        }
    }

    public void cancelAlertJob(Long alertId) {
        ScheduledFuture<?> scheduledTask = AlertTaskManager.scheduledJobs.get(alertId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            AlertTaskManager.scheduledJobs.remove(alertId);
        }
    }
}
