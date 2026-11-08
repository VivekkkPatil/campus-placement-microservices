package com.vivek.placement.application_service.controller;

import com.vivek.placement.application_service.dto.*;
import com.vivek.placement.application_service.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(@Valid @RequestBody ApplyRequest req) {
        return service.apply(req);
    }

    @GetMapping("/{id}")
    public ApplicationDetailResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/student/{studentId}")
    public List<ApplicationResponse> byStudent(@PathVariable Long studentId) {
        return service.byStudent(studentId);
    }

    @GetMapping("/drive/{driveId}")
    public List<ApplicationResponse> byDrive(@PathVariable Long driveId) {
        return service.byDrive(driveId);
    }

    @PatchMapping("/{id}/advance")
    public ApplicationResponse advance(@PathVariable Long id) {
        return service.advance(id);
    }

    @PatchMapping("/{id}/reject")
    public ApplicationResponse reject(@PathVariable Long id) {
        return service.reject(id);
    }
}
