package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
