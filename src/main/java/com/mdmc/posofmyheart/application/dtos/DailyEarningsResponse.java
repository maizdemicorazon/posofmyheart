package com.mdmc.posofmyheart.application.dtos;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyEarningsResponse {
    private int totalCount;
    private Set<DailyEarnings> dailyEarnings;
}
