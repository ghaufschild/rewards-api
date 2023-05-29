package com.example.rewardswebapi.records;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerTransaction(UUID customerId, LocalDateTime transactionTimestamp, Integer totalPriceCents) { }
