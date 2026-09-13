package com.techassist.backend.service;

import com.techassist.backend.dto.DashboardResponse;
import com.techassist.backend.model.*;
import com.techassist.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DashboardService — builds dashboard statistics.
 *
 * Uses existing repository methods (countByStatus, findAll, etc.)
 * and simple Java code to assemble the numbers.
 */
@Service
public class DashboardService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public DashboardService(TicketRepository ticketRepository,
                            UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    /**
     * Employee dashboard — stats about MY tickets only.
     */
    @Transactional(readOnly = true)
    public DashboardResponse getEmployeeDashboard(String email) {
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Ticket> tickets = ticketRepository.findByEmployeeOrderByCreatedAtDesc(employee);
        return buildDashboard(tickets);
    }

    /**
     * Technician dashboard — stats about MY assigned tickets.
     */
    @Transactional(readOnly = true)
    public DashboardResponse getTechnicianDashboard(String email) {
        User technician = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        List<Ticket> tickets = ticketRepository.findByAssignedTechnicianOrderByCreatedAtDesc(technician);
        return buildDashboard(tickets);
    }

    /**
     * Admin dashboard — stats about EVERY ticket in the system.
     */
    @Transactional(readOnly = true)
    public DashboardResponse getAdminDashboard() {
        List<Ticket> tickets = ticketRepository.findAll();
        return buildDashboard(tickets);
    }

    /**
     * Common logic: take a list of tickets and compute all counters.
     */
    private DashboardResponse buildDashboard(List<Ticket> tickets) {
        long total = tickets.size();

        long open = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.NEW
                          || t.getStatus() == TicketStatus.ASSIGNED
                          || t.getStatus() == TicketStatus.IN_PROGRESS)
                .count();

        long resolved = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.RESOLVED
                          || t.getStatus() == TicketStatus.CLOSED)
                .count();

        long critical = tickets.stream()
                .filter(t -> t.getPriority() == TicketPriority.CRITICAL)
                .count();

        // Tickets by status
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (TicketStatus s : TicketStatus.values()) {
            long c = tickets.stream().filter(t -> t.getStatus() == s).count();
            if (c > 0) byStatus.put(s.name(), c);
        }

        // Tickets by priority
        Map<String, Long> byPriority = new LinkedHashMap<>();
        for (TicketPriority p : TicketPriority.values()) {
            long c = tickets.stream().filter(t -> t.getPriority() == p).count();
            if (c > 0) byPriority.put(p.name(), c);
        }

        // Tickets by category
        Map<String, Long> byCategory = new LinkedHashMap<>();
        tickets.forEach(t -> {
            String name = t.getCategory().getName();
            byCategory.put(name, byCategory.getOrDefault(name, 0L) + 1);
        });

        return DashboardResponse.builder()
                .totalTickets(total)
                .openTickets(open)
                .resolvedTickets(resolved)
                .criticalTickets(critical)
                .ticketsByStatus(byStatus)
                .ticketsByPriority(byPriority)
                .ticketsByCategory(byCategory)
                .build();
    }
}