package com.vivek.placement.student_service.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record StudentRequest(
        @NotBlank String rollNo,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String branch,
        @NotNull @DecimalMin("0.0") @DecimalMax("10.0") BigDecimal cgpa,
        @NotNull @Min(0) Integer backlogs,
        @NotNull Integer graduationYear
) {}
