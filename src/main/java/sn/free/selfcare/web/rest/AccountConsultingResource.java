package sn.free.selfcare.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;
import sn.free.selfcare.apiclient.dto.BalanceInfoDTO;
import sn.free.selfcare.apiclient.service.BalanceInfoService;

import javax.annotation.PostConstruct;

/**
 * REST controller for managing {@link sn.free.selfcare.domain.PeriodeEnvoi}.
 */
@RestController
@RequestMapping("/api")
public class AccountConsultingResource {

	private final Logger log = LoggerFactory.getLogger(AccountConsultingResource.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

    @Value("${services.balanceInfo.url}")
    private String balanceInfoURL;

    @Autowired
	private BalanceInfoService balanceInfoService;

    @PostConstruct
    public void init() {
        log.info("Init -> Start");

        log.info("balanceInfoURL = {}", balanceInfoURL);
        this.balanceInfoService.setBalanceInfoURL(balanceInfoURL);

        log.info("Init -> End");
    }

	@GetMapping("/consulting")
	public ResponseEntity<BalanceInfoDTO> getBalanceInfo(@RequestParam String number) {
		log.debug("REST request to get BalanceInfo : {}", number);
		Optional<BalanceInfoDTO> balanceInfoDTO = Optional.of(this.balanceInfoService.getBalanceInfo(number));
		return ResponseUtil.wrapOrNotFound(balanceInfoDTO);
	}
}
