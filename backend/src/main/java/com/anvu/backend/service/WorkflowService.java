package com.anvu.backend.service;

import com.anvu.backend.entity.WorkflowRun;
import com.anvu.backend.repository.WorkflowRunRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkflowService {

    private final ReportService reportService;
    private final EmailDraftService emailDraftService;
    private final EmailSenderService emailSenderService;
    private final WorkflowRunRepository workflowRunRepository;

    public WorkflowService(ReportService reportService, EmailDraftService emailDraftService,
                           EmailSenderService emailSenderService, WorkflowRunRepository workflowRunRepository) {
        this.reportService = reportService;
        this.emailDraftService = emailDraftService;
        this.emailSenderService = emailSenderService;
        this.workflowRunRepository = workflowRunRepository;
    }

    // BUOC 1 + 2 cua workflow: AI tao bao cao, AI soan email chua bao cao do -> dung lai cho duyet
    public WorkflowRun startWeeklyReportWorkflow(String targetEmail, String createdBy) {
        // Buoc 1: goi Report Agent (da xay Ngay 20)
        String report = reportService.generateSystemReport();

        // Buoc 2: goi Email Agent de soan email chua noi dung bao cao (da xay Ngay 22)
        Map<String, String> draft = emailDraftService.draftEmail(
                "Soan 1 email gui bao cao he thong sau day den nguoi nhan, giu nguyen noi dung bao cao:\n\n" + report,
                "Admin"
        );

        // Luu lai, trang thai cho duyet - CHUA gui email that
        WorkflowRun run = new WorkflowRun();
        run.setWorkflowName("Weekly System Report");
        run.setStatus("PENDING_APPROVAL");
        run.setDraftSubject(draft.get("subject"));
        run.setDraftBody(draft.get("body"));
        run.setTargetEmail(targetEmail);
        run.setCreatedBy(createdBy);
        return workflowRunRepository.save(run);
    }

    // BUOC 3 cua workflow: con nguoi duyet -> moi thuc su gui email
    public WorkflowRun approveAndExecute(Long runId) {
        WorkflowRun run = workflowRunRepository.findById(runId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay workflow"));

        if (!"PENDING_APPROVAL".equals(run.getStatus())) {
            throw new RuntimeException("Workflow nay khong o trang thai cho duyet");
        }

        try {
            emailSenderService.sendEmail(run.getTargetEmail(), run.getDraftSubject(), run.getDraftBody());
            run.setStatus("COMPLETED");
        } catch (Exception e) {
            run.setStatus("FAILED");
        }
        return workflowRunRepository.save(run);
    }

    public WorkflowRun reject(Long runId) {
        WorkflowRun run = workflowRunRepository.findById(runId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay workflow"));
        run.setStatus("REJECTED");
        return workflowRunRepository.save(run);
    }
}