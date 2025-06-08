package de.saascom.probeaufgabe.controllers;

import de.saascom.probeaufgabe.models.Booking;
import de.saascom.probeaufgabe.models.Buyer;
import de.saascom.probeaufgabe.models.Event;
import de.saascom.probeaufgabe.repository.BookingRepository;
import de.saascom.probeaufgabe.repository.BuyerRepository;
import de.saascom.probeaufgabe.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public String showBookingPage(@RequestParam(name = "eid") Long eventId,
                                  @RequestParam(name = "bid") Long buyerId,
                                  Model model) {
        if (eventId == null || buyerId == null) {
            System.out.println("FEHLER: 'bid' oder 'eid' Parameter fehlt.");
            return "redirect:/events";
        }

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Buyer> buyerOpt = buyerRepository.findById(buyerId);

        if (eventOpt.isEmpty() || buyerOpt.isEmpty()) {
            System.out.println("FEHLER: Käufer oder Event nicht gefunden!");
            return "redirect:/events";
        }

        Event event = eventOpt.get();
        Buyer buyer = buyerOpt.get();

        Optional<Booking> existingBookingOpt = bookingRepository.findByEventAndBuyer(event, buyer);
        int existingTickets = existingBookingOpt.map(Booking::getNumberOfTickets).orElse(0);
        double totalPrice = existingTickets * event.getPrice();

        model.addAttribute("event", event);
        model.addAttribute("buyer", buyer);
        model.addAttribute("numberOfTickets", existingTickets);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("error", ""); // Platzhalter für eine Fehlermeldung hinzugefügt

        return "booking";
    }

    @PostMapping
    public String bookTickets(@RequestParam Long eid,
                              @RequestParam Long bid,
                              @RequestParam int numberOfTickets,
                              Model model) {
        Optional<Event> eventOpt = eventRepository.findById(eid);
        Optional<Buyer> buyerOpt = buyerRepository.findById(bid);

        if (eventOpt.isPresent() && buyerOpt.isPresent()) {
            Event event = eventOpt.get();
            Buyer buyer = buyerOpt.get();

            int availableTickets = event.getMaxNumberOfTickets() - event.getActualNumberOfTickets();
            if (numberOfTickets > availableTickets) {
                // 🛑 Wenn keine Tickets verfügbar sind, Fehlermeldung hinzufügen und gleiche Seite anzeigen
                model.addAttribute("error", "⚠️ FEHLER: Nicht genügend Tickets verfügbar! Verfügbare Anzahl: " + availableTickets);

                // Seite neu laden mit Model-Variablen
                model.addAttribute("event", event);
                model.addAttribute("buyer", buyer);
                model.addAttribute("numberOfTickets", 0); // Benutzer muss neue Anzahl auswählen
                model.addAttribute("totalPrice", 0);
                return "booking"; // **Seite erneut anzeigen**
            }

            Optional<Booking> existingBookingOpt = bookingRepository.findByEventAndBuyer(event, buyer);

            if (existingBookingOpt.isPresent()) {
                Booking booking = existingBookingOpt.get();
                booking.setNumberOfTickets(booking.getNumberOfTickets() + numberOfTickets);
                bookingRepository.save(booking);
            } else {
                Booking newBooking = new Booking();
                newBooking.setBuyer(buyer);
                newBooking.setEvent(event);
                newBooking.setNumberOfTickets(numberOfTickets);
                bookingRepository.save(newBooking);
            }

            event.setActualNumberOfTickets(event.getActualNumberOfTickets() + numberOfTickets);
            eventRepository.save(event);

            return "redirect:/confirmation?eid=" + eid + "&bid=" + bid;
        } else {
            return "redirect:/events";
        }
    }
}
