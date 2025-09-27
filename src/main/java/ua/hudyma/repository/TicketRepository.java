package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
