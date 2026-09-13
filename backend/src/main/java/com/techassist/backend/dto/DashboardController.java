package com.techassist.backend.controller;

import com.techassist.backend.dto.DashboardResponse;
import com.techassist.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DashboardController — statistics endpoints.
 *
 *   GET /api/dashboard/employee    → Employee's ticket stats
 *   GET /api/dashboard/technician  → Technician's assigned ticket stats
 *   GET /api/dashboard/admin       → System-wide stats
 *
 * All require a valid JWT.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/employee")
    public ResponseEntity<DashboardResponse> employeeDashboard(Authentication auth) {
        return ResponseEntity.ok(dashboardService.getEmployeeDashboard(auth.getName()));
    }

    @GetMapping("/technician")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<DashboardResponse> technicianDashboard(Authentication auth) {
        return ResponseEntity.ok(dashboardService.getTechnicianDashboard(auth.getName()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResponse> adminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }
}