package com.example.BBA.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BusResponse(
        Long id,
        String busName,
        String source,
        String destination,
        String startTime,
        String endTime,
        Integer totalSeats,
        BigDecimal pricePerSeat,
        LocalDateTime createdAt
) {}
