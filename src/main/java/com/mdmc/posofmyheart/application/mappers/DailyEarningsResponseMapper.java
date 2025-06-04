package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DailyEarningsResponseMapper {
    DailyEarningsResponseMapper INSTANCE = Mappers.getMapper(DailyEarningsResponseMapper.class);

    default DailyEarningsResponse toResponse(List<DailyEarnings> dailyEarnings) {
        return DailyEarningsResponse
                .builder()
                .totalCount(dailyEarnings.size())
                .dailyEarnings(dailyEarnings)
                .build();
    }
}
