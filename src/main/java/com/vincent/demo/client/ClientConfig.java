package com.vincent.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ClientConfig {

    @Bean
    public IpApiClient ipApiClient(
            @Value("${client.timeout.second}") int timeoutSecond
    ) {
        var restTemplate = new RestTemplateBuilder()
                .rootUri("https://ipapi.co")
                .setConnectTimeout(Duration.ofSeconds(timeoutSecond))
                .build();
        return new IpApiClient(restTemplate);
    }

    @Bean
    public CurrencyLayerClient currencyLayerClient(
            @Value("${client.timeout.second}") int timeoutSecond,
            @Value("${currency-layer-client.access-key}") String accessKey
    ) {
        var restTemplate = new RestTemplateBuilder()
                .rootUri("http://apilayer.net/api")
                .setConnectTimeout(Duration.ofSeconds(timeoutSecond))
                .build();
        return new CurrencyLayerClient(restTemplate, accessKey);
    }
}
