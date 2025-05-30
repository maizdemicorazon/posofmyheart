package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "water_options", indexes = {
        @Index(name = "idx_water_option_id_product", columnList = "id_product")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaterOptionEntity extends FlavorOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_water")
    private Long idWater;
}
