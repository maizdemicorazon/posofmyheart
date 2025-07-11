package com.mdmc.posofmyheart.application.services;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductExtra;

public interface ProductExtraService {
    ProductExtra getExtraById(Long idExtra);

    List<ProductExtra> getAllExtras();
}
