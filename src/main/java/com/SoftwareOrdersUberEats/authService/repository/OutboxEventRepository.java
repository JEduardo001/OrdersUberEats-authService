package com.SoftwareOrdersUberEats.authService.repository;

import com.SoftwareOrdersUberEats.authService.entity.OutboxEventEntity;
import com.SoftwareOrdersUberEats.authService.enums.StatusEventEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findAllByStatusEvent(StatusEventEnum status);
}
