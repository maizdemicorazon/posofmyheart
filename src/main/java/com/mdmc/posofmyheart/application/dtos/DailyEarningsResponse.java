package com.mdmc.posofmyheart.application.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyEarningsResponse {
    private int totalCount;
    private List<DailyEarnings> dailyEarnings;
}
