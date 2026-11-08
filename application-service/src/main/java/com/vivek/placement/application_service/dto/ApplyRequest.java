package com.vivek.placement.application_service.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyRequest(@NotNull Long studentId, @NotNull Long driveId) {}
