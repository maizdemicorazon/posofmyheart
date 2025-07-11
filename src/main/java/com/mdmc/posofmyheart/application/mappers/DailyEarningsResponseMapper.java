package com.mdmc.posofmyheart.application.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;

@Mapper
public interface DailyEarningsResponseMapper {
    DailyEarningsResponseMapper INSTANCE = Mappers.getMapper(DailyEarningsResponseMapper.class);

    default DailyEarningsResponse toResponse(Set<DailyEarnings> dailyEarnings) {
        return DailyEarningsResponse
                .builder()
                .totalCount(dailyEarnings.size())
                .dailyEarnings(dailyEarnings)
                .build();
    }
}
