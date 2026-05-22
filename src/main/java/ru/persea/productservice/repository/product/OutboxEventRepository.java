package ru.persea.productservice.repository.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.persea.productservice.entity.outbox.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    
}
