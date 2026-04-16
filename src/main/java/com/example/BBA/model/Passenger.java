package com.example.BBA.model;

import jakarta.persistence.*;

@Entity
@Table(name = "passenger")
public class Passenger {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "gender")
    private String gender;

    public Passenger() {}
    public Passenger(Long id, Long bookingId, String name, Integer age, String gender) { this.id = id; this.bookingId = bookingId; this.name = name; this.age = age; this.gender = gender; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public static PassengerBuilder builder() { return new PassengerBuilder(); }
    public static class PassengerBuilder {
        private Long bookingId;
        private String name, gender;
        private Integer age;
        public PassengerBuilder bookingId(Long v) { this.bookingId = v; return this; }
        public PassengerBuilder name(String v) { this.name = v; return this; }
        public PassengerBuilder age(Integer v) { this.age = v; return this; }
        public PassengerBuilder gender(String v) { this.gender = v; return this; }
        public Passenger build() { return new Passenger(null, bookingId, name, age, gender); }
    }
}
