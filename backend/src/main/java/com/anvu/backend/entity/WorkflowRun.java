package com.anvu.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_runs")
public class WorkflowRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String workflowName;

    @Column(nullable = false)
    private String status; // PENDING_APPROVAL, APPROVED, REJECTED, COMPLETED, FAILED

    @Lob
    @Column(columnDefinition = "TEXT")
    private String draftSubject;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String draftBody;

    private String targetEmail;

    @Column(nullable = false)
    private String createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDraftSubject() { return draftSubject; }
    public void setDraftSubject(String draftSubject) { this.draftSubject = draftSubject; }
    public String getDraftBody() { return draftBody; }
    public void setDraftBody(String draftBody) { this.draftBody = draftBody; }
    public String getTargetEmail() { return targetEmail; }
    public void setTargetEmail(String targetEmail) { this.targetEmail = targetEmail; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}