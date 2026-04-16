package com.example.BBA.dto.seat;

import jakarta.validation.constraints.NotBlank;

public record ReleaseRequest(
    @NotBlank(message = "Lock token is required") String lockToken
) {}
