package com.vivek.placement.drive_service.repository;

import com.vivek.placement.drive_service.entity.Drive;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    // Fixed version: loads drives and companies in ONE query
    @EntityGraph(attributePaths = "company")
    @Query("select d from Drive d order by d.id")
    List<Drive> findAllWithCompany();

    @EntityGraph(attributePaths = "company")
    Optional<Drive> findWithCompanyById(Long id);
}
