package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderExtraDetailKey implements Serializable {

    @Column(name = "id_extra")
    private Long idExtra;

    @Column(name = "id_order_detail")
    private Long idOrderDetail;

}