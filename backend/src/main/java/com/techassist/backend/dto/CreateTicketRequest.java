package com.techassist.backend.dto;

import com.techassist.backend.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/tickets
 * Sent by an EMPLOYEE to create a new ticket.
 *
 * Example JSON:
 * {
 *   "title": "Cannot connect to VPN",
 *   "description": "Every time I try to connect I get error 809.",
 *   "categoryId": 3,
 *   "priority": "HIGH"
 * }
 *
 * The employee's own userId is taken from the JWT token, NOT from this request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be 5-200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}