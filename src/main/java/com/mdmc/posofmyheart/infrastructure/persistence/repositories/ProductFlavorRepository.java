package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFlavorRepository  extends JpaRepository<ProductFlavorEntity, Long> {
}
