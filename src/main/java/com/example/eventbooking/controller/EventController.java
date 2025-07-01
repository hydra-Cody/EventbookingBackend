package com.example.eventbooking.controller;

import com.example.eventbooking.dto.EventCreateRequest;
import com.example.eventbooking.dto.EventResponse;
import com.example.eventbooking.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

        private final EventService eventService;

        @GetMapping
        public ResponseEntity<List<EventResponse>> getAllEvents() {
                return ResponseEntity.ok(eventService.getAllEvents());
        }

        @GetMapping("/{id}")
        public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
                return ResponseEntity.ok(eventService.getEventById(id));
        }

        @PostMapping
        public ResponseEntity<EventResponse> createEvent(
                        @Valid @RequestBody EventCreateRequest request,
                        @RequestParam Long organizerId) {
                return ResponseEntity.ok(eventService.createEvent(request, organizerId));
        }

        @PostMapping("/{id}/attend")
        public ResponseEntity<String> attendEvent(
                        @PathVariable Long id,
                        @RequestParam Long userId) {
                eventService.attendEvent(id, userId);
                return ResponseEntity.ok("Registered successfully");
        }

        @PostMapping("/{id}/cancel")
        public ResponseEntity<String> cancelAttendance(
                        @PathVariable Long id,
                        @RequestParam Long userId) {
                eventService.cancelAttendance(id, userId);
                return ResponseEntity.ok("Cancelled successfully");
        }
}
