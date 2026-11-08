package com.vivek.placement.application_service.dto;

import com.vivek.placement.application_service.entity.ApplicationStatus;
import java.time.Instant;

public record EventResponse(ApplicationStatus fromStatus, ApplicationStatus toStatus, Instant changedAt) {}
