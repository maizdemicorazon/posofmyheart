package com.mdmc.posofmyheart.domain.models;

import java.util.List;

public record Product(
        Long idProduct,
        Long idCategory,
        String name,
        String image,
        List<ProductVariant> options,
        List<ProductFlavor> flavors) {

    public Product {
        if (idProduct == null || idProduct <= 0) {
            throw new IllegalArgumentException("ID de producto inválido");
        }
        if (idCategory == null || idCategory <= 0) {
            throw new IllegalArgumentException("idCategory de producto inválido");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nombre de producto inválido");
        }
    }

}
