package com.example.BBA.controller;

import com.example.BBA.dto.bus.BusDetailResponse;
import com.example.BBA.dto.bus.BusSearchResponse;
import com.example.BBA.dto.bus.ScheduleDetailResponse;
import com.example.BBA.service.BusSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Bus & Search", description = "Bus search and schedule endpoints")
public class BusController {

    private final BusSearchService busSearchService;

    public BusController(BusSearchService busSearchService) {
        this.busSearchService = busSearchService;
    }

    @GetMapping("/buses/search")
    @Operation(summary = "Search buses by source, destination and date")
    @ApiResponse(responseCode = "200", description = "List of matching buses with availability")
    public ResponseEntity<List<BusSearchResponse>> searchBuses(@RequestParam String source, @RequestParam String destination, @RequestParam LocalDate date) {
        return ResponseEntity.ok(busSearchService.searchBuses(source, destination, date));
    }

    @GetMapping("/buses/{busId}")
    @Operation(summary = "Get bus details by ID")
    @ApiResponse(responseCode = "200", description = "Bus details returned")
    public ResponseEntity<BusDetailResponse> getBusById(@PathVariable Long busId) {
        return ResponseEntity.ok(busSearchService.getBusById(busId));
    }

    @GetMapping("/schedules/{scheduleId}")
    @Operation(summary = "Get schedule details by ID")
    @ApiResponse(responseCode = "200", description = "Schedule details with bus info")
    public ResponseEntity<ScheduleDetailResponse> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(busSearchService.getScheduleById(scheduleId));
    }
}
