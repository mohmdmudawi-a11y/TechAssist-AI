package com.techassist.backend.model;

/**
 * TicketPriority — how urgent a ticket is.
 *
 * Same as a C# enum. Used to:
 *   - Sort the technician's queue (Critical first)
 *   - Send alerts on high-priority tickets
 *   - Generate dashboard statistics (e.g. "3 Critical tickets open")
 *
 * Guidelines for choosing a priority:
 *   CRITICAL : Outage affecting many users — drop everything.
 *              Example: "VPN is down for the entire company."
 *   HIGH     : One person blocked from working.
 *              Example: "I cannot access the shared finance folder."
 *   MEDIUM   : Inconvenient but not blocking.
 *              Example: "Outlook is slow."
 *   LOW      : Cosmetic / nice to fix someday.
 *              Example: "My mousepad is worn out."
 */
public enum TicketPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}