package com.example.BBA.model;

import jakarta.persistence.*;

@Entity
@Table(name = "booked_seat", uniqueConstraints = @UniqueConstraint(columnNames = {"bus_schedule_id", "seat_id"}))
public class BookedSeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;
    @Column(name = "bus_schedule_id", nullable = false)
    private Long busScheduleId;
    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    public BookedSeat() {}
    public BookedSeat(Long id, Long bookingId, Long busScheduleId, Long seatId) { this.id = id; this.bookingId = bookingId; this.busScheduleId = busScheduleId; this.seatId = seatId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getBusScheduleId() { return busScheduleId; }
    public void setBusScheduleId(Long busScheduleId) { this.busScheduleId = busScheduleId; }
    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public static BookedSeatBuilder builder() { return new BookedSeatBuilder(); }
    public static class BookedSeatBuilder {
        private Long bookingId, busScheduleId, seatId;
        public BookedSeatBuilder bookingId(Long v) { this.bookingId = v; return this; }
        public BookedSeatBuilder busScheduleId(Long v) { this.busScheduleId = v; return this; }
        public BookedSeatBuilder seatId(Long v) { this.seatId = v; return this; }
        public BookedSeat build() { return new BookedSeat(null, bookingId, busScheduleId, seatId); }
    }
}
