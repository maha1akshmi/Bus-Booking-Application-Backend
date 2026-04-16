package com.example.BBA.service;

import com.example.BBA.dto.bus.BusDetailResponse;
import com.example.BBA.dto.bus.BusSearchResponse;
import com.example.BBA.dto.bus.ScheduleDetailResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BusSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<BusSearchResponse> searchBuses(String source, String destination, LocalDate date) {
        String sql = """
            SELECT b.id, b.bus_name, b.source, b.destination, b.start_time, b.end_time,
                   b.total_seats, bs.id AS schedule_id, bs.journey_date,
                   (b.total_seats - (SELECT COUNT(*) FROM booked_seat bks WHERE bks.bus_schedule_id = bs.id)) AS available_seats
            FROM bus b
            INNER JOIN bus_schedule bs ON b.id = bs.bus_id
            WHERE LOWER(b.source) = LOWER(:source)
              AND LOWER(b.destination) = LOWER(:destination)
              AND bs.journey_date = :date
            ORDER BY b.start_time ASC
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("source", source);
        query.setParameter("destination", destination);
        query.setParameter("date", date);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        return rows.stream().map(r -> new BusSearchResponse(
            ((Number) r[0]).longValue(),
            (String) r[1],
            (String) r[2],
            (String) r[3],
            ((java.sql.Time) r[4]).toLocalTime(),
            ((java.sql.Time) r[5]).toLocalTime(),
            ((Number) r[6]).intValue(),
            ((Number) r[9]).intValue(),
            ((Number) r[7]).longValue(),
            ((java.sql.Date) r[8]).toLocalDate()
        )).toList();
    }

    public BusDetailResponse getBusById(Long busId) {
        String sql = "SELECT b.id, b.bus_name, b.source, b.destination, b.start_time, b.end_time, b.total_seats, b.created_at FROM bus b WHERE b.id = :busId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("busId", busId);
        Object[] r = (Object[]) query.getSingleResult();
        return new BusDetailResponse(
            ((Number) r[0]).longValue(),
            (String) r[1],
            (String) r[2],
            (String) r[3],
            ((java.sql.Time) r[4]).toLocalTime(),
            ((java.sql.Time) r[5]).toLocalTime(),
            ((Number) r[6]).intValue(),
            ((java.sql.Timestamp) r[7]).toLocalDateTime()
        );
    }

    public ScheduleDetailResponse getScheduleById(Long scheduleId) {
        String sql = """
            SELECT bs.id, bs.bus_id, b.bus_name, b.source, b.destination, b.start_time, b.end_time,
                   b.total_seats, bs.journey_date,
                   (b.total_seats - (SELECT COUNT(*) FROM booked_seat bks WHERE bks.bus_schedule_id = bs.id)) AS available_seats
            FROM bus_schedule bs
            INNER JOIN bus b ON b.id = bs.bus_id
            WHERE bs.id = :scheduleId
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("scheduleId", scheduleId);
        Object[] r = (Object[]) query.getSingleResult();
        return new ScheduleDetailResponse(
            ((Number) r[0]).longValue(),
            ((Number) r[1]).longValue(),
            (String) r[2],
            (String) r[3],
            (String) r[4],
            ((java.sql.Time) r[5]).toLocalTime(),
            ((java.sql.Time) r[6]).toLocalTime(),
            ((Number) r[7]).intValue(),
            ((Number) r[9]).intValue(),
            ((java.sql.Date) r[8]).toLocalDate()
        );
    }
}
