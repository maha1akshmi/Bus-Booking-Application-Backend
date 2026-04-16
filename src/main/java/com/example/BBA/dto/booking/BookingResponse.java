package com.example.BBA.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
    Long bookingId,
    Long scheduleId,
    String bookingStatus,
    BigDecimal totalAmount,
    List<Long> seatIds,
    List<PassengerRequest> passengers,
    Long paymentId,
    String paymentStatus,
    LocalDateTime createdAt
) {}
