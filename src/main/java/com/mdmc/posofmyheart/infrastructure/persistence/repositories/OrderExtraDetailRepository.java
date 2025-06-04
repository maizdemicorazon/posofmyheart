package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtraDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderExtraDetailRepository extends JpaRepository<OrderExtraDetailEntity, OrderExtraDetailKey> {
}
