package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.Sauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.SauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SauceMapper {

    SauceMapper INSTANCE = Mappers.getMapper(SauceMapper.class);

    Sauce toDomainSauce(SauceEntity sauce);

    default List<Sauce> toDomainSauces(List<SauceEntity> sauces) {
        return sauces.stream()
                .map(this::toDomainSauce)
                .sorted(Comparator.comparing(Sauce::idSauce))
                .collect(Collectors.toList());
    }

}
