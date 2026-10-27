package com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository;

import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA para movimientos con @EntityGraph para evitar N+1
@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"product", "request", "request.user"})
    List<MovementEntity> findAll();

    @EntityGraph(attributePaths = {"product", "request", "request.user"})
    List<MovementEntity> findByProductId(Long productId);
}
