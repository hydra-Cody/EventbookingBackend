package com.example.eventbooking.exception;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(String message) {
        super(message);
    }
}
