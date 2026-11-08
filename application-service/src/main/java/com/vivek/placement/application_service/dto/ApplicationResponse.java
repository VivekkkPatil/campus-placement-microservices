package com.vivek.placement.application_service.dto;

import com.vivek.placement.application_service.entity.ApplicationStatus;
import java.time.Instant;

public record ApplicationResponse(Long id, Long studentId, Long driveId,
                                  ApplicationStatus status, Instant createdAt) {}
