package com.example.BBA.controller;

import com.example.BBA.dto.booking.*;
import com.example.BBA.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking & Payment", description = "Booking management endpoints")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking", responses = @ApiResponse(responseCode = "200", description = "Booking created"))
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername().split(",")[0]);
        return ResponseEntity.ok(bookingService.createBooking(request, userId));
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking details", responses = @ApiResponse(responseCode = "200", description = "Booking details"))
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel a booking", responses = @ApiResponse(responseCode = "200", description = "Booking cancelled"))
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, userDetails.getUsername()));
    }

    @GetMapping("/history")
    @Operation(summary = "Get booking history for current user", responses = @ApiResponse(responseCode = "200", description = "Booking history"))
    public ResponseEntity<List<BookingResponse>> getBookingHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername().split(",")[0]);
        return ResponseEntity.ok(bookingService.getBookingHistory(userId));
    }
}
