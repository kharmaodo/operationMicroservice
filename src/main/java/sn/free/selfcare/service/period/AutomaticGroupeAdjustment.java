/**
 *
 */
package sn.free.selfcare.service.period;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sn.free.selfcare.apiclient.dto.BalanceInfoDTO;
import sn.free.selfcare.apiclient.service.AdjustAccountService;
import sn.free.selfcare.apiclient.service.BalanceInfoService;
import sn.free.selfcare.client.AssetFeignClient;
import sn.free.selfcare.domain.enumeration.ActionStatus;
import sn.free.selfcare.domain.enumeration.AdjustmentType;
import sn.free.selfcare.service.AdjustmentService;
import sn.free.selfcare.service.SmsNotificationService;
import sn.free.selfcare.service.dto.AdjustmentDTO;
import sn.free.selfcare.service.dto.NotificationDTO;
import sn.free.selfcare.service.dto.asset.EmployeAdjustmentDTO;
import sn.free.selfcare.service.dto.asset.GroupeAdjustmentDTO;

import java.time.Instant;
import java.util.*;

/**
 * This task executes the automatic adjustment for a group without human
 * intervention. It is run by a job scheduled periodically.
 * 
 * @author AhmaDIAW
 *
 */
public class AutomaticGroupeAdjustment implements Runnable {

	private static final Integer RETRIES = 3;
	private final Logger log = LoggerFactory.getLogger(AutomaticGroupeAdjustment.class);
	private Long groupeId;

	private AssetFeignClient assetClient;

	private BalanceInfoService balanceInfoService;

	private AdjustAccountService adjustAccountService;

	private AdjustmentService adjustmentService;

	private SmsNotificationService smsNotificationService;

	public AutomaticGroupeAdjustment(AssetFeignClient assetClient, BalanceInfoService balanceInfoService,
			AdjustAccountService adjustAccountService, AdjustmentService adjustmentService,
			SmsNotificationService smsNotificationService, Long groupeId) {
		this.log.debug("Initializing Groupe {} adjustment job", groupeId);
		this.assetClient = assetClient;
		this.balanceInfoService = balanceInfoService;
		this.adjustAccountService = adjustAccountService;
		this.adjustmentService = adjustmentService;
		this.smsNotificationService = smsNotificationService;
		this.groupeId = groupeId;
	}

	private static String replaceLast(String text, String search, String replacement) {
		int pos = text.lastIndexOf(search);
		if (pos > -1) {
			return text.substring(0, pos) + replacement + text.substring(pos + search.length(), text.length());
		} else {
			return text;
		}
	}

	@Override
	public void run() {
		this.log.info("Executing Groupe {} adjustment job", this.groupeId);
		GroupeAdjustmentDTO groupe = this.assetClient.getGroupeForAdjustment(this.groupeId);
		this.log.info("Groupe adjustment {}", groupe);
		Set<AdjustmentDTO> adjustments = this.generateAdjustments(groupe);
		adjustments.forEach(adjustment -> adjustForEmploye(adjustment, groupe.getNumeroClient()));
	}

	private Set<AdjustmentDTO> generateAdjustments(GroupeAdjustmentDTO groupe) {
		Set<AdjustmentDTO> adjustments = new HashSet<>();
		for (EmployeAdjustmentDTO employe : groupe.getEmployes()) {
			AdjustmentDTO adjustment = new AdjustmentDTO();
			adjustment.setCredit(groupe.getCredit());
			adjustment.setSms(groupe.getSms());
			adjustment.setMinAppel(groupe.getVoix());
			adjustment.setGoData(groupe.getData());

			adjustment.setGroupeId(groupe.getId());
			adjustment.setClientId(groupe.getClientId());
			adjustment.setNumeroVirtuel(groupe.getNumeroClient());
			adjustment.setTargetNumber(employe.getNumero());

			Double tarifSms = employe.getTarifSms() != null ? employe.getTarifSms() : 5;
			Double tarifVoix = employe.getTarifVoix() != null ? employe.getTarifVoix() : 60;
			Double tarifData = employe.getTarifData() != null ? employe.getTarifData() : 500;
			adjustment.setPrice(adjustment.getCredit() + adjustment.getSms() * tarifSms
					+ adjustment.getMinAppel() * tarifVoix + adjustment.getGoData() * tarifData);

			adjustments.add(adjustment);
		}
		return adjustments;
	}

	private void adjustForEmploye(AdjustmentDTO adjustment, String numeroClient) {
		adjustment.setStatus(ActionStatus.INPROGRESS);
		adjustment.setTypeAdjustment(AdjustmentType.PERIODIC);
		adjustment.setDateAdjustment(Instant.now());
		new RepeatableAdjustForEmploye(adjustment, numeroClient);
	}

	private String createSmsContent(AdjustmentDTO adjustment) {
		StringBuilder message = new StringBuilder("Votre employeur vous a envoye ");
		Double credit = adjustment.getCredit() != null ? adjustment.getCredit() : 0;
		Double sms = adjustment.getSms() != null ? adjustment.getSms() : 0;
		Double minAppel = adjustment.getMinAppel() != null ? adjustment.getMinAppel() : 0;
		Double data = adjustment.getGoData() != null ? adjustment.getGoData() : 0;
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

	private class RepeatableAdjustForEmploye {
		private Timer t;
		private AdjustmentDTO adjustment;
		private String numeroClient;

		protected RepeatableAdjustForEmploye(AdjustmentDTO adjustment, String numeroClient) {
			t = new Timer();
			this.adjustment = adjustment;
			this.numeroClient = "221" + numeroClient;
			// retry all 5 minutes
			t.schedule(new AdjustForEmploye(), 0, 1 * 1000);
			// TODO : Bugs
		}

		class AdjustForEmploye extends TimerTask {
			int nbrRepetitions = 1;

			public void run() {
				if ((ActionStatus.INPROGRESS.equals(adjustment.getStatus())
						|| ActionStatus.PENDING.equals(adjustment.getStatus())) && nbrRepetitions <= RETRIES) {
					adjustment.setTrials(nbrRepetitions);
					adjust();
					nbrRepetitions++;
				} else {
					adjustment.setStatus(ActionStatus.FAILED);
					adjustment.setAdjustmentMessage("Periodic resource sharing failed !");
					adjustment = adjustmentService.save(adjustment);
					t.cancel();
				}
			}

			private void adjust() {
				log.debug("Request to adjust employee : {}", adjustment);
				adjustment.setDateAdjustment(Instant.now());
				if (ActionStatus.FAILED.equals(adjustment.getStatus())) {
					t.cancel();
				} else if (ActionStatus.SUCCESS.equals(adjustment.getStatus())) {
					t.cancel();
				} else {
					BalanceInfoDTO balanceInfoDTO = balanceInfoService.getBalanceInfo(numeroClient);
					if (adjustment.getPrice() > balanceInfoDTO.getCredit()) {
						log.warn("Client's virtual number {} does not have enough credit.", numeroClient);
						adjustment.setStatus(ActionStatus.FAILED);
						adjustment.setAdjustmentMessage("Periodic resource sharing failed !");
						adjustment = adjustmentService.save(adjustment);
						t.cancel();
					} else {
						String targetNumber = "221" + adjustment.getTargetNumber();
						boolean accountAdjusted = false;
						boolean resourcesShared = false;
						Double adjustPrice = adjustment.getPrice();
						try {
							// défalquer le prix du produit du compte client
							accountAdjusted = adjustAccountService.adjustCreditAccount(numeroClient, -adjustPrice);

							Double credit = adjustment.getCredit() != null ? adjustment.getCredit() : 0;
							Double sms = adjustment.getSms() != null ? adjustment.getSms() : 0;
							Double minAppel = adjustment.getMinAppel() != null ? adjustment.getMinAppel() : 0;
							Double data = adjustment.getGoData() != null ? adjustment.getGoData() : 0;
							if (credit != 0) {
								adjustAccountService.adjustCreditAccount(targetNumber, credit);
							}
							if (sms != 0) {
								adjustAccountService.adjustSMSAccount(targetNumber, sms);
							}
							if (minAppel != 0) {
								adjustAccountService.adjustAppelAccount(targetNumber, minAppel);
							}
							if (data != 0) {
								adjustAccountService.adjustDataAccount(targetNumber, data);
							}
							resourcesShared = true;

							// save adjustment
							adjustment.setStatus(ActionStatus.SUCCESS);
							adjustment.setAdjustmentMessage("Periodic resource sharing success !");
							adjustment = adjustmentService.save(adjustment);
							// notify employee
							NotificationDTO notif = new NotificationDTO();
							notif.setTo(targetNumber);
							notif.setText(createSmsContent(adjustment));
							smsNotificationService.sendSms(notif);
							/*
							 * sendNotificationService.sendSms(targetNumber, createSmsContent(adjustment),
							 * "B2BSelfcare - Partage periodique de ressources");
							 */
							t.cancel();
						} catch (Exception e) {
							log.warn("Error while adjusting resources for number {} - price {} (cause : {})",
									targetNumber, adjustPrice, e.getMessage());
							if (accountAdjusted && !resourcesShared) { // remettre le crédit défalqué dans le compte
																		// client
								try {
									adjustAccountService.adjustCreditAccount(numeroClient, adjustPrice);
									log.info("Readjustment done for number {} - price {}", targetNumber, adjustPrice);
								} catch (Exception e1) {
									log.warn("Fail to readjust account : number {} - price {} (cause : {})",
											numeroClient, adjustPrice, e1.getMessage());
								}
							}
							adjustment.setStatus(ActionStatus.PENDING);
							adjustment = adjustmentService.save(adjustment);
						}
					}

				}
			}
		}
	}

}
