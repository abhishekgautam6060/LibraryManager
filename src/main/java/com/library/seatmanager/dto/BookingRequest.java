package com.library.seatmanager.dto;

public class BookingRequest {
        private int seatNumber;
        private String name;
        private String phone;
        private int amountPaid;

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "seatNumber=" + seatNumber +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", amountPaid=" + amountPaid +
                '}';
    }

    public BookingRequest(int seatNumber, int amountPaid, String phone, String name) {
        this.seatNumber = seatNumber;
        this.amountPaid = amountPaid;
        this.phone = phone;
        this.name = name;
    }

    public BookingRequest() {
    }
}
