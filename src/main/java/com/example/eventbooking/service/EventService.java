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

        private final EventRepository eventRepository;
        private final UserRepository userRepository;
        private final EventAttendeeRepository attendeeRepository;

        public List<EventResponse> getAllEvents() {
                return eventRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        public EventResponse getEventById(Long id) {
                Event event = eventRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                return mapToResponse(event);
        }

        @Transactional
        public EventResponse createEvent(EventCreateRequest req, Long organizerId) {

                User organizer = userRepository.findById(organizerId)
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

                Event saved = eventRepository.save(event);
                return mapToResponse(saved);
        }

        @Transactional
        public void attendEvent(Long eventId, Long userId) {

                Event event = eventRepository.findById(eventId)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                if (event.getAvailableSeats() <= 0) {
                        throw new SeatUnavailableException("No seats available");
                }

                boolean exists = attendeeRepository.existsByUserAndEvent(user, event);
                if (exists)
                        throw new EntityExistsException("User already registered");

                EventAttendee attendee = EventAttendee.builder()
                                .user(user)
                                .event(event)
                                .status(AttendanceStatus.REGISTERED)
                                .registeredAt(OffsetDateTime.now())
                                .build();

                attendeeRepository.save(attendee);

                event.setAvailableSeats(event.getAvailableSeats() - 1);
                eventRepository.save(event);
        }

        @Transactional
        public void cancelAttendance(Long eventId, Long userId) {

                Event event = eventRepository.findById(eventId)
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                EventAttendee attendee = attendeeRepository.findByUserAndEvent(user, event)
                                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

                if (attendee.getStatus() == AttendanceStatus.CANCELLED) {
                        throw new IllegalStateException("Already cancelled");
                }

                attendee.setStatus(AttendanceStatus.CANCELLED);
                attendeeRepository.save(attendee);

                event.setAvailableSeats(event.getAvailableSeats() + 1);
                eventRepository.save(event);
        }

        private EventResponse mapToResponse(Event e) {
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
