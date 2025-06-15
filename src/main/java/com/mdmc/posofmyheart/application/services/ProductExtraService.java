package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.ProductExtra;

import java.util.List;

public interface ProductExtraService {
    ProductExtra getExtraById(Long idExtra);

    List<ProductExtra> getAllExtras();
}
