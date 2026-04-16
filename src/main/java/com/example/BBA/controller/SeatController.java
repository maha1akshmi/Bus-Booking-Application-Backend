package com.example.BBA.controller;

import com.example.BBA.dto.seat.LockRequest;
import com.example.BBA.dto.seat.LockResponse;
import com.example.BBA.dto.seat.ReleaseRequest;
import com.example.BBA.dto.seat.SeatLayoutResponse;
import com.example.BBA.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Seat", description = "Seat layout, locking and release endpoints")
public class SeatController {

    private final SeatService seatService;

    @PersistenceContext
    private EntityManager entityManager;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/schedules/{scheduleId}/seats")
    @Operation(summary = "Get seat layout for a schedule")
    @ApiResponse(responseCode = "200", description = "Seat layout with availability status")
    public ResponseEntity<List<SeatLayoutResponse>> getSeatLayout(@PathVariable Long scheduleId) {
        Long busId = getBusIdFromSchedule(scheduleId);
        return ResponseEntity.ok(seatService.getSeatLayout(scheduleId, busId));
    }

    @PostMapping("/seats/lock")
    @Operation(summary = "Lock selected seats for booking")
    @ApiResponse(responseCode = "200", description = "Seats locked successfully with token")
    @ApiResponse(responseCode = "400", description = "Seats already locked or booked")
    public ResponseEntity<LockResponse> lockSeats(@Valid @RequestBody LockRequest request) {
        return ResponseEntity.ok(seatService.lockSeats(request.scheduleId(), request.seatIds(), "user"));
    }

    @PostMapping("/seats/release")
    @Operation(summary = "Release previously locked seats")
    @ApiResponse(responseCode = "200", description = "Seats released successfully")
    public ResponseEntity<String> releaseSeats(@Valid @RequestBody ReleaseRequest request) {
        seatService.releaseLock(request.lockToken());
        return ResponseEntity.ok("Seats released successfully");
    }

    private Long getBusIdFromSchedule(Long scheduleId) {
        Object result = entityManager.createNativeQuery("SELECT bus_id FROM bus_schedule WHERE id = :scheduleId").setParameter("scheduleId", scheduleId).getSingleResult();
        return ((Number) result).longValue();
    }
}
