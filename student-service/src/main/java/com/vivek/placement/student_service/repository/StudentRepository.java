package com.vivek.placement.student_service.repository;

import com.vivek.placement.student_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
