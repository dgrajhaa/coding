package com.inn.ticket.reservation.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AvailabilityDTO {
    private Long mov_id;
    private String mov_name;
    private Long thtr_id;
    private String thtr_name;
    private Long scn_id;
    private Long scn_name;
    private Long shw_id;
    private Instant shw_date;
    private String rw_nam;
    private Integer seat_no;
    private String locked;
    private String sts;
}
