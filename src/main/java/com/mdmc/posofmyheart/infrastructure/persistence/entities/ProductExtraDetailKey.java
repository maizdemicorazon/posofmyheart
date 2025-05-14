package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ProductExtraDetailKey implements Serializable {
    @Column(name = "id_extra")
    private Long idExtra;
    @Column(name = "id_order_detail")
    private Long idOrderDetail;
}
