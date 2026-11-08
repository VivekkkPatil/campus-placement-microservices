package com.vivek.placement.drive_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DriveResponse(
        Long id,
        Long companyId,
        String companyName,
        String role,
        BigDecimal packageLpa,
        BigDecimal minCgpa,
        Integer maxBacklogs,
        String allowedBranches,
        Integer graduationYear,
        LocalDate deadline,
        String status
) {}
