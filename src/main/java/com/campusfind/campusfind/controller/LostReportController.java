package com.campusfind.campusfind.controller;

import com.campusfind.campusfind.dto.CreateLostReportRequest;
import com.campusfind.campusfind.entity.LostReport;
import com.campusfind.campusfind.service.LostReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-reports")
public class LostReportController {

    private final LostReportService lostReportService;

    public LostReportController(LostReportService lostReportService) {
        this.lostReportService = lostReportService;
    }

    @PostMapping
    public ResponseEntity<LostReport> createLostReport(@Valid @RequestBody CreateLostReportRequest request) {
        LostReport createdReport = lostReportService.createLostReport(request);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LostReport>> getAllLostReports() {
        return ResponseEntity.ok(lostReportService.getAllLostReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostReport> getLostReportById(@PathVariable Long id) {
        return ResponseEntity.ok(lostReportService.getLostReportById(id));
    }
}
