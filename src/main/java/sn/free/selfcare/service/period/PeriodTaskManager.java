package sn.free.selfcare.service.period;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import sn.free.selfcare.apiclient.service.AdjustAccountService;
import sn.free.selfcare.apiclient.service.BalanceInfoService;
import sn.free.selfcare.client.AssetFeignClient;
import sn.free.selfcare.domain.PeriodeEnvoi;
import sn.free.selfcare.domain.enumeration.ObjectStatus;
import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.service.PeriodeEnvoiService;
import sn.free.selfcare.service.SmsNotificationService;

/**
 * This component manages the jobs scheduling, allowing for automatic adjustment
 * to be executed without human intervention.
 *
 * @author AhmaDIAW
 *
 */
@Component
public class PeriodTaskManager {

	private final Logger log = LoggerFactory.getLogger(PeriodTaskManager.class);

	private static Map<Long, ScheduledFuture<?>> scheduledJobs = new HashMap<>();

    @Value("${services.balanceInfo.url}")
    private String balanceInfoURL;

    @Value("${services.adjustAccount.url}")
    private String adjustAccountURL;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private PeriodeEnvoiService periodeEnvoiService;

	@Autowired
	private AssetFeignClient assetClient;

	@Autowired
	private BalanceInfoService balanceInfoService;

	@Autowired
	private AdjustAccountService adjustAccountService;

	@Autowired
	private AdjustmentService adjustmentService;

	@Autowired
	private SmsNotificationService smsNotificationService;

	@PostConstruct
	public void jobsInitialization() {
		this.log.info("Initializing automatic adjustment jobs.");
		setServicesURL();
		if (PeriodTaskManager.scheduledJobs.isEmpty())
			this.periodeEnvoiService.findAllEntities().forEach(periode -> this.createAdjustmentJob(periode));
	}

    public void setServicesURL() {
        log.info("setServicesURL -> Start");

        log.info("balanceInfoURL = {}", balanceInfoURL);
        this.balanceInfoService.setBalanceInfoURL(balanceInfoURL);

        log.info("adjustAccountURL = {}", adjustAccountURL);
        this.adjustAccountService.setAdjustAccountURL(adjustAccountURL);

        log.info("setServicesURL -> End");
    }

	public void createAdjustmentJob(PeriodeEnvoi periodeEnvoi) {
		this.cancelAdjustmentJob(periodeEnvoi.getGroupeId());
		if (!ObjectStatus.ARCHIVED.equals(periodeEnvoi.getStatus())) {
			ScheduledFuture<?> job = this.taskScheduler.schedule(
					new AutomaticGroupeAdjustment(assetClient, balanceInfoService, adjustAccountService,
							adjustmentService, smsNotificationService, periodeEnvoi.getGroupeId()),
					new CronTrigger(periodeEnvoi.getExpression()));
			PeriodTaskManager.scheduledJobs.put(periodeEnvoi.getGroupeId(), job);
		}
	}

	public void cancelAdjustmentJob(Long groupeId) {
		ScheduledFuture<?> scheduledTask = PeriodTaskManager.scheduledJobs.get(groupeId);
		if (scheduledTask != null) {
			scheduledTask.cancel(true);
			PeriodTaskManager.scheduledJobs.remove(groupeId);
		}
	}

}
