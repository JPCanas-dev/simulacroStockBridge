package com.stockBridge.inventory.infrastructure.monitoring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// HealthIndicator personalizado para monitorear el estado del microservicio externo de proveedores
@Component
public class SupplierServiceHealthIndicator implements HealthIndicator {

    private final RestClient supplierRestClient;
    private final String baseUrl;

    public SupplierServiceHealthIndicator(RestClient supplierRestClient,
                                          @Value("${application.services.supplier.base-url}") String baseUrl) {
        this.supplierRestClient = supplierRestClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Health health() {
        try {
            // Intenta verificar conexion con el microservicio externo
            supplierRestClient.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .toBodilessEntity();

            return Health.up()
                    .withDetail("supplierServiceUrl", baseUrl)
                    .withDetail("status", "AVAILABLE")
                    .build();

        } catch (Exception ex) {
            return Health.down()
                    .withDetail("supplierServiceUrl", baseUrl)
                    .withDetail("status", "UNAVAILABLE")
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
