package de.saascom.probeaufgabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.saascom.probeaufgabe.models.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Buyer findByLogin(String login);
}
