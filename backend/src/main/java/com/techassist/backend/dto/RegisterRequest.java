package com.techassist.backend.dto;

import com.techassist.backend.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * A DTO (Data Transfer Object) is a simple class that describes 
 * what comes into or goes out of an API endpoint.
 *
 *Why we need it: We don't want to expose 
 *the User entity directly to the API 
 *(it contains password hashes). 
 *Instead, we accept a clean RegisterRequest with just email, 
 *password, firstName, lastName, and role.
 *
 *
 * Request body for POST /api/auth/register
 *
 * Example JSON:
 * {
 *   "firstName": "John",
 *   "lastName": "Doe",
 *   "email": "john.doe@company.com",
 *   "password": "SecureP@ss123",
 *   "role": "EMPLOYEE"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be under 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}