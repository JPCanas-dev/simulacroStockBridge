package com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository;

import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio JPA para productos con soporte de bloqueo pesimista y consultas JPQL
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByCode(String code);

    boolean existsByCode(String code);

    // Consulta JPQL para productos por debajo del umbral de stock
    @Query("SELECT p FROM ProductEntity p WHERE p.stock < :minStock")
    List<ProductEntity> findByStockLessThan(@Param("minStock") int minStock);

    // Bloqueo pesimista para resolver concurrencia en aprobacion/entrega simultanea
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithLock(@Param("id") Long id);
}
