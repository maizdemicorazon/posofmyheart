package com.mdmc.posofmyheart.application.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyEarningsResponse {
    private int totalCount;
    private Set<DailyEarnings> dailyEarnings;
}
