package com.example.BBA.dto.seat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LockRequest(
    @NotNull(message = "Schedule ID is required") Long scheduleId,
    @NotEmpty(message = "Seat IDs cannot be empty") List<Long> seatIds
) {}
