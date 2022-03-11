/**
 *
 */
package sn.free.selfcare.service.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sn.free.selfcare.apiclient.dto.BalanceInfoDTO;
import sn.free.selfcare.apiclient.service.BalanceInfoService;
import sn.free.selfcare.service.MailService;
import sn.free.selfcare.service.SmsNotificationService;
import sn.free.selfcare.service.dto.AlertDTO;
import sn.free.selfcare.service.dto.NotificationDTO;
import sn.free.selfcare.service.dto.SeuilEmployeDTO;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This task executes the automatic adjustment for a group without human
 * intervention. It is run by a job scheduled periodically.
 *
 * @author MSF
 *
 */
public class AutomaticAlert implements Runnable {

    private final Logger log = LoggerFactory.getLogger(AutomaticAlert.class);
    MailService mailService;
    private AlertDTO alertDTO;
    private BalanceInfoService balanceInfoService;
    private SmsNotificationService smsNotificationService;

    public AutomaticAlert(BalanceInfoService balanceInfoService, MailService mailService, SmsNotificationService smsNotificationService, AlertDTO alertDTO) {
        this.log.debug("Initializing Alert job {}", alertDTO);
        this.alertDTO = alertDTO;
        this.balanceInfoService = balanceInfoService;
        this.mailService = mailService;
        this.smsNotificationService = smsNotificationService;
    }

    @Override
    public void run() {
        this.log.debug("Executing Alert job {}", alertDTO);
        this.alertForGestionnaire();
        Map<String, Map<String, Double>> seuilsAndSoldesByNumero = this.getSeuilsAndSoldesByNumero();
        seuilsAndSoldesByNumero.forEach((numero, seuilsAndSoldes) -> alertForEmploye(numero, seuilsAndSoldes));
    }

    private Map<String, Map<String, Double>> getSeuilsAndSoldesByNumero() {
        Map<String, Map<String, Double>> seuilsAndSoldesByNumero = new HashMap<String, Map<String, Double>>();
        Set<SeuilEmployeDTO> seuilEmployes = alertDTO.getSeuilEmployes();
        seuilEmployes.stream()
            .filter(seuilEmploye -> seuilEmploye.getNumero() != null && !seuilEmploye.getNumero().isEmpty())
            .forEach((seuilEmploye) -> {
                String numero = "221" + seuilEmploye.getNumero();
                Map<String, Double> seuilsAndSoldes = new HashMap<String, Double>();

                seuilsAndSoldes.put("seuilCredit", seuilEmploye.getSeuilCredit());
                seuilsAndSoldes.put("seuilData", seuilEmploye.getSeuilData());

                BalanceInfoDTO balanceInfo = this.balanceInfoService.getBalanceInfo(numero);
                if (balanceInfo != null) {
                    seuilsAndSoldes.put("credit", balanceInfo.getCredit());
                    seuilsAndSoldes.put("data", balanceInfo.getData());
                }

                seuilsAndSoldesByNumero.put(numero, seuilsAndSoldes);
            });
        return seuilsAndSoldesByNumero;
    }

    private void alertForEmploye(String numero, Map<String, Double> seuilsAndSoldes) {
        Double seuilCredit = seuilsAndSoldes.get("seuilCredit");
        Double seuilData = seuilsAndSoldes.get("seuilData");
        Double credit = seuilsAndSoldes.get("credit");
        Double data = seuilsAndSoldes.get("data");
        this.log.debug("Alert numero = {}, seuilCredit = {}, seuilData = {}, credit = {}, data = {}", numero,
            seuilCredit, seuilData, credit, data);
        if (seuilCredit != null && seuilCredit != 0 && credit < seuilCredit) {
            NotificationDTO notif = new NotificationDTO();
            notif.setTo(numero);
            notif.setText("Il ne vous reste plus que " + credit + " FCFA de credit.");
            smsNotificationService.sendSms(notif);
            /*this.smsNotificationService.sendSms(numero, "Il ne vous reste plus que " + credit + " FCFA de credit.", "B2BSelfcare - Credit Alert");*/
        }
        if (seuilData != null && seuilData != 0 && data < seuilData) {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_DOWN);

            NotificationDTO notif = new NotificationDTO();
            notif.setTo(numero);
            notif.setText("Il ne vous reste plus que " + df.format(data) + " Go de donnees.");
            smsNotificationService.sendSms(notif);
            /*this.smsNotificationService.sendSms(numero, "Il ne vous reste plus que " + df.format(data) + " Go de donnees.", "B2BSelfcare - Data Alert");*/
        }
    }

    private void alertForGestionnaire() {
        String numeroVirtuel = "221" + alertDTO.getNumeroVirtuel();
        BalanceInfoDTO balanceInfo = this.balanceInfoService.getBalanceInfo(numeroVirtuel);
        if (balanceInfo != null) {
            Double stock = balanceInfo.getCredit();
            Double seuilStock = alertDTO.getSeuilStock();
            if (seuilStock != null && seuilStock != 0 && stock < seuilStock) {
                String stockFormat = String.format("%,.0f", stock);
                String seuilStockFormat = String.format("%,.0f", seuilStock);
                this.mailService.sendStockAlertEmail(alertDTO.getGestionnaire(), numeroVirtuel, stockFormat, seuilStockFormat);
            }
        }
    }

}
