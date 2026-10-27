package com.stockBridge.inventory.infrastructure.monitoring;

import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

// Servicio de metricas personalizadas con Micrometer
@Component
public class InventoryMetrics {

    private final Counter requestsCreatedCounter;
    private final Counter requestsApprovedCounter;
    private final Counter requestsRejectedCounter;
    private final Counter requestsDeliveredCounter;
    private final Timer supplierExternalCallTimer;

    public InventoryMetrics(MeterRegistry registry, ProductRepositoryPort productRepository) {
        this.requestsCreatedCounter = Counter.builder("stockbridge.requests.created")
                .description("Total de solicitudes internas creadas")
                .register(registry);

        this.requestsApprovedCounter = Counter.builder("stockbridge.requests.approved")
                .description("Total de solicitudes aprobadas")
                .register(registry);

        this.requestsRejectedCounter = Counter.builder("stockbridge.requests.rejected")
                .description("Total de solicitudes rechazadas")
                .register(registry);

        this.requestsDeliveredCounter = Counter.builder("stockbridge.requests.delivered")
                .description("Total de solicitudes entregadas y despachadas")
                .register(registry);

        this.supplierExternalCallTimer = Timer.builder("stockbridge.supplier.call.time")
                .description("Tiempo de respuesta de las consultas al proveedor externo")
                .register(registry);

        // Gauge para monitorear productos con existencias criticas (menor a 5 unidades)
        registry.gauge("stockbridge.products.low_stock.count", productRepository,
                repo -> repo.findByStockLessThan(5).size());
    }

    public void incrementRequestsCreated() {
        requestsCreatedCounter.increment();
    }

    public void incrementRequestsApproved() {
        requestsApprovedCounter.increment();
    }

    public void incrementRequestsRejected() {
        requestsRejectedCounter.increment();
    }

    public void incrementRequestsDelivered() {
        requestsDeliveredCounter.increment();
    }

    public <T> T recordSupplierCall(Callable<T> callable) throws Exception {
        return supplierExternalCallTimer.recordCallable(callable);
    }
}
