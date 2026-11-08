package com.vivek.placement.drive_service.controller;

import com.vivek.placement.drive_service.dto.CompanyRequest;
import com.vivek.placement.drive_service.dto.CompanyResponse;
import com.vivek.placement.drive_service.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@Valid @RequestBody CompanyRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<CompanyResponse> list() {
        return service.list();
    }
}
