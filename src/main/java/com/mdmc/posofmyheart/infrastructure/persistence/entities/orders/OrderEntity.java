package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.mdmc.posofmyheart.domain.OrderStatusEnum;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_order_payment_method", columnList = "id_payment_method"),
        @Index(name = "idx_order_total_amount", columnList = "total_amount"),
        @Index(name = "idx_order_status", columnList = "status") // <-- Índice agregado aquí
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
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long idOrder;

    @Column(name = "client_name", length = 40)
    private String clientName;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatusEnum status = OrderStatusEnum.RECEIVED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_method", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderDetailEntity> orderDetails = new HashSet<>();

    // Helper method para agregar order detail
    public void addOrderDetail(OrderDetailEntity detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }

}