package com.techassist.backend.dto;

import com.techassist.backend.model.TicketPriority;
import com.techassist.backend.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response body for ticket endpoints.
 * Returned by GET, POST, and PUT /api/tickets.
 *
 * Example JSON:
 * {
 *   "id": 1,
 *   "title": "Cannot connect to VPN",
 *   "description": "Every time I try to connect I get error 809.",
 *   "status": "NEW",
 *   "priority": "HIGH",
 *   "categoryId": 3,
 *   "categoryName": "Network",
 *   "employeeId": 1,
 *   "employeeName": "John Doe",
 *   "technicianId": null,
 *   "technicianName": null,
 *   "resolution": null,
 *   "createdAt": "2026-09-11T20:14:00",
 *   "updatedAt": "2026-09-11T20:14:00",
 *   "resolvedAt": null
 * }
 *
 * We send categoryName / employeeName / technicianName directly
 * so the frontend doesn't need to make extra API calls.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;

    private Long categoryId;
    private String categoryName;

    private Long employeeId;
    private String employeeName;

    private Long technicianId;
    private String technicianName;

    private String resolution;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}