package com.vivek.placement.application_service.dto;

import java.math.BigDecimal;

public record StudentDto(Long id, String branch, BigDecimal cgpa, Integer backlogs, Integer graduationYear) {}
