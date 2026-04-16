package com.example.BBA.service;

import com.example.BBA.dto.admin.BusRequest;
import com.example.BBA.dto.admin.BusResponse;
import com.example.BBA.model.Bus;
import com.example.BBA.repository.BusRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBusService {

    private final BusRepository busRepository;

    @PersistenceContext
    private EntityManager em;

    public List<BusResponse> getAllBuses() {
        return busRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BusResponse getBusById(Long id) {
        return toResponse(busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found with id: " + id)));
    }

    @Transactional
    public BusResponse createBus(BusRequest req) {
        Bus bus = new Bus();
        bus.setBusName(req.busName());
        bus.setSource(req.source());
        bus.setDestination(req.destination());
        bus.setStartTime(LocalTime.parse(req.startTime()));
        bus.setEndTime(LocalTime.parse(req.endTime()));
        bus.setTotalSeats(req.totalSeats());
        bus.setPricePerSeat(req.pricePerSeat());
        Bus saved = busRepository.save(bus);
        generateSeats(saved.getId(), saved.getTotalSeats());
        return toResponse(saved);
    }

    @Transactional
    public BusResponse updateBus(Long id, BusRequest req) {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));
        bus.setBusName(req.busName());
        bus.setSource(req.source());
        bus.setDestination(req.destination());
        bus.setStartTime(LocalTime.parse(req.startTime()));
        bus.setEndTime(LocalTime.parse(req.endTime()));
        bus.setTotalSeats(req.totalSeats());
        bus.setPricePerSeat(req.pricePerSeat());
        return toResponse(busRepository.save(bus));
    }

    @Transactional
    public void deleteBus(Long id) {
        if (!busRepository.existsById(id)) throw new RuntimeException("Bus not found with id: " + id);
        busRepository.deleteById(id);
    }

    private void generateSeats(Long busId, int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) em.createNativeQuery("INSERT INTO seat (bus_id, seat_number) VALUES (?, ?)").setParameter(1, busId).setParameter(2, String.valueOf(i)).executeUpdate();
    }

    private BusResponse toResponse(Bus bus) {
        return new BusResponse(bus.getId(), bus.getBusName(), bus.getSource(), bus.getDestination(), bus.getStartTime().toString(), bus.getEndTime().toString(), bus.getTotalSeats(), bus.getPricePerSeat(), bus.getCreatedAt());
    }
}
