package com.example.eventbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventbooking.entity.Event;
import com.example.eventbooking.entity.EventAttendee;
import com.example.eventbooking.entity.User;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    boolean existsByUserAndEvent(User user, Event event);

    Optional<EventAttendee> findByUserAndEvent(User user, Event event);
}