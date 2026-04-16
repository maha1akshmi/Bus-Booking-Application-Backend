package com.example.BBA.controller;

import com.example.BBA.dto.admin.*;
import com.example.BBA.service.AdminBusService;
import com.example.BBA.service.AdminScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin bus and schedule management")
public class AdminController {

    private final AdminBusService busService;
    private final AdminScheduleService scheduleService;

    @GetMapping("/buses")
    @Operation(summary = "Get all buses", responses = @ApiResponse(responseCode = "200", description = "List of all buses"))
    public ResponseEntity<?> getAllBuses() { return ResponseEntity.ok(busService.getAllBuses()); }

    @GetMapping("/buses/{busId}")
    @Operation(summary = "Get bus by ID", responses = @ApiResponse(responseCode = "200", description = "Bus details"))
    public ResponseEntity<?> getBusById(@PathVariable Long busId) { return ResponseEntity.ok(busService.getBusById(busId)); }

    @PostMapping("/buses")
    @Operation(summary = "Create a new bus", responses = @ApiResponse(responseCode = "200", description = "Bus created with seats auto-generated"))
    public ResponseEntity<?> createBus(@Valid @RequestBody BusRequest request) { return ResponseEntity.ok(busService.createBus(request)); }

    @PutMapping("/buses/{busId}")
    @Operation(summary = "Update a bus", responses = @ApiResponse(responseCode = "200", description = "Bus updated"))
    public ResponseEntity<?> updateBus(@PathVariable Long busId, @Valid @RequestBody BusRequest request) { return ResponseEntity.ok(busService.updateBus(busId, request)); }

    @DeleteMapping("/buses/{busId}")
    @Operation(summary = "Delete a bus", responses = @ApiResponse(responseCode = "200", description = "Bus deleted"))
    public ResponseEntity<?> deleteBus(@PathVariable Long busId) { busService.deleteBus(busId); return ResponseEntity.ok("Bus deleted successfully"); }

    @GetMapping("/schedules")
    @Operation(summary = "Get all schedules", responses = @ApiResponse(responseCode = "200", description = "List of all schedules"))
    public ResponseEntity<?> getAllSchedules() { return ResponseEntity.ok(scheduleService.getAllSchedules()); }

    @GetMapping("/schedules/bus/{busId}")
    @Operation(summary = "Get schedules by bus", responses = @ApiResponse(responseCode = "200", description = "Schedules for the given bus"))
    public ResponseEntity<?> getSchedulesByBus(@PathVariable Long busId) { return ResponseEntity.ok(scheduleService.getSchedulesByBus(busId)); }

    @PostMapping("/schedules")
    @Operation(summary = "Create a schedule", responses = @ApiResponse(responseCode = "200", description = "Schedule created"))
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequest request) { return ResponseEntity.ok(scheduleService.createSchedule(request)); }

    @PutMapping("/schedules/{id}")
    @Operation(summary = "Update a schedule", responses = @ApiResponse(responseCode = "200", description = "Schedule updated"))
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) { return ResponseEntity.ok(scheduleService.updateSchedule(id, request)); }

    @DeleteMapping("/schedules/{id}")
    @Operation(summary = "Delete a schedule", responses = @ApiResponse(responseCode = "200", description = "Schedule deleted"))
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) { scheduleService.deleteSchedule(id); return ResponseEntity.ok("Schedule deleted successfully"); }

    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard stats", responses = @ApiResponse(responseCode = "200", description = "Dashboard data"))
    public ResponseEntity<?> getDashboard() { return ResponseEntity.ok(scheduleService.getDashboard()); }

    @GetMapping("/dashboard")
    @Operation(summary="Get admin dashboard stats",responses=@ApiResponse(responseCode="200",description="Dashboard data"))
}
