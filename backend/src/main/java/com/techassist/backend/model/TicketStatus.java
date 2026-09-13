package com.techassist.backend.model;

/**
 * TicketStatus — the lifecycle states of a support ticket.
 *
 * This is a Java enum, identical in concept to a C# enum:
 *     public enum TicketStatus { NEW, ASSIGNED, ... }
 *
 * The ticket workflow in TechAssist AI follows this exact order:
 *
 *     NEW ──► ASSIGNED ──► IN_PROGRESS ──► RESOLVED ──► CLOSED
 *
 * Meaning of each state:
 *   NEW         : Employee just created the ticket. No technician assigned yet.
 *   ASSIGNED    : A technician has been assigned but hasn't started working.
 *   IN_PROGRESS : Technician is actively investigating / working on it.
 *   RESOLVED    : Technician marked it fixed. Waiting for confirmation.
 *   CLOSED      : Ticket is done and archived.
 *
 * We use an enum (not a String) so that:
 *   1. Typos are impossible — the compiler rejects "NEWW".
 *   2. IDE autocomplete works (type "TicketStatus." and see all options).
 *   3. PostgreSQL only accepts valid values — Hibernate creates a CHECK constraint.
 */
public enum TicketStatus {
    NEW,
    ASSIGNED,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}