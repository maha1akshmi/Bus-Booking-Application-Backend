package com.example.BBA.dto.booking;

import jakarta.validation.constraints.*;

public record PassengerRequest(
    @NotBlank String name,
    @Min(1) @Max(120) int age,
    String gender
) {}
