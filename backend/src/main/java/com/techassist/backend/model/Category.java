package com.techassist.backend.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Category — a broad classification for tickets.
 *
 * In C# / EF Core terms, this is like:
 *     [Table("categories")]
 *     public class Category
 *     {
 *         [Key] public long Id { get; set; }
 *         public string Name { get; set; }
 *         public string Description { get; set; }
 *     }
 *
 * Categories are the top level of ticket classification. Examples:
 *   Hardware, Software, Network, Account & Access, Security
 *
 * A technician/admin can create new categories at any time
 * (that's why this is its own table instead of an enum).
 *
 * Relationships (added in Ticket.java):
 *   Category 1 ──── * Tickets
 *   One category can have many tickets.
 */
@Entity
@Table(name = "categories")
// Lombok annotations below generate all the boilerplate:
//   @Getter        : generates getId(), getName(), etc.
//   @Setter        : generates setId(...), setName(...), etc.
//   @NoArgsConstructor  : generates a no-arg constructor (required by JPA)
//   @AllArgsConstructor : generates a constructor with every field
//   @Builder       : generates Category.builder().name("Network").build()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    /**
     * Primary key.
     * GenerationType.IDENTITY → PostgreSQL uses SERIAL (auto-increment).
     * Same as [DatabaseGenerated(DatabaseGeneratedOption.Identity)] in EF Core.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Category name, e.g. "Network".
     *
     *   nullable = false → NOT NULL in the database
     *   unique   = true  → no two categories can have the same name
     *   length   = 50    → VARCHAR(50)
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Short human-friendly description.
     * Optional (no "nullable = false"), max 200 chars.
     * Example: "Wi-Fi, VPN, DNS, and general connectivity issues."
     */
    @Column(length = 200)
    private String description;
}