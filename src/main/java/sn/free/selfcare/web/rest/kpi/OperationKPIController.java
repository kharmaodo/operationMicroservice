package sn.free.selfcare.web.rest.kpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sn.free.selfcare.service.FactureService;
import sn.free.selfcare.service.PaiementService;
import sn.free.selfcare.service.dto.AmountPaidBillKpiDTO;
import sn.free.selfcare.service.dto.CreditValuePurchasedKpiDTO;
import sn.free.selfcare.service.dto.NumberPaidBillKpiDTO;

import java.util.List;

@RestController
@RequestMapping("/kpi")
public class OperationKPIController {

    @Autowired
    FactureService factureService;

    @Autowired
    PaiementService paiementService;

    @GetMapping("/numberOfPaidBillsPerMonth")
    public List<NumberPaidBillKpiDTO> getNumberOfPaidBillsPerMonth(@RequestParam(name = "year", required = true) int year) {
        return factureService.getNumberOfPaidBillsPerMonth(year);
    }

    @GetMapping("/creditValuePurchasedPerMonth")
    public List<CreditValuePurchasedKpiDTO> getCreditValuePurchasedPerMonth(@RequestParam(name = "year", required = true) int year) {
        return paiementService.getCreditValuePurchasedPerMonth(year);
    }

    @GetMapping("/amountOfPaidBillsPerMonth")
    public List<AmountPaidBillKpiDTO> getAmountOfPaidBillsPerMonth(@RequestParam(name = "year", required = true) int year) {
        return factureService.getAmountOfPaidBillsPerMonth(year);
    }
}
