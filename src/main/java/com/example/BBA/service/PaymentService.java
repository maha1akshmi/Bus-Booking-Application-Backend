package com.example.BBA.service;

import com.example.BBA.dto.payment.*;
import com.example.BBA.model.*;
import com.example.BBA.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, Long userId) {
        Payment payment = paymentRepository.findByBookingId(request.bookingId()).orElseThrow(() -> new RuntimeException("Payment not found for booking"));
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());
        paymentRepository.save(payment);
        String redirectUrl = "https://mock-payment-gateway.com/pay?txn=" + payment.getTransactionId();
        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), redirectUrl, payment.getCreatedAt());
    }

    public PaymentResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), null, payment.getCreatedAt());
    }

    @Transactional
    public PaymentResponse handleWebhook(WebhookRequest request, String userEmail) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId()).orElseThrow(() -> new RuntimeException("Payment not found for transaction"));
        Booking booking = bookingRepository.findById(payment.getBookingId()).orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("SUCCESS".equalsIgnoreCase(request.status())) {
            payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
            booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
            paymentRepository.save(payment);
            bookingRepository.save(booking);
            int seatCount = bookedSeatRepository.findByBookingId(booking.getId()).size();
            emailService.sendBookingConfirmation(userEmail, booking.getId(), payment.getAmount(), seatCount);
            emailService.sendPaymentSuccess(userEmail, booking.getId(), payment.getTransactionId(), payment.getAmount());
        } else {
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
            paymentRepository.save(payment);
            bookingRepository.save(booking);
            bookedSeatRepository.deleteByBookingId(booking.getId());
            emailService.sendPaymentFailed(userEmail, booking.getId(), payment.getTransactionId());
        }

        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), null, payment.getCreatedAt());
    }
}
