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

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, Long userId) {
        Payment payment = paymentRepository.findByBookingId(request.bookingId()).orElseThrow(() -> new RuntimeException("Payment not found for booking"));
        Booking booking = bookingRepository.findById(request.bookingId()).orElseThrow(() -> new RuntimeException("Booking not found"));

        // Mock payment gateway — auto-succeed
        payment.setPaymentMethod(request.paymentMethod());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // Confirm the booking
        booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Send confirmation emails (async)
        try {
            int seatCount = bookedSeatRepository.findByBookingId(booking.getId()).size();
            String userEmail = getUserEmail(userId);
            emailService.sendBookingConfirmation(userEmail, booking.getId(), payment.getAmount(), seatCount);
            emailService.sendPaymentSuccess(userEmail, booking.getId(), payment.getTransactionId(), payment.getAmount());
        } catch (Exception e) {
            System.out.println("WARN: Email sending failed — " + e.getMessage());
        }

        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), null, payment.getCreatedAt());
    }

    private String getUserEmail(Long userId) {
        return userRepository.findById(userId).map(u -> u.getEmail()).orElse("noreply@busbooking.com");
    }

    public PaymentResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), null, payment.getCreatedAt());
    }

    @Transactional
    public PaymentResponse handleWebhook(WebhookRequest request, String userEmail) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId()).orElseThrow(() -> new RuntimeException("Payment not found for transaction"));
        Booking booking = bookingRepository.findById(payment.getBookingId()).orElseThrow(() -> new RuntimeException("Booking not found"));

        // Resolve email from payment's user if not provided
        if (userEmail == null || userEmail.isBlank() || "noreply@busbooking.com".equals(userEmail)) {
            userEmail = getUserEmail(payment.getUserId());
        }

        if ("SUCCESS".equalsIgnoreCase(request.status())) {
            payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
            booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
            paymentRepository.save(payment);
            bookingRepository.save(booking);
            try {
                int seatCount = bookedSeatRepository.findByBookingId(booking.getId()).size();
                emailService.sendBookingConfirmation(userEmail, booking.getId(), payment.getAmount(), seatCount);
                emailService.sendPaymentSuccess(userEmail, booking.getId(), payment.getTransactionId(), payment.getAmount());
            } catch (Exception e) { System.out.println("WARN: Email failed — " + e.getMessage()); }
        } else {
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
            paymentRepository.save(payment);
            bookingRepository.save(booking);
            bookedSeatRepository.deleteByBookingId(booking.getId());
            try { emailService.sendPaymentFailed(userEmail, booking.getId(), payment.getTransactionId()); }
            catch (Exception e) { System.out.println("WARN: Email failed — " + e.getMessage()); }
        }

        return new PaymentResponse(payment.getId(), payment.getBookingId(), payment.getTransactionId(), payment.getPaymentStatus().name(), payment.getPaymentMethod(), payment.getAmount(), null, payment.getCreatedAt());
    }
}
