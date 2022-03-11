package sn.free.selfcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sn.free.selfcare.apiclient.service.BalanceInfoService;

@Component
public class AlpiClientConfiguration {
    @Bean
    public BalanceInfoService balanceInfoService() {
        return new BalanceInfoService();
    }
}
