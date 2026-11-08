package com.vivek.placement.student_service.controller;

import com.vivek.placement.student_service.dto.StudentRequest;
import com.vivek.placement.student_service.dto.StudentResponse;
import com.vivek.placement.student_service.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<StudentResponse> list(Pageable pageable) {
        return service.list(pageable);
    }
}
