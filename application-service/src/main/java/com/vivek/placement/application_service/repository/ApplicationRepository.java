package com.vivek.placement.application_service.repository;

import com.vivek.placement.application_service.entity.Application;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByStudentIdAndDriveId(Long studentId, Long driveId);

    List<Application> findByStudentId(Long studentId);

    List<Application> findByDriveId(Long driveId);

    @EntityGraph(attributePaths = "events")
    Optional<Application> findWithEventsById(Long id);
}
