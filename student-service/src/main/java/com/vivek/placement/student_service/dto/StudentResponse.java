package com.vivek.placement.student_service.dto;

import java.math.BigDecimal;

public record StudentResponse(
        Long id,
        String rollNo,
        String name,
        String email,
        String branch,
        BigDecimal cgpa,
        Integer backlogs,
        Integer graduationYear
) {}
