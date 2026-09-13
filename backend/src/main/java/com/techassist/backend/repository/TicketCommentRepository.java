package com.techassist.backend.repository;

import com.techassist.backend.model.Ticket;
import com.techassist.backend.model.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    // All comments for a ticket, oldest first (conversation order)
    List<TicketComment> findByTicketOrderByCreatedAtAsc(Ticket ticket);

    // Count comments per ticket
    long countByTicket(Ticket ticket);
}