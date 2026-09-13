package com.techassist.backend.model;

/**
 * Defines the roles available in the TechAssist AI system.
 *
 * Each user has exactly one role, which determines what they can do:
 *   - EMPLOYEE   : Creates tickets and views their own tickets
 *   - TECHNICIAN : Handles assigned tickets, uses AI troubleshooting
 *   - ADMIN      : Manages users, categories, and views everything
 */
public enum Role {
    EMPLOYEE,
    TECHNICIAN,
    ADMIN
}