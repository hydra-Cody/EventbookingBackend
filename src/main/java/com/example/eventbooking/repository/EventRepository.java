package com.example.eventbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventbooking.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}