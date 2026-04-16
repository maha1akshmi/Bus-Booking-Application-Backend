package com.example.BBA.service;

import com.example.BBA.dto.booking.*;
import com.example.BBA.model.*;
import com.example.BBA.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private SeatService seatService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        if (!seatService.validateLockToken(request.lockToken(), request.seatIds()))
            throw new RuntimeException("Invalid or expired lock token");

        List<Long> alreadyBooked = bookedSeatRepository.findLockedSeatsByScheduleAndSeatIds(request.scheduleId(), request.seatIds());
        if (!alreadyBooked.isEmpty())
            throw new RuntimeException("Seats already booked: " + alreadyBooked);

        BigDecimal totalAmount = request.pricePerSeat().multiply(BigDecimal.valueOf(request.seatIds().size()));

        Booking booking = bookingRepository.save(Booking.builder()
                .userId(userId)
                .busScheduleId(request.scheduleId())
                .totalAmount(totalAmount)
                .bookingStatus(Booking.BookingStatus.PENDING)
                .build());

        request.seatIds().forEach(seatId -> bookedSeatRepository.save(BookedSeat.builder()
                .bookingId(booking.getId())
                .busScheduleId(request.scheduleId())
                .seatId(seatId)
                .build()));

        request.passengers().forEach(p -> passengerRepository.save(Passenger.builder()
                .bookingId(booking.getId())
                .name(p.name())
                .age(p.age())
                .gender(p.gender())
                .build()));

        Payment payment = paymentRepository.save(Payment.builder()
                .bookingId(booking.getId())
                .userId(userId)
                .transactionId(UUID.randomUUID().toString())
                .paymentStatus(Payment.PaymentStatus.PENDING)
                .amount(totalAmount)
                .build());

        seatService.releaseLock(request.lockToken());

        List<PassengerRequest> passengerList = request.passengers().stream().toList();
        return new BookingResponse(booking.getId(), booking.getBusScheduleId(), booking.getBookingStatus().name(), totalAmount, request.seatIds(), passengerList, payment.getId(), payment.getPaymentStatus().name(), booking.getCreatedAt());
    }

    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        List<Long> seatIds = bookedSeatRepository.findByBookingId(bookingId).stream().map(BookedSeat::getSeatId).toList();
        List<PassengerRequest> passengers = passengerRepository.findByBookingId(bookingId).stream().map(p -> new PassengerRequest(p.getName(), p.getAge(), p.getGender())).toList();
        Payment payment = paymentRepository.findByBookingId(bookingId).orElse(null);
        return new BookingResponse(booking.getId(), booking.getBusScheduleId(), booking.getBookingStatus().name(), booking.getTotalAmount(), seatIds, passengers, payment != null ? payment.getId() : null, payment != null ? payment.getPaymentStatus().name() : null, booking.getCreatedAt());
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        bookedSeatRepository.deleteByBookingId(bookingId);

        paymentRepository.findByBookingId(bookingId).ifPresent(payment -> {
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
            paymentRepository.save(payment);
        });

        emailService.sendBookingCancellation(userEmail, bookingId);

        return getBooking(bookingId);
    }

    public List<BookingResponse> getBookingHistory(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(b -> getBooking(b.getId())).toList();
    }
}
