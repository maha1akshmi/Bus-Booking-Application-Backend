package com.example.BBA.dto.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record BookingRequest(
    @NotNull Long scheduleId,
    @NotEmpty List<Long> seatIds,
    @NotEmpty @Valid List<PassengerRequest> passengers,
    @NotBlank String lockToken,
    @NotNull @DecimalMin("0.01") BigDecimal pricePerSeat
) {}
