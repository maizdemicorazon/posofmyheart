package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boing_options", indexes = {
        @Index(name = "idx_boing_option_id_product", columnList = "id_product")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoingOptionEntity extends FlavorOptions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boing")
    private Long idBoing;
}
