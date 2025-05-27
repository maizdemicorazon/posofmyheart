package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtraDetailKey;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtrasDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderExtraDetailRepository extends JpaRepository<OrderExtrasDetailEntity, OrderExtraDetailKey> {
}
