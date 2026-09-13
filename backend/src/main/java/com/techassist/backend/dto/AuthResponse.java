package com.techassist.backend.dto;

import com.techassist.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body returned after successful login or registration.
 *
 * Example JSON:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9...",
 *   "tokenType": "Bearer",
 *   "userId": 1,
 *   "email": "john.doe@company.com",
 *   "firstName": "John",
 *   "lastName": "Doe",
 *   "role": "EMPLOYEE"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    private Role role;
}