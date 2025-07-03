package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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