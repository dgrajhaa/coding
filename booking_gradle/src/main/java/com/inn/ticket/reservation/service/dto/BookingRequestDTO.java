package com.inn.ticket.reservation.service.dto;


import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Data
public class BookingRequestDTO {

    @NotNull
    private String city;
    @NotNull
    private String theatre;
    @NotNull
    private String Screen;
    @NotNull
    private String movieName;
    @NotNull
    private Instant showDate;
    @NotNull
    private String show;
    @NotNull
    private String platformName;
    @NotEmpty
    private List<SeatDetails> seatDetails;

    private BigDecimal totalCharge;

    private BigDecimal total_tax;


}
