package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.exception.DuplicateResourceException;
import com.stockBridge.inventory.domain.exception.ResourceNotFoundException;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Pruebas unitarias para la logica y reglas de negocio de ProductService
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ProductService productService;

    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProduct = new Product(
                1L, "TEST-001", "Laptop Test", "Descripcion prueba",
                "COMPUTO", BigDecimal.valueOf(1000.00), 10, true
        );
    }

    @Test
    @DisplayName("Debe registrar un producto valido y activarlo por defecto")
    void createProduct_Success() {
        when(productRepository.existsByCode("TEST-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);

        Product created = productService.create(validProduct);

        assertNotNull(created);
        assertEquals("TEST-001", created.getCode());
        assertTrue(created.getActive());
        verify(productRepository).save(validProduct);
    }

    @Test
    @DisplayName("Debe rechazar la creacion cuando el codigo de producto ya existe")
    void createProduct_DuplicateCode_ThrowsDuplicateResourceException() {
        when(productRepository.existsByCode("TEST-001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productService.create(validProduct));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el precio es menor o igual a cero")
    void createProduct_InvalidPrice_ThrowsIllegalArgumentException() {
        validProduct.setPrice(BigDecimal.ZERO);
        when(productRepository.existsByCode("TEST-001")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> productService.create(validProduct));
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el stock es negativo")
    void createProduct_NegativeStock_ThrowsIllegalArgumentException() {
        validProduct.setStock(-5);
        when(productRepository.existsByCode("TEST-001")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> productService.create(validProduct));
    }

    @Test
    @DisplayName("Debe alternar correctamente el estado activo del producto")
    void toggleActive_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = productService.toggleActive(1L);

        assertFalse(updated.getActive());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el producto a consultar no existe")
    void getById_NotFound_ThrowsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }
}
