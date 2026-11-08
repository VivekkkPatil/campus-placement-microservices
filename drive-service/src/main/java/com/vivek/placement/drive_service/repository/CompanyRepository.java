package com.vivek.placement.drive_service.repository;

import com.vivek.placement.drive_service.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
