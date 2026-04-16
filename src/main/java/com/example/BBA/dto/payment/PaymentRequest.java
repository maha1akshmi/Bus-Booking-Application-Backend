package com.example.BBA.dto.payment;

import jakarta.validation.constraints.*;

public record PaymentRequest(
    @NotNull Long bookingId,
    @NotBlank String paymentMethod
) {}
