package com.example.BBA.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_schedule")
public class BusSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;
    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public BusSchedule() {}
    public BusSchedule(Long id, Bus bus, LocalDate journeyDate, LocalDateTime createdAt) { this.id = id; this.bus = bus; this.journeyDate = journeyDate; this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
