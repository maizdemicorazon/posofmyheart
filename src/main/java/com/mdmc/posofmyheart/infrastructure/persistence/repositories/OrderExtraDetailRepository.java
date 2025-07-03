package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderExtraDetailRepository extends JpaRepository<OrderExtraDetailEntity, OrderExtraDetailKey> {
}
