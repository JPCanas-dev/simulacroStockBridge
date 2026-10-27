package com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository;

import com.stockBridge.inventory.domain.model.RequestStatus;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.RequestEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio JPA para solicitudes usando @EntityGraph para evitar el problema N+1
@Repository
public interface RequestJpaRepository extends JpaRepository<RequestEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "product"})
    Optional<RequestEntity> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"user", "product"})
    List<RequestEntity> findAll();

    @EntityGraph(attributePaths = {"user", "product"})
    List<RequestEntity> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "product"})
    List<RequestEntity> findByStatus(RequestStatus status);
}
