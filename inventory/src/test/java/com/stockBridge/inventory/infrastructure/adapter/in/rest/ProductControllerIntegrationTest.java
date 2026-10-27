package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.port.in.ProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de integracion REST con MockMvc y seguridad basada en roles
@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"
})
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductUseCase productUseCase;

    @MockitoBean
    private DataSource dataSource;

    @Test
    @DisplayName("ROLE_ADMIN puede crear productos exitosamente (201 Created)")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void createProduct_AsAdmin_Success() throws Exception {
        Product mockProduct = new Product(1L, "PRD-99", "Teclado RGB", "Mecanico", "ACCESORIOS", BigDecimal.valueOf(50), 10, true);
        when(productUseCase.create(any(Product.class))).thenReturn(mockProduct);

        String json = """
                {
                  "code": "PRD-99",
                  "name": "Teclado RGB",
                  "description": "Mecanico",
                  "category": "ACCESORIOS",
                  "price": 50.00,
                  "stock": 10
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PRD-99"))
                .andExpect(jsonPath("$.name").value("Teclado RGB"));
    }

    @Test
    @DisplayName("ROLE_EMPLEADO es rechazado al intentar crear productos (403 Forbidden)")
    @WithMockUser(username = "empleado", authorities = {"ROLE_EMPLEADO"})
    void createProduct_AsEmpleado_ReturnsForbidden() throws Exception {
        String json = """
                {
                  "code": "PRD-99",
                  "name": "Teclado RGB",
                  "description": "Mecanico",
                  "category": "ACCESORIOS",
                  "price": 50.00,
                  "stock": 10
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Forbidden"));
    }

    @Test
    @DisplayName("Usuario sin autenticar es rechazado con 401 Unauthorized")
    void getProducts_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"));
    }

    @Test
    @DisplayName("Cualquier usuario autenticado puede consultar productos (200 OK)")
    @WithMockUser(username = "empleado", authorities = {"ROLE_EMPLEADO"})
    void getProducts_Authenticated_Success() throws Exception {
        Product p = new Product(1L, "PRD-01", "Mouse", "Optico", "ACCESORIOS", BigDecimal.valueOf(20), 5, true);
        when(productUseCase.getAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("PRD-01"));
    }
}
