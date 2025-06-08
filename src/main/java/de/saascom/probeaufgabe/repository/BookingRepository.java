package de.saascom.probeaufgabe.repository;

import de.saascom.probeaufgabe.models.Booking;
import de.saascom.probeaufgabe.models.Buyer;
import de.saascom.probeaufgabe.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByEventAndBuyer(Event event, Buyer buyer);
}
