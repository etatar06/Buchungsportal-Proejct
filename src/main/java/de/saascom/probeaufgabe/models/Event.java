package de.saascom.probeaufgabe.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")  // Definiert den Namen der Tabelle in der Datenbank.
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int maxNumberOfTickets;

    @Column(nullable = false)
    private int actualNumberOfTickets;

    @Column(nullable = false)
    private int price;

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getMaxNumberOfTickets() { return maxNumberOfTickets; }
    public void setMaxNumberOfTickets(int maxNumberOfTickets) { this.maxNumberOfTickets = maxNumberOfTickets; }

    public int getActualNumberOfTickets() { return actualNumberOfTickets; }
    public void setActualNumberOfTickets(int actualNumberOfTickets) { this.actualNumberOfTickets = actualNumberOfTickets; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
