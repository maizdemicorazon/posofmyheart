package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailKey;

public interface OrderExtraDetailRepository extends JpaRepository<OrderExtraDetailEntity, OrderExtraDetailKey> {
}
