package com.example.BBA.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus")
public class Bus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bus_name", nullable = false, length = 100)
    private String busName;
    @Column(nullable = false, length = 100)
    private String source;
    @Column(nullable = false, length = 100)
    private String destination;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    @Column(name = "price_per_seat")
    private BigDecimal pricePerSeat;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Bus() {}
    public Bus(Long id, String busName, String source, String destination, LocalTime startTime, LocalTime endTime, Integer totalSeats, BigDecimal pricePerSeat, LocalDateTime createdAt) { this.id = id; this.busName = busName; this.source = source; this.destination = destination; this.startTime = startTime; this.endTime = endTime; this.totalSeats = totalSeats; this.pricePerSeat = pricePerSeat; this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(BigDecimal pricePerSeat) { this.pricePerSeat = pricePerSeat; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
