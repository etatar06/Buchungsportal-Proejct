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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ConfirmationController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/confirmation")
    public String showConfirmation(@RequestParam(name = "eid") Long eventId, 
                                   @RequestParam(name = "bid") Long buyerId, 
                                   Model model) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Buyer> buyerOpt = buyerRepository.findById(buyerId);

        if (eventOpt.isEmpty() || buyerOpt.isEmpty()) {
            return "redirect:/events"; // Falls Käufer oder Event nicht gefunden wird, umleiten
        }

        Event event = eventOpt.get();
        Buyer buyer = buyerOpt.get();

        Optional<Booking> bookingOpt = bookingRepository.findByEventAndBuyer(event, buyer);
        if (bookingOpt.isEmpty()) {
            return "redirect:/events"; // Falls keine Buchung gefunden wird, umleiten
        }

        Booking booking = bookingOpt.get();
        int numberOfTickets = booking.getNumberOfTickets();
        double totalPrice = numberOfTickets * event.getPrice();

        model.addAttribute("buyer", buyer);
        model.addAttribute("event", event);
        model.addAttribute("numberOfTickets", numberOfTickets);
        model.addAttribute("totalPrice", totalPrice);

        return "confirmation";
    }
}
