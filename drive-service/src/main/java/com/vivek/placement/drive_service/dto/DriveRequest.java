package com.vivek.placement.drive_service.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DriveRequest(
        @NotNull Long companyId,
        @NotBlank String role,
        @NotNull @DecimalMin("0.0") BigDecimal packageLpa,
        @NotNull @DecimalMin("0.0") @DecimalMax("10.0") BigDecimal minCgpa,
        @NotNull @Min(0) Integer maxBacklogs,
        @NotBlank String allowedBranches,
        @NotNull Integer graduationYear,
        @NotNull LocalDate deadline
) {}
