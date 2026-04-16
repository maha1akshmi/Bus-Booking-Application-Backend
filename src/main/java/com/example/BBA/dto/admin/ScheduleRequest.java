package com.example.BBA.dto.admin;

import jakarta.validation.constraints.*;

public record ScheduleRequest(
        @NotNull(message = "Bus ID is required") Long busId,
        @NotBlank(message = "Journey date is required") String journeyDate
) {}
