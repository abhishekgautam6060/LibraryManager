package com.library.seatmanager.repository;


import com.library.seatmanager.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    long countByOccupiedTrue();
    Optional<Seat> findBySeatNumber(int seatNumber);
}
