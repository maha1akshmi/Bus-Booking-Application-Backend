package com.example.BBA.dto.admin;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BusRequest(
        @NotBlank(message = "Bus name is required") String busName,
        @NotBlank(message = "Source is required") String source,
        @NotBlank(message = "Destination is required") String destination,
        @NotBlank(message = "Start time is required") String startTime,
        @NotBlank(message = "End time is required") String endTime,
        @NotNull(message = "Total seats is required") @Min(1) Integer totalSeats,
        @NotNull(message = "Price per seat is required") @DecimalMin("0.01") BigDecimal pricePerSeat
) {}
