package com.stockBridge.inventory.infrastructure.adapter.out.external;

import com.stockBridge.inventory.domain.exception.ExternalServiceException;
import com.stockBridge.inventory.domain.model.SupplierAvailability;
import com.stockBridge.inventory.domain.port.out.SupplierClientPort;
import com.stockBridge.inventory.infrastructure.adapter.out.external.dto.SupplierCheckRequest;
import com.stockBridge.inventory.infrastructure.adapter.out.external.dto.SupplierCheckResponse;
import com.stockBridge.inventory.infrastructure.monitoring.InventoryMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

// Adaptador que implementa SupplierClientPort comunicandose via HTTP con el servicio externo
@Component
public class SupplierRestClientAdapter implements SupplierClientPort {

    private static final Logger log = LoggerFactory.getLogger(SupplierRestClientAdapter.class);
    private final RestClient supplierRestClient;
    private final InventoryMetrics metrics;

    public SupplierRestClientAdapter(RestClient supplierRestClient, InventoryMetrics metrics) {
        this.supplierRestClient = supplierRestClient;
        this.metrics = metrics;
    }

    @Override
    public SupplierAvailability checkAvailability(String productCode, int quantity, String category) {
        SupplierCheckRequest requestBody = new SupplierCheckRequest(productCode, quantity, category);

        try {
            log.info("Consultando disponibilidad externa para producto {} cantidad {}", productCode, quantity);

            return metrics.recordSupplierCall(() -> {
                SupplierCheckResponse response = supplierRestClient.post()
                        .uri("/api/suppliers/check-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .body(SupplierCheckResponse.class);

                if (response == null) {
                    throw new ExternalServiceException("El proveedor devolvio una respuesta vacia");
                }

                return new SupplierAvailability(
                        response.supplierCode(),
                        response.available(),
                        response.availableQuantity(),
                        response.estimatedDeliveryDays()
                );
            });

        } catch (ResourceAccessException ex) {
            // Manejo de timeout o caida de red del servicio de proveedores
            log.error("Timeout o conexion fallida con el servicio de proveedores: {}", ex.getMessage());
            throw new ExternalServiceException("El servicio de proveedores no responde o supero el tiempo limite de espera", ex);

        } catch (RestClientResponseException ex) {
            // Manejo de respuestas HTTP 4xx o 5xx del servicio de proveedores
            log.error("Error HTTP devuelto por el servicio de proveedores: status={}", ex.getStatusCode());
            throw new ExternalServiceException("El servicio de proveedores devolvio un error: " + ex.getStatusCode(), ex);

        } catch (ExternalServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Fallo inesperado al consultar servicio de proveedores: {}", ex.getMessage());
            throw new ExternalServiceException("Fallo al comunicarse con el servicio de proveedores", ex);
        }
    }
}
