package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sauces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SauceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sauce")
    private Long idSauce;
    private String name;
    private String description;
    private String image;
}
