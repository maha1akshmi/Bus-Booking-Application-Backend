package com.example.BBA.controller;

import com.example.BBA.dto.payment.*;
import com.example.BBA.model.User;
import com.example.BBA.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Booking & Payment", description = "Payment management endpoints")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Initiate payment for a booking", responses = @ApiResponse(responseCode = "200", description = "Payment initiated"))
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.initiatePayment(request, user.getId()));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details", responses = @ApiResponse(responseCode = "200", description = "Payment details"))
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @PostMapping("/webhook")
    @Operation(summary = "Payment gateway webhook callback", responses = @ApiResponse(responseCode = "200", description = "Webhook processed"))
    public ResponseEntity<PaymentResponse> handleWebhook(@Valid @RequestBody WebhookRequest request, @AuthenticationPrincipal User user) {
        String email = user != null ? user.getEmail() : "noreply@busbooking.com";
        return ResponseEntity.ok(paymentService.handleWebhook(request, email));
    }
}
