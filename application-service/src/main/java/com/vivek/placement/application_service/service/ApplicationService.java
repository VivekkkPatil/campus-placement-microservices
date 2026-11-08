package com.vivek.placement.application_service.service;

import com.vivek.placement.application_service.client.DriveClient;
import com.vivek.placement.application_service.client.StudentClient;
import com.vivek.placement.application_service.dto.*;
import com.vivek.placement.application_service.entity.Application;
import com.vivek.placement.application_service.entity.ApplicationStatus;
import com.vivek.placement.application_service.exception.*;
import com.vivek.placement.application_service.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository repository;
    private final StudentClient studentClient;
    private final DriveClient driveClient;

    // Not @Transactional on purpose: remote calls should not hold a DB connection.
    // repository.save() runs in its own short transaction.
    public ApplicationResponse apply(ApplyRequest req) {
        if (repository.existsByStudentIdAndDriveId(req.studentId(), req.driveId())) {
            throw new DuplicateApplicationException();
        }
        StudentDto student = studentClient.getStudent(req.studentId());
        if (student == null) throw new ResourceNotFoundException("Student", req.studentId());

        EligibilityDto eligibility = driveClient.checkEligibility(req.driveId(), student);
        if (eligibility == null) throw new ResourceNotFoundException("Drive", req.driveId());
        if (!eligibility.eligible()) throw new NotEligibleException(eligibility.reason());

        Application app = new Application();
        app.setStudentId(req.studentId());
        app.setDriveId(req.driveId());
        app.moveTo(ApplicationStatus.APPLIED);
        return toResponse(repository.save(app));
    }

    @Transactional
    public ApplicationResponse advance(Long id) {
        Application app = find(id);
        if (app.getStatus().isFinal()) {
            throw new InvalidTransitionException("Application is already " + app.getStatus());
        }
        app.moveTo(app.getStatus().next());
        return toResponse(app);
    }

    @Transactional
    public ApplicationResponse reject(Long id) {
        Application app = find(id);
        if (app.getStatus().isFinal()) {
            throw new InvalidTransitionException("Application is already " + app.getStatus());
        }
        app.moveTo(ApplicationStatus.REJECTED);
        return toResponse(app);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailResponse get(Long id) {
        Application a = repository.findWithEventsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
        List<EventResponse> history = a.getEvents().stream()
                .map(e -> new EventResponse(e.getFromStatus(), e.getToStatus(), e.getChangedAt()))
                .toList();
        return new ApplicationDetailResponse(a.getId(), a.getStudentId(), a.getDriveId(),
                a.getStatus(), a.getCreatedAt(), history);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> byStudent(Long studentId) {
        return repository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> byDrive(Long driveId) {
        return repository.findByDriveId(driveId).stream().map(this::toResponse).toList();
    }

    private Application find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }

    private ApplicationResponse toResponse(Application a) {
        return new ApplicationResponse(a.getId(), a.getStudentId(), a.getDriveId(), a.getStatus(), a.getCreatedAt());
    }
}
