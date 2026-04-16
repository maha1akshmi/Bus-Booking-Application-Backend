package com.example.BBA.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bus_id", nullable = false)
    private Long busId;
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    public Seat() {}
    public Seat(Long id, Long busId, String seatNumber) { this.id = id; this.busId = busId; this.seatNumber = seatNumber; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
}
