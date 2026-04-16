package com.example.BBA.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        Long busId,
        String busName,
        LocalDate journeyDate,
        LocalDateTime createdAt
) {}
