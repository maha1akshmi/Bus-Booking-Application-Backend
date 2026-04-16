package com.example.BBA.service;

import com.example.BBA.dto.admin.DashboardResponse;
import com.example.BBA.dto.admin.ScheduleRequest;
import com.example.BBA.dto.admin.ScheduleResponse;
import com.example.BBA.model.Bus;
import com.example.BBA.model.BusSchedule;
import com.example.BBA.repository.BusRepository;
import com.example.BBA.repository.BusScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminScheduleService {
    private final BusScheduleRepository scheduleRepository;
    private final BusRepository busRepository;
    @PersistenceContext
    private EntityManager em;

    public AdminScheduleService(BusScheduleRepository scheduleRepository, BusRepository busRepository) { this.scheduleRepository = scheduleRepository; this.busRepository = busRepository; }

    public List<ScheduleResponse> getAllSchedules() { return scheduleRepository.findAll().stream().map(this::toResponse).toList(); }

    public List<ScheduleResponse> getSchedulesByBus(Long busId) { return scheduleRepository.findByBusId(busId).stream().map(this::toResponse).toList(); }

    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest req) {
        Bus bus = busRepository.findById(req.busId()).orElseThrow(() -> new RuntimeException("Bus not found with id: " + req.busId()));
        BusSchedule schedule = new BusSchedule(); schedule.setBus(bus); schedule.setJourneyDate(LocalDate.parse(req.journeyDate()));
        return toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long id, ScheduleRequest req) {
        BusSchedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
        schedule.setJourneyDate(LocalDate.parse(req.journeyDate()));
        if (!schedule.getBus().getId().equals(req.busId())) schedule.setBus(busRepository.findById(req.busId()).orElseThrow(() -> new RuntimeException("Bus not found with id: " + req.busId())));
        return toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public void deleteSchedule(Long id) { if (!scheduleRepository.existsById(id)) throw new RuntimeException("Schedule not found with id: " + id); scheduleRepository.deleteById(id); }

    @SuppressWarnings("unchecked")
    public DashboardResponse getDashboard() {
        long totalBuses = busRepository.count();
        long totalBookings = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM booking").getSingleResult()).longValue();
        BigDecimal totalRevenue = toBigDecimal(em.createNativeQuery("SELECT COALESCE(SUM(total_amount), 0) FROM booking WHERE booking_status = 'CONFIRMED'").getSingleResult());
        List<Object[]> rows = em.createNativeQuery("SELECT b.id, u.email, bus.bus_name, bs.journey_date, b.booking_status, b.total_amount, b.created_at FROM booking b JOIN user u ON b.user_id = u.id JOIN bus_schedule bs ON b.bus_schedule_id = bs.id JOIN bus ON bs.bus_id = bus.id ORDER BY b.created_at DESC LIMIT 10").getResultList();
        List<DashboardResponse.RecentBooking> recent = rows.stream().map(r -> new DashboardResponse.RecentBooking(((Number) r[0]).longValue(), (String) r[1], (String) r[2], r[3].toString(), (String) r[4], toBigDecimal(r[5]), r[6] instanceof LocalDateTime ldt ? ldt : LocalDateTime.parse(r[6].toString().replace(" ", "T")))).toList();
        return new DashboardResponse(totalBuses, totalBookings, totalRevenue, recent);
    }

    private BigDecimal toBigDecimal(Object val) { return val instanceof BigDecimal bd ? bd : new BigDecimal(val.toString()); }

    private ScheduleResponse toResponse(BusSchedule s) { return new ScheduleResponse(s.getId(), s.getBus().getId(), s.getBus().getBusName(), s.getJourneyDate(), s.getCreatedAt()); }
}
