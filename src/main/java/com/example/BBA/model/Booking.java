package com.example.BBA.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    public enum BookingStatus { PENDING, CONFIRMED, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "bus_schedule_id", nullable = false)
    private Long busScheduleId;
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus = BookingStatus.PENDING;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Booking() {}
    public Booking(Long id, Long userId, Long busScheduleId, BigDecimal totalAmount, BookingStatus bookingStatus, LocalDateTime createdAt) { this.id = id; this.userId = userId; this.busScheduleId = busScheduleId; this.totalAmount = totalAmount; this.bookingStatus = bookingStatus; this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBusScheduleId() { return busScheduleId; }
    public void setBusScheduleId(Long busScheduleId) { this.busScheduleId = busScheduleId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static BookingBuilder builder() { return new BookingBuilder(); }
    public static class BookingBuilder {
        private Long userId, busScheduleId;
        private BigDecimal totalAmount;
        private BookingStatus bookingStatus = BookingStatus.PENDING;
        public BookingBuilder userId(Long v) { this.userId = v; return this; }
        public BookingBuilder busScheduleId(Long v) { this.busScheduleId = v; return this; }
        public BookingBuilder totalAmount(BigDecimal v) { this.totalAmount = v; return this; }
        public BookingBuilder bookingStatus(BookingStatus v) { this.bookingStatus = v; return this; }
        public Booking build() { return new Booking(null, userId, busScheduleId, totalAmount, bookingStatus, null); }
    }
}
