package com.stockBridge.inventory.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

// Configuracion del cliente HTTP RestClient con timeouts de conexion y lectura
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient supplierRestClient(
            @Value("${application.services.supplier.base-url}") String baseUrl,
            @Value("${application.services.supplier.timeout-ms:5000}") int timeoutMs) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        requestFactory.setReadTimeout(Duration.ofMillis(timeoutMs));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
