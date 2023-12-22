package com.inn.ticket.reservation.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeatDetails {

    private String seatRow;
    private List<Integer> seatNumbers;

}
