package de.saascom.probeaufgabe.controllers;

import de.saascom.probeaufgabe.models.Buyer;
import de.saascom.probeaufgabe.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private BuyerRepository buyerRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Zeigt die Login-Seite für den Benutzer an
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, Model model) {
        Buyer buyer = buyerRepository.findByLogin(username);
        
        if (buyer == null) {
            buyer = new Buyer();
            buyer.setLogin(username);
            buyerRepository.save(buyer);
        }

        // Benutzer-ID dem Model hinzufügen
        model.addAttribute("buyer", buyer);

        // Nach der Anmeldung wird der Benutzer zur Event-Seite weitergeleitet
        return "redirect:/events?bid=" + buyer.getId();
    }
}
