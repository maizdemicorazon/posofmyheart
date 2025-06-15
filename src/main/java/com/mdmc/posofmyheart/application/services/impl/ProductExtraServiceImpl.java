package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ProductExtraNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductExtraMapper;
import com.mdmc.posofmyheart.application.services.ProductExtraService;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductExtraServiceImpl implements ProductExtraService {

    private ProductExtraRepository productExtraRepository;

    @Override
    public ProductExtra getExtraById(Long idExtra) {
        return ProductExtraMapper.INSTANCE.toProductExtra(
                productExtraRepository.findById(idExtra)
                        .orElseThrow(() -> new ProductExtraNotFoundException(idExtra))
        );
    }

    @Override
    public List<ProductExtra> getAllExtras() {
        return ProductExtraMapper.INSTANCE.toProductExtras(productExtraRepository.findAll());
    }

}
