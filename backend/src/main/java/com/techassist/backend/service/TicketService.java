package com.techassist.backend.service;

import com.techassist.backend.dto.CreateTicketRequest;
import com.techassist.backend.dto.TicketResponse;
import com.techassist.backend.model.*;
import com.techassist.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TicketService — business logic for tickets.
 *
 * In WebForms terms: this is like the code-behind of Tickets.aspx,
 * but WITHOUT any UI. It only knows how to talk to the database.
 *
 * Responsibilities:
 *   - Create tickets (from CreateTicketRequest)
 *   - Fetch tickets (converted to TicketResponse)
 *   - Assign technicians
 *   - Update status and priority
 *   - Resolve tickets
 *   - Update a ticket's category
 *
 * The controller is thin — it just delegates here.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new ticket.
     * Called by an EMPLOYEE via POST /api/tickets.
     */
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request, String employeeEmail) {
        // 1. Look up the employee by email (from JWT)
        User employee = userRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 2. Look up the category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3. Build the ticket
        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TicketStatus.NEW)
                .category(category)
                .employee(employee)
                .build();

        // 4. Save — Hibernate sets createdAt / updatedAt automatically
        ticketRepository.save(ticket);

        return toResponse(ticket);
    }

    /**
     * Get all tickets (ADMIN view).
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Get one ticket by ID.
     */
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
        return toResponse(ticket);
    }

    /**
     * Get all tickets created by a specific employee.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByEmployee(String email) {
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return ticketRepository.findByEmployeeOrderByCreatedAtDesc(employee)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Get all tickets assigned to a specific technician.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByTechnician(String email) {
        User technician = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        return ticketRepository.findByAssignedTechnicianOrderByCreatedAtDesc(technician)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Assign a technician to a ticket.
     * Called by an ADMIN (or self-assign by a technician).
     * Automatically moves the ticket from NEW → ASSIGNED.
     */
    @Transactional
    public TicketResponse assignTechnician(Long ticketId, Long technicianId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        // Make sure the user actually has the TECHNICIAN (or ADMIN) role
        if (technician.getRole() != Role.TECHNICIAN && technician.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not a technician: " + technician.getEmail());
        }

        ticket.setAssignedTechnician(technician);

        // Move status forward only if still NEW
        if (ticket.getStatus() == TicketStatus.NEW) {
            ticket.setStatus(TicketStatus.ASSIGNED);
        }

        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    /**
     * Change the status of a ticket (technician action).
     * When set to RESOLVED, we also stamp resolvedAt.
     */
    @Transactional
    public TicketResponse updateStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(newStatus);

        if (newStatus == TicketStatus.RESOLVED && ticket.getResolvedAt() == null) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    /**
     * Change the priority of a ticket.
     */
    @Transactional
    public TicketResponse updatePriority(Long ticketId, TicketPriority newPriority) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setPriority(newPriority);
        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    /**
     * Resolve a ticket — set status to RESOLVED with a resolution note.
     */
    @Transactional
    public TicketResponse resolveTicket(Long ticketId, String resolution) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolution(resolution);
        ticket.setResolvedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    /**
     * Convert a Ticket entity to a TicketResponse DTO.
     * This is where we "join" the extra display fields
     * (categoryName, employeeName, technicianName).
     */
    private TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .categoryId(ticket.getCategory().getId())
                .categoryName(ticket.getCategory().getName())
                .employeeId(ticket.getEmployee().getId())
                .employeeName(ticket.getEmployee().getFirstName() + " " + ticket.getEmployee().getLastName())
                .technicianId(ticket.getAssignedTechnician() != null ? ticket.getAssignedTechnician().getId() : null)
                .technicianName(ticket.getAssignedTechnician() != null
                        ? ticket.getAssignedTechnician().getFirstName() + " " + ticket.getAssignedTechnician().getLastName()
                        : null)
                .resolution(ticket.getResolution())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .resolvedAt(ticket.getResolvedAt())
                .build();
    }
}