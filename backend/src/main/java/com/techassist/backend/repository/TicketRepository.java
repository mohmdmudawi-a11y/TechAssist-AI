package com.techassist.backend.repository;

import com.techassist.backend.model.Ticket;
import com.techassist.backend.model.TicketStatus;
import com.techassist.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Employee's own tickets
    List<Ticket> findByEmployeeOrderByCreatedAtDesc(User employee);

    // Technician's assigned tickets
    List<Ticket> findByAssignedTechnicianOrderByCreatedAtDesc(User technician);

    // Filter by status
    List<Ticket> findByStatusOrderByCreatedAtDesc(TicketStatus status);

    // Count by status (for dashboards)
    long countByStatus(TicketStatus status);

    // Open tickets for a specific technician
    List<Ticket> findByAssignedTechnicianAndStatus(User technician, TicketStatus status);
}