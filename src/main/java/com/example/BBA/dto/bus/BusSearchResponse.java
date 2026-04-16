package com.example.BBA.dto.bus;

import java.time.LocalDate;
import java.time.LocalTime;

public record BusSearchResponse(
    Long busId,
    String busName,
    String source,
    String destination,
    LocalTime startTime,
    LocalTime endTime,
    int totalSeats,
    int availableSeats,
    Long scheduleId,
    LocalDate journeyDate
) {}
