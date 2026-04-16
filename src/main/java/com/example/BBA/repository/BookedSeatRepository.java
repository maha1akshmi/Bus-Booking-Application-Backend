package com.example.BBA.repository;

import com.example.BBA.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    List<BookedSeat> findByBookingId(Long bookingId);
    void deleteByBookingId(Long bookingId);

    @Query(value = "SELECT bs.seat_id FROM booked_seat bs WHERE bs.bus_schedule_id = :scheduleId AND bs.seat_id IN (:seatIds) FOR UPDATE", nativeQuery = true)
    List<Long> findLockedSeatsByScheduleAndSeatIds(@Param("scheduleId") Long scheduleId, @Param("seatIds") List<Long> seatIds);
}
