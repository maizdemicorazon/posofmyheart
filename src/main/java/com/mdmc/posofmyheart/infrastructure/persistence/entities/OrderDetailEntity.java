package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "id_variant", nullable = false)
    private ProductVariantEntity variant;

    // Relación con múltiples salsas
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailSauceEntity> sauceDetails = new ArrayList<>();

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderExtrasDetailEntity> extraDetails = new ArrayList<>();

    public void addExtraDetail(OrderExtrasDetailEntity extraDetail) {
        extraDetails.add(extraDetail);
        extraDetail.setOrderDetail(this);
    }

    public void clearSauces() {
        this.sauceDetails.clear();
    }

    // Método helper para agregar salsas
    public void addSauce(SauceEntity sauce) {
        OrderDetailSauceEntity detailSauce = new OrderDetailSauceEntity();
        detailSauce.setOrderDetailSauceKey(new OrderDetailSauceKey(this.idOrderDetail, sauce.getIdSauce()));
        detailSauce.setOrderDetail(this);
        detailSauce.setSauce(sauce);
        this.sauceDetails.add(detailSauce);
    }

}
