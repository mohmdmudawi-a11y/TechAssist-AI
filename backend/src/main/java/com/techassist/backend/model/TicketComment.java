package com.techassist.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * TicketComment — a message posted on a ticket.
 *
 * In C# / EF Core terms:
 *     [Table("ticket_comments")]
 *     public class TicketComment
 *     {
 *         [Key] public long Id { get; set; }
 *         public Ticket Ticket { get; set; }
 *         public User Author { get; set; }
 *         public string Message { get; set; }
 *         public DateTime CreatedAt { get; set; }
 *     }
 *
 * Comments form the conversation between employee and technician:
 *
 *     Employee : "I cannot connect to the company VPN."
 *     Technician: "Can you confirm your internet connection works?"
 *     Employee : "Yes, websites load normally."
 *     Technician: "Please restart the VPN client and try again."
 *
 * Relationships:
 *     Many comments ──► 1 Ticket  (the parent ticket)
 *     Many comments ──► 1 User    (who wrote the comment)
 *
 * When a Ticket is deleted, its comments are deleted too
 * (orphanRemoval + cascade in the Ticket side — added later).
 */
@Entity
@Table(name = "ticket_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketComment {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Which ticket this comment belongs to.
     *
     * @ManyToOne  : many comments → one ticket
     * @JoinColumn(name = "ticket_id") : FK column in ticket_comments table
     *
     * nullable = false → every comment MUST belong to a ticket.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    /**
     * Who wrote the comment.
     * Can be an Employee or a Technician (both are User entities).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * The comment text.
     * Uses TEXT so users can write detailed messages.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * When the comment was posted.
     * Auto-set by @PrePersist.
     * updatable = false → comments are never edited after posting.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Called by Hibernate before INSERT.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}