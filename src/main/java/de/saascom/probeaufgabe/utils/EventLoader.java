package de.saascom.probeaufgabe.utils;

import de.saascom.probeaufgabe.models.Event;
import de.saascom.probeaufgabe.repository.BookingRepository;
import de.saascom.probeaufgabe.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventLoader implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public EventLoader(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Zuerst die Buchungstabelle leeren
        bookingRepository.deleteAll();
        System.out.println("🗑️ Alle Daten in der Buchungstabelle wurden gelöscht.");

        // 2️⃣ Dann die Event-Tabelle leeren
        eventRepository.deleteAll();
        System.out.println("🗑️ Alle Events in der Event-Tabelle wurden gelöscht.");

        // 3️⃣ Überprüfen, ob die Datei events.txt existiert
        if (getClass().getClassLoader().getResource("events.txt") == null) {
            System.out.println("⚠️ FEHLER: Die Datei events.txt wurde nicht gefunden! Stellen Sie sicher, dass sie sich in src/main/resources befindet.");
            return;
        }

        // 4️⃣ Datei events.txt lesen
        List<String> lines = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource("events.txt").toURI()));

        // 5️⃣ Daten mit Regex parsen
        Pattern pattern = Pattern.compile("\\[(.+?)]\\s*\\{(.+?)}\\s*\\((.+?)\\)\\s*#(\\d+)\\s*€(\\d+)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        int count = 0; // Zählt die geladenen Events

        for (String line : lines) {
            System.out.println("📄 Gelesene Zeile: " + line); // Fehlerdiagnose

            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                System.out.println("✅ Korrektes Format: " + line);

                Event event = new Event();
                event.setName(matcher.group(1));

                try {
                    // 🔹 Datum von String in LocalDateTime konvertieren
                    LocalDateTime date = LocalDateTime.parse(matcher.group(2), formatter);
                    event.setDate(date);
                } catch (DateTimeParseException e) {
                    System.out.println("❌ FEHLER: Ungültiges Datumsformat - " + matcher.group(2));
                    continue; // Überspringe fehlerhafte Daten
                }

                event.setCity(matcher.group(3));
                event.setMaxNumberOfTickets(Integer.parseInt(matcher.group(4)));
                event.setPrice(Integer.parseInt(matcher.group(5)));
                event.setActualNumberOfTickets(0);

                // 6️⃣ Event in die Datenbank speichern
                eventRepository.save(event);
                count++;

                System.out.println("✅ Erfolgreich gespeichert: " + event.getName());
            } else {
                System.out.println("⚠️ Warnung: Ungültiges Format - " + line);
            }
        }

        System.out.println("✅ Insgesamt " + count + " Events erfolgreich geladen!");
    }
}
