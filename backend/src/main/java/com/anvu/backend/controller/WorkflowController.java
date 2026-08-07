package com.anvu.backend.controller;

import com.anvu.backend.entity.WorkflowRun;
import com.anvu.backend.repository.WorkflowRunRepository;
import com.anvu.backend.service.WorkflowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final WorkflowRunRepository workflowRunRepository;

    public WorkflowController(WorkflowService workflowService, WorkflowRunRepository workflowRunRepository) {
        this.workflowService = workflowService;
        this.workflowRunRepository = workflowRunRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/weekly-report/start")
    public WorkflowRun start(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return workflowService.startWeeklyReportWorkflow(body.get("targetEmail"), email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public WorkflowRun approve(@PathVariable Long id) {
        return workflowService.approveAndExecute(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public WorkflowRun reject(@PathVariable Long id) {
        return workflowService.reject(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<WorkflowRun> getAll() {
        return workflowRunRepository.findAll();
    }
}