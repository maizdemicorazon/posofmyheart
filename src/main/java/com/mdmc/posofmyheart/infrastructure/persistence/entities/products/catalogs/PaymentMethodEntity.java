package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;

@Entity
@Table(name = "payment_methods", indexes = {
        @Index(name = "idx_payment_method_name", columnList = "name"),
        @Index(name = "idx_payment_method_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_method")
    private Long idPaymentMethod;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active = true;
}
