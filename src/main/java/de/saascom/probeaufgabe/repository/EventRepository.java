package de.saascom.probeaufgabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.saascom.probeaufgabe.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> { }
