package com.techassist.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * DashboardResponse — statistics for the dashboard.
 *
 * Used by all three dashboards (Employee, Technician, Admin).
 * Fields that aren't relevant (e.g. allTechnicians for an employee)
 * are simply left null or empty.
 *
 * Example JSON (admin):
 * {
 *   "totalTickets": 42,
 *   "openTickets": 15,
 *   "resolvedTickets": 27,
 *   "criticalTickets": 3,
 *   "ticketsByStatus": { "NEW": 5, "ASSIGNED": 4, ... },
 *   "ticketsByPriority": { "CRITICAL": 3, "HIGH": 8, ... },
 *   "ticketsByCategory": { "Network": 12, "Hardware": 7, ... }
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // Common counters
    private long totalTickets;
    private long openTickets;
    private long resolvedTickets;
    private long criticalTickets;

    // Breakdowns
    private Map<String, Long> ticketsByStatus;
    private Map<String, Long> ticketsByPriority;
    private Map<String, Long> ticketsByCategory;
}