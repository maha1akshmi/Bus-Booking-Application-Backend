package com.example.BBA.repository;

import com.example.BBA.model.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {

    List<BusSchedule> findByBusId(Long busId);
}
