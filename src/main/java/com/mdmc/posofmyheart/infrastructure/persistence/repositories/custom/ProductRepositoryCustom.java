package com.mdmc.posofmyheart.infrastructure.persistence.repositories.custom;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Object[]> getAvailableProducts();
}