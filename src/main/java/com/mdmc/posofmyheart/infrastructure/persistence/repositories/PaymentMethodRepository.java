package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {

    @Query("""
            SELECT new com.mdmc.posofmyheart.domain.models.PaymentMethod(
                pm.idPayment,
                pm.name
            )
            FROM PaymentMethodEntity pm
             ORDER BY pm.idPayment ASC
            """)
    List<PaymentMethod> findAllPaymentMethods();
}