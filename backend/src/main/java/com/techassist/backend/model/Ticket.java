package com.techassist.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Ticket — the core entity of TechAssist AI.
 *
 * In C# / EF Core terms:
 *     [Table("tickets")]
 *     public class Ticket
 *     {
 *         [Key] public long Id { get; set; }
 *         public string Title { get; set; }
 *         public TicketStatus Status { get; set; }
 *         public User Employee { get; set; }
 *         public User AssignedTechnician { get; set; }
 *         public Category Category { get; set; }
 *         // ... etc
 *     }
 *
 * A ticket represents ONE IT problem reported by ONE employee.
 * It has:
 *   - Basic info:    title, description
 *   - Classification: category, priority, status
 *   - People:        employee (reporter), assignedTechnician (fixer)
 *   - Resolution:    how it was fixed
 *   - Timestamps:    created, updated, resolved
 *
 * Relationships:
 *   Many tickets ────► 1 Category           (e.g. Network)
 *   Many tickets ────► 1 Employee (User)    (the reporter)
 *   Many tickets ────► 1 Technician (User)  (may be null until assigned)
 *   One ticket  ────► Many Comments         (added in TicketComment.java)
 */
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    /**
     * Primary key — auto-increment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Short summary of the problem.
     * Example: "Cannot connect to VPN"
     * Max 200 chars so it fits nicely in list views.
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Full description of the problem.
     * Uses TEXT (unlimited length) because users can write a lot.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * The ticket's current state in the workflow.
     * Stored as a String in DB ("NEW", "ASSIGNED", etc.)
     * Defaults to NEW for new tickets.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TicketStatus status = TicketStatus.NEW;

    /**
     * How urgent the ticket is.
     * Stored as a String in DB ("HIGH", "CRITICAL", etc.)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority;

    /**
     * Which category this ticket belongs to (Network, Hardware, etc.).
     *
     * @ManyToOne : many tickets can point to the same category.
     * @JoinColumn(name = "category_id") : FK column in the tickets table.
     *
     * fetch = LAZY : don't load the Category unless we ask for it.
     *                Avoids loading huge object trees automatically.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * The employee who reported the problem.
     * Never null — every ticket has a reporter.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    /**
     * The technician assigned to fix it.
     * NULL until a technician is assigned (ticket is NEW).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User assignedTechnician;

    /**
     * How the problem was resolved.
     * NULL until the ticket is marked RESOLVED.
     */
    @Column(columnDefinition = "TEXT")
    private String resolution;

    /**
     * When the ticket was created.
     * Set automatically by @PrePersist below.
     * updatable = false → once written, never changes.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When the ticket was last updated.
     * Refreshed automatically by @PreUpdate below.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * When the ticket was marked RESOLVED.
     * Set manually in the service when status changes to RESOLVED.
     */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    /**
     * Called by Hibernate right before INSERT.
     * Sets createdAt and updatedAt to "now".
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Called by Hibernate right before UPDATE.
     * Refreshes updatedAt to "now".
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}