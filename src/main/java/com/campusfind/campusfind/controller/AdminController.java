package com.campusfind.campusfind.controller;

import com.campusfind.campusfind.dto.AdminDashboardResponse;
import com.campusfind.campusfind.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboardData() {
        AdminDashboardResponse response = adminService.getDashboardData();
        return ResponseEntity.ok(response);
    }
}
