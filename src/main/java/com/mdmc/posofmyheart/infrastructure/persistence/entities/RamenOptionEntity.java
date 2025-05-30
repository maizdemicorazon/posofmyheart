package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ramen_options", indexes = {
        @Index(name = "idx_ramen_option_id_product", columnList = "id_product")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RamenOptionEntity extends FlavorOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ramen")
    private Long idRamen;
}
