package com.example.rewardswebapi.records;

import java.util.List;

public record CustomerRewards(List<MonthlyRewards> monthlyRewards, Long totalPoints) {}
