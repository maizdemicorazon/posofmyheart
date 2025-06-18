package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

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
