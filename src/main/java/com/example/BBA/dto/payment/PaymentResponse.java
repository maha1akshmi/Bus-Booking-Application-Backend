package com.example.BBA.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long paymentId,
    Long bookingId,
    String transactionId,
    String paymentStatus,
    String paymentMethod,
    BigDecimal amount,
    String redirectUrl,
    LocalDateTime createdAt
) {}
