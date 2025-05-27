package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
