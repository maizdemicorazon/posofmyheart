package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
}