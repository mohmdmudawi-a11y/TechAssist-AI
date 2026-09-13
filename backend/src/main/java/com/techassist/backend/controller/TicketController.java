package com.techassist.backend.controller;

import com.techassist.backend.dto.CreateTicketRequest;
import com.techassist.backend.dto.TicketResponse;
import com.techassist.backend.model.TicketPriority;
import com.techassist.backend.model.TicketStatus;
import com.techassist.backend.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TicketController — REST endpoints for tickets.
 *
 * URLs (all require a valid JWT in the Authorization header):
 *
 *   POST   /api/tickets                      — Employee creates a ticket
 *   GET    /api/tickets                      — Admin: get all tickets
 *   GET    /api/tickets/{id}                 — Get one ticket
 *   GET    /api/tickets/mine                 — Employee: my tickets
 *   GET    /api/tickets/assigned             — Technician: my assigned tickets
 *   POST   /api/tickets/{id}/assign          — Assign a technician
 *   PUT    /api/tickets/{id}/status          — Change status
 *   PUT    /api/tickets/{id}/priority        — Change priority
 *   POST   /api/tickets/{id}/resolve         — Resolve with a note
 *
 * Roles are enforced with @PreAuthorize (method-level security).
 * This works because SecurityConfig has @EnableMethodSecurity (we'll add it).
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Employee creates a new ticket.
     * The employee's identity comes from the JWT (Authentication object),
     * not from the request body — that way nobody can spoof someone else.
     */
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            Authentication authentication) {
        String email = authentication.getName();   // email from JWT subject
        return ResponseEntity.ok(ticketService.createTicket(request, email));
    }

    /**
     * Admin: get every ticket in the system.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    /**
     * Get one ticket by ID.
     * Access control inside the service can be added later if needed.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    /**
     * Employee: list the tickets I created.
     */
    @GetMapping("/mine")
    public ResponseEntity<List<TicketResponse>> getMyTickets(Authentication authentication) {
        return ResponseEntity.ok(ticketService.getTicketsByEmployee(authentication.getName()));
    }

    /**
     * Technician: list the tickets assigned to me.
     */
    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAssignedTickets(Authentication authentication) {
        return ResponseEntity.ok(ticketService.getTicketsByTechnician(authentication.getName()));
    }

    /**
     * Assign a technician to a ticket.
     * Body example: { "technicianId": 2 }
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponse> assignTechnician(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        Long technicianId = body.get("technicianId");
        return ResponseEntity.ok(ticketService.assignTechnician(id, technicianId));
    }

    /**
     * Change status.
     * Body example: { "status": "IN_PROGRESS" }
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        TicketStatus newStatus = TicketStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(ticketService.updateStatus(id, newStatus));
    }

    /**
     * Change priority.
     * Body example: { "priority": "CRITICAL" }
     */
    @PutMapping("/{id}/priority")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<TicketResponse> updatePriority(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        TicketPriority newPriority = TicketPriority.valueOf(body.get("priority"));
        return ResponseEntity.ok(ticketService.updatePriority(id, newPriority));
    }

    /**
     * Resolve a ticket with a resolution note.
     * Body example: { "resolution": "Restarted VPN service on the client." }
     */
    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<TicketResponse> resolveTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String resolution = body.get("resolution");
        return ResponseEntity.ok(ticketService.resolveTicket(id, resolution));
    }
}