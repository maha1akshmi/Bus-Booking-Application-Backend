package com.example.BBA.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DashboardResponse(
        long totalBuses,
        long totalBookings,
        BigDecimal totalRevenue,
        List<RecentBooking> recentBookings
) {
    public record RecentBooking(
            Long bookingId,
            String userEmail,
            String busName,
            String journeyDate,
            String status,
            BigDecimal amount,
            LocalDateTime createdAt
    ) {}
}
