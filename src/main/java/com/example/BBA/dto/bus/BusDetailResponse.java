package com.example.BBA.dto.bus;

import java.time.LocalTime;
import java.time.LocalDateTime;

public record BusDetailResponse(
    Long busId,
    String busName,
    String source,
    String destination,
    LocalTime startTime,
    LocalTime endTime,
    int totalSeats,
    LocalDateTime createdAt
) {}
