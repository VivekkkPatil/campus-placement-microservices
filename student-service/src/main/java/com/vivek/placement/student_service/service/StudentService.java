package com.vivek.placement.student_service.service;

import com.vivek.placement.student_service.dto.StudentRequest;
import com.vivek.placement.student_service.dto.StudentResponse;
import com.vivek.placement.student_service.entity.Student;
import com.vivek.placement.student_service.exception.StudentNotFoundException;
import com.vivek.placement.student_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    @Transactional
    public StudentResponse create(StudentRequest req) {
        Student s = new Student();
        s.setRollNo(req.rollNo());
        s.setName(req.name());
        s.setEmail(req.email());
        s.setBranch(req.branch());
        s.setCgpa(req.cgpa());
        s.setBacklogs(req.backlogs());
        s.setGraduationYear(req.graduationYear());
        return toResponse(repository.save(s));
    }

    @Transactional(readOnly = true)
    public StudentResponse get(Long id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    private StudentResponse toResponse(Student s) {
        return new StudentResponse(s.getId(), s.getRollNo(), s.getName(), s.getEmail(),
                s.getBranch(), s.getCgpa(), s.getBacklogs(), s.getGraduationYear());
    }
}
