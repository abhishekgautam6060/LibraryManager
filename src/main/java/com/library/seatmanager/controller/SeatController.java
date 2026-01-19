package com.library.seatmanager.controller;

import com.library.seatmanager.dto.SeatStatusDTO;
import com.library.seatmanager.entity.Seat;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin
public class SeatController {

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private StudentRepository  studentRepo;


    @GetMapping
    public List<SeatStatusDTO> getAllSeats() {
        List<Seat> seats = seatRepo.findAll(Sort.by("seatNumber"));
        List<SeatStatusDTO> result = new ArrayList<>();

        for (Seat seat : seats) {
            boolean occupied = studentRepo
                    .findBySeat_SeatNumberAndActiveTrue(seat.getSeatNumber())
                    .isPresent();

            result.add(new SeatStatusDTO(seat.getSeatNumber(), occupied));
        }
        return result;
    }
}

