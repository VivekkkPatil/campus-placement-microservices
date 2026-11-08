package com.vivek.placement.drive_service.controller;

import com.vivek.placement.drive_service.dto.DriveRequest;
import com.vivek.placement.drive_service.dto.DriveResponse;
import com.vivek.placement.drive_service.dto.EligibilityResponse;
import com.vivek.placement.drive_service.service.DriveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/drives")
@RequiredArgsConstructor
public class DriveController {

    private final DriveService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriveResponse create(@Valid @RequestBody DriveRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<DriveResponse> list() {
        return service.listFast();
    }

    @GetMapping("/slow")
    public List<DriveResponse> listSlow() {
        return service.listSlow();
    }

    @GetMapping("/{id}")
    public DriveResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{id}/eligibility")
    public EligibilityResponse eligibility(@PathVariable Long id,
                                           @RequestParam BigDecimal cgpa,
                                           @RequestParam int backlogs,
                                           @RequestParam String branch,
                                           @RequestParam int graduationYear) {
        return service.checkEligibility(id, cgpa, backlogs, branch, graduationYear);
    }
}
