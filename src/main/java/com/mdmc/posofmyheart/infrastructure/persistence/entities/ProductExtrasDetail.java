package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_extras_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductExtrasDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra_detail")
    private Integer idExtraDetail;
    @Column(name = "id_extra", nullable = false)
    private Integer idExtra;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
