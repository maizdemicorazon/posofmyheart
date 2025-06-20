package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_date", columnList = "order_date"),
        @Index(name = "idx_order_payment_method", columnList = "id_payment_method"),
        @Index(name = "idx_order_total_amount", columnList = "total_amount")
})
@NamedEntityGraphs({
        // EntityGraph básico: Solo carga payment method
        @NamedEntityGraph(
                name = "Order.basic",
                attributeNodes = {
                        @NamedAttributeNode("paymentMethod")
                }
        ),

        // EntityGraph intermedio: Carga order details básicos sin relaciones profundas
        @NamedEntityGraph(
                name = "Order.withOrderDetails",
                attributeNodes = {
                        @NamedAttributeNode("paymentMethod"),
                        @NamedAttributeNode(value = "orderDetails", subgraph = "orderDetail.basic")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "orderDetail.basic",
                                attributeNodes = {
                                        @NamedAttributeNode("product"),
                                        @NamedAttributeNode("variant")
                                }
                        )
                }
        ),

        // EntityGraph completo: Carga TODAS las relaciones para eliminar N+1
        @NamedEntityGraph(
                name = "Order.withCompleteDetails",
                attributeNodes = {
                        @NamedAttributeNode("paymentMethod"),
                        @NamedAttributeNode(value = "orderDetails", subgraph = "orderDetail.complete")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "orderDetail.complete",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "product", subgraph = "product.withCategory"),
                                        @NamedAttributeNode("variant"),
                                        @NamedAttributeNode(value = "extraDetails", subgraph = "extraDetail.complete"),
                                        @NamedAttributeNode(value = "sauceDetails", subgraph = "sauceDetail.complete"),
                                        @NamedAttributeNode(value = "flavorDetails", subgraph = "flavorDetail.complete")
                                }
                        ),
                        @NamedSubgraph(
                                name = "product.withCategory",
                                attributeNodes = {
                                        @NamedAttributeNode("category")
                                }
                        ),
                        @NamedSubgraph(
                                name = "extraDetail.complete",
                                attributeNodes = {
                                        @NamedAttributeNode("productExtra")
                                }
                        ),
                        @NamedSubgraph(
                                name = "sauceDetail.complete",
                                attributeNodes = {
                                        @NamedAttributeNode("productSauce")
                                }
                        ),
                        @NamedSubgraph(
                                name = "flavorDetail.complete",
                                attributeNodes = {
                                        @NamedAttributeNode("flavor")
                                }
                        )
                }
        ),

        // EntityGraph para reportes: Solo datos necesarios para cálculos
        @NamedEntityGraph(
                name = "Order.forReports",
                attributeNodes = {
                        @NamedAttributeNode("paymentMethod"),
                        @NamedAttributeNode(value = "orderDetails", subgraph = "orderDetail.forCalculations")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "orderDetail.forCalculations",
                                attributeNodes = {
                                        @NamedAttributeNode("product"),
                                        @NamedAttributeNode("variant"),
                                        @NamedAttributeNode("extraDetails")
                                }
                        )
                }
        )
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
    private String clientName;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_method", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderDetailEntity> orderDetails;

    // Helper method para agregar order detail
    public void addOrderDetail(OrderDetailEntity detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }

    // Helper method para verificar si tiene comentarios
    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        orderDetails = new HashSet<>();
        updatedAt = LocalDateTime.now();
    }
}