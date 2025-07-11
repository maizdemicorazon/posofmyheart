package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {

    @Query("""
            SELECT new com.mdmc.posofmyheart.domain.models.PaymentMethod(
                pm.idPaymentMethod,
                pm.name
            )
            FROM PaymentMethodEntity pm
             ORDER BY pm.idPaymentMethod ASC
            """)
    List<PaymentMethod> findAllPaymentMethods();
}