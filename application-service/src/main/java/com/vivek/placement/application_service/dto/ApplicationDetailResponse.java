package com.vivek.placement.application_service.dto;

import com.vivek.placement.application_service.entity.ApplicationStatus;
import java.time.Instant;
import java.util.List;

public record ApplicationDetailResponse(Long id, Long studentId, Long driveId,
                                        ApplicationStatus status, Instant createdAt,
                                        List<EventResponse> history) {}
