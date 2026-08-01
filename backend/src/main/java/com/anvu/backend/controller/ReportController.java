package com.anvu.backend.controller;

import com.anvu.backend.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system")
    public Map<String, String> generateSystemReport() {
        String report = reportService.generateSystemReport();
        return Map.of("report", report);
    }
}