package com.example.BBA.repository;

import com.example.BBA.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBusId(Long busId);

    @Query("SELECT s FROM Seat s WHERE s.busId = :busId ORDER BY s.seatNumber ASC")
    List<Seat> findAllByBusIdOrdered(@Param("busId") Long busId);

    @Query(value = "SELECT s.id FROM seat s WHERE s.bus_id = :busId AND s.id NOT IN (SELECT bs.seat_id FROM booked_seat bs WHERE bs.bus_schedule_id = :scheduleId)", nativeQuery = true)
    List<Long> findAvailableSeatIds(@Param("busId") Long busId, @Param("scheduleId") Long scheduleId);

    @Query(value = "SELECT bs.seat_id FROM booked_seat bs WHERE bs.bus_schedule_id = :scheduleId", nativeQuery = true)
    List<Long> findBookedSeatIdsByScheduleId(@Param("scheduleId") Long scheduleId);
}
