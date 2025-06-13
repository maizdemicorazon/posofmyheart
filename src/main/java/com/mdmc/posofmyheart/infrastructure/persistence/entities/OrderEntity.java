package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long idOrder;
    @Column(name = "client_name", length = 40)
    String clientName;

    @Builder.Default
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "id_payment_method", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @Column(name = "comment")
    private String comment;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetailEntity detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }

}
