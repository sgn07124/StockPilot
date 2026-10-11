package com.stockpilot.infra.market;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MarketDataConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "kisMarketDataClient")
    public MarketDataClient primaryMarketDataClient(@Qualifier("mockMarketDataClient") MarketDataClient mockClient) {
        return mockClient;
    }
}
