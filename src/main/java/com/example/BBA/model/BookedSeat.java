package com.example.BBA.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booked_seat", uniqueConstraints = @UniqueConstraint(columnNames = {"bus_schedule_id", "seat_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "bus_schedule_id", nullable = false)
    private Long busScheduleId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;
}
