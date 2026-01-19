package com.library.seatmanager.controller;

import com.library.seatmanager.entity.Seat;
import com.library.seatmanager.entity.Student;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vacate")
@CrossOrigin
public class VacateController {

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private StudentRepository studentRepo;

    @PostMapping("/{seatNumber}")
    public ResponseEntity<String> vacateSeat(@PathVariable int seatNumber) {

        Seat seat = seatRepo.findBySeatNumber(seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat not found"));


        Optional<Student> studentOpt =
                studentRepo.findBySeat_SeatNumberAndActiveTrue(seatNumber);

        if (studentOpt.isEmpty()) {
            seat.setOccupied(false);
            seatRepo.save(seat);
            return ResponseEntity.ok("Seat already vacant");
        }

        Student student = studentRepo
                .findBySeat_SeatNumberAndActiveTrue(seatNumber)
                .orElseThrow(() -> new RuntimeException("Active student not found"));

        // deactivate student
        student.setActive(false);
        studentRepo.save(student);

        // free seat
        seat.setOccupied(false);
        seatRepo.save(seat);

        return ResponseEntity.ok("Seat vacated successfully");
    }
}
