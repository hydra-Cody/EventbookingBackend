package com.example.eventbooking.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDate eventDate;
    private String imageUrl;
    private Integer capacity;
    private Integer availableSeats;
    private Double latitude;
    private Double longitude;
    private Long organizerId;
}
