package com.example.eventbooking.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EventCreateRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String location;

    @NotNull
    private LocalDate eventDate;

    @NotBlank
    private String imageUrl;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
