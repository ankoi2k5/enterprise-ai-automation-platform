package com.anvu.backend.repository;

import com.anvu.backend.entity.WorkflowRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowRunRepository extends JpaRepository<WorkflowRun, Long> {
}