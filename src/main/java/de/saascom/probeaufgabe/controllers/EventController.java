package de.saascom.probeaufgabe.controllers;

import de.saascom.probeaufgabe.models.Event;
import de.saascom.probeaufgabe.models.Buyer;
import de.saascom.probeaufgabe.repository.EventRepository;
import de.saascom.probeaufgabe.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @GetMapping
    public String listEvents(@RequestParam(name = "bid", required = false) Long buyerId, Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);

        if (buyerId != null) {
            Optional<Buyer> buyerOpt = buyerRepository.findById(buyerId);
            if (buyerOpt.isPresent()) {
                model.addAttribute("buyer", buyerOpt.get());
                System.out.println("✅ Käufer gefunden: " + buyerOpt.get().getLogin() + " (ID: " + buyerOpt.get().getId() + ")");
            } else {
                System.out.println("❌ FEHLER: Käufer-ID nicht gefunden: " + buyerId);
                return "redirect:/login"; // Falls die Käufer-ID falsch ist, zur Login-Seite weiterleiten
            }
        } else {
            System.out.println("⚠️ WARNUNG: 'bid'-Parameter ist NULL.");
            return "redirect:/login"; // Falls 'bid' fehlt, zur Login-Seite weiterleiten
        }

        return "events";
    }
}
