package com.library.seatmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    private int seatNumber;

    private int amount;

    private LocalDate bookingDate;
    private LocalDate expiryDate;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int amountPaid;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", seatNumber=" + seatNumber +
                ", amount=" + amount +
                ", bookingDate=" + bookingDate +
                ", expiryDate=" + expiryDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amountPaid=" + amountPaid +
                ", seat=" + seat +
                ", active=" + active +
                '}';
    }

    public Student() {
    }

    public Student(Long id, String name, String phone, int amount, int seatNumber, LocalDate bookingDate, LocalDate expiryDate, LocalDateTime startDate, LocalDateTime endDate, int amountPaid, Seat seat, boolean active) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.expiryDate = expiryDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountPaid = amountPaid;
        this.seat = seat;
        this.active = active;
    }
}

