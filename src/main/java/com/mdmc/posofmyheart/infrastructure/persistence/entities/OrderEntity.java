package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_date", columnList = "order_date"),
        @Index(name = "idx_order_payment_method", columnList = "id_payment_method"),
        @Index(name = "idx_order_total_amount", columnList = "total_amount")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long idOrder;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "id_payment_method", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetailEntity detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }

}
