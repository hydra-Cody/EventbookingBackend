package com.example.eventbooking.service;

import com.example.eventbooking.dto.EventCreateRequest;
import com.example.eventbooking.dto.EventResponse;
import com.example.eventbooking.entity.*;
import com.example.eventbooking.exception.ResourceNotFoundException;
import com.example.eventbooking.exception.SeatUnavailableException;
import com.example.eventbooking.repository.EventAttendeeRepository;
import com.example.eventbooking.repository.EventRepository;
import com.example.eventbooking.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

        private final EventRepository eventRepo;
        private final UserRepository userRepo;
        private final EventAttendeeRepository attendeeRepo;

        /* ---------- READ‑ONLY ---------- */

        public List<EventResponse> getAllEvents() {
                return eventRepo.findAll()
                                .stream()
                                .map(this::mapToDto)
                                .collect(Collectors.toList());
        }

        public EventResponse getEventById(Long id) {
                Event event = eventRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                return mapToDto(event);
        }

        /* ---------- CREATE ---------- */

        @Transactional
        public EventResponse createEvent(EventCreateRequest req, Long organizerId) {

                User organizer = userRepo.findById(organizerId)
                                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found"));

                Event event = Event.builder()
                                .title(req.getTitle())
                                .description(req.getDescription())
                                .location(req.getLocation())
                                .eventDate(req.getEventDate())
                                .imageUrl(req.getImageUrl())
                                .latitude(req.getLatitude())
                                .longitude(req.getLongitude())
                                .capacity(req.getCapacity())
                                .availableSeats(req.getCapacity())
                                .organizer(organizer)
                                .createdAt(OffsetDateTime.now())
                                .build();

                return mapToDto(eventRepo.save(event));
        }

        /* ---------- ATTEND / CANCEL ---------- */

        @Transactional
        public void attendEvent(Long eventId, Long userId) {

                Event event = eventRepo.findById(eventId)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                if (event.getAvailableSeats() <= 0) {
                        throw new SeatUnavailableException("No seats available");
                }

                if (attendeeRepo.existsByUserAndEvent(user, event)) {
                        throw new EntityExistsException("Already registered");
                }

                EventAttendee attendee = EventAttendee.builder()
                                .user(user)
                                .event(event)
                                .status(AttendanceStatus.REGISTERED)
                                .registeredAt(OffsetDateTime.now())
                                .build();

                attendeeRepo.save(attendee);
                event.setAvailableSeats(event.getAvailableSeats() - 1);
                eventRepo.save(event);
        }

        @Transactional
        public void cancelAttendance(Long eventId, Long userId) {

                Event event = eventRepo.findById(eventId)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                EventAttendee attendee = attendeeRepo.findByUserAndEvent(user, event)
                                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

                if (attendee.getStatus() == AttendanceStatus.CANCELLED) {
                        return; // already cancelled; silently ignore or throw if preferred
                }

                attendee.setStatus(AttendanceStatus.CANCELLED);
                attendeeRepo.save(attendee);

                event.setAvailableSeats(event.getAvailableSeats() + 1);
                eventRepo.save(event);
        }

        /* ---------- MAPPER ---------- */

        private EventResponse mapToDto(Event e) {
                return EventResponse.builder()
                                .id(e.getId())
                                .title(e.getTitle())
                                .description(e.getDescription())
                                .location(e.getLocation())
                                .eventDate(e.getEventDate())
                                .imageUrl(e.getImageUrl())
                                .capacity(e.getCapacity())
                                .availableSeats(e.getAvailableSeats())
                                .latitude(e.getLatitude())
                                .longitude(e.getLongitude())
                                .organizerId(e.getOrganizer().getId())
                                .build();
        }
}
