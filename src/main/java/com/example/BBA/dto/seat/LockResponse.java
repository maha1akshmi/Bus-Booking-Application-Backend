package com.example.BBA.dto.seat;

import java.time.LocalDateTime;

public record LockResponse(
    String lockToken,
    LocalDateTime expiresAt
) {}
