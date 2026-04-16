package com.example.BBA.dto.payment;

import jakarta.validation.constraints.*;

public record WebhookRequest(
    @NotBlank String transactionId,
    @NotBlank String status
) {}
