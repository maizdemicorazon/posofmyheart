package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderExtraDetailKey implements Serializable {
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @Column(name = "id_extra")
    private Long idExtra;
}