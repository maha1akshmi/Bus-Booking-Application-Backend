package com.example.BBA.service;

import com.example.BBA.dto.seat.LockResponse;
import com.example.BBA.dto.seat.SeatLayoutResponse;
import com.example.BBA.model.Seat;
import com.example.BBA.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ConcurrentHashMap<String, LockEntry> lockStore = new ConcurrentHashMap<>();

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public record LockEntry(List<Long> seatIds, Long scheduleId, String lockedBy, LocalDateTime expiresAt) {}

    public List<SeatLayoutResponse> getSeatLayout(Long scheduleId, Long busId) {
        List<Seat> allSeats = seatRepository.findAllByBusIdOrdered(busId);
        Set<Long> bookedSeatIds = seatRepository.findBookedSeatIdsByScheduleId(scheduleId).stream().map(Number.class::cast).map(Number::longValue).collect(java.util.stream.Collectors.toSet());
        Set<Long> lockedSeatIds = getLockedSeatIdsForSchedule(scheduleId);
        return allSeats.stream().map(seat -> {
            String status = bookedSeatIds.contains(seat.getId()) ? "BOOKED" : lockedSeatIds.contains(seat.getId()) ? "LOCKED" : "AVAILABLE";
            return new SeatLayoutResponse(seat.getId(), seat.getSeatNumber(), status);
        }).toList();
    }

    public LockResponse lockSeats(Long scheduleId, List<Long> seatIds, String userEmail) {
        cleanExpiredLocks();
        for (LockEntry entry : lockStore.values()) {
            if (entry.scheduleId().equals(scheduleId) && entry.seatIds().stream().anyMatch(seatIds::contains)) {
                throw new RuntimeException("One or more seats are already locked by another user");
            }
        }
        Set<Long> bookedSeatIds = seatRepository.findBookedSeatIdsByScheduleId(scheduleId).stream().map(Number.class::cast).map(Number::longValue).collect(java.util.stream.Collectors.toSet());
        for (Long seatId : seatIds) {
            if (bookedSeatIds.contains(seatId)) throw new RuntimeException("Seat " + seatId + " is already booked");
        }
        String lockToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        lockStore.put(lockToken, new LockEntry(new ArrayList<>(seatIds), scheduleId, userEmail, expiresAt));
        return new LockResponse(lockToken, expiresAt);
    }

    public void releaseLock(String lockToken) {
        lockStore.remove(lockToken);
    }

    public boolean validateLockToken(String token, List<Long> seatIds) {
        cleanExpiredLocks();
        LockEntry entry = lockStore.get(token);
        if (entry == null) return false;
        return entry.seatIds().containsAll(seatIds) && seatIds.containsAll(entry.seatIds());
    }

    private Set<Long> getLockedSeatIdsForSchedule(Long scheduleId) {
        cleanExpiredLocks();
        Set<Long> locked = new HashSet<>();
        lockStore.values().stream().filter(e -> e.scheduleId().equals(scheduleId)).forEach(e -> locked.addAll(e.seatIds()));
        return locked;
    }

    private void cleanExpiredLocks() {
        lockStore.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(LocalDateTime.now()));
    }
}
