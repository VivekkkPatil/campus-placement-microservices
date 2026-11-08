package com.vivek.placement.drive_service.service;

import com.vivek.placement.drive_service.dto.DriveRequest;
import com.vivek.placement.drive_service.dto.DriveResponse;
import com.vivek.placement.drive_service.dto.EligibilityResponse;
import com.vivek.placement.drive_service.entity.Company;
import com.vivek.placement.drive_service.entity.Drive;
import com.vivek.placement.drive_service.exception.ResourceNotFoundException;
import com.vivek.placement.drive_service.repository.CompanyRepository;
import com.vivek.placement.drive_service.repository.DriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriveService {

    private final DriveRepository driveRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public DriveResponse create(DriveRequest req) {
        Company company = companyRepository.findById(req.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", req.companyId()));
        Drive d = new Drive();
        d.setCompany(company);
        d.setRole(req.role());
        d.setPackageLpa(req.packageLpa());
        d.setMinCgpa(req.minCgpa());
        d.setMaxBacklogs(req.maxBacklogs());
        d.setAllowedBranches(req.allowedBranches());
        d.setGraduationYear(req.graduationYear());
        d.setDeadline(req.deadline());
        return toResponse(driveRepository.save(d));
    }

    // BAD on purpose: N+1 problem. 1 query for drives + 1 query per company.
    @Transactional(readOnly = true)
    public List<DriveResponse> listSlow() {
        return driveRepository.findAll().stream().map(this::toResponse).toList();
    }

    // FIXED: one query using @EntityGraph.
    @Transactional(readOnly = true)
    public List<DriveResponse> listFast() {
        return driveRepository.findAllWithCompany().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DriveResponse get(Long id) {
        return driveRepository.findWithCompanyById(id).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Drive", id));
    }

    @Transactional(readOnly = true)
    public EligibilityResponse checkEligibility(Long id, BigDecimal cgpa, int backlogs,
                                                String branch, int graduationYear) {
        Drive d = driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drive", id));

        if (!"OPEN".equals(d.getStatus()) || d.getDeadline().isBefore(LocalDate.now())) {
            return new EligibilityResponse(false, "Drive is closed");
        }
        if (cgpa.compareTo(d.getMinCgpa()) < 0) {
            return new EligibilityResponse(false, "CGPA below minimum " + d.getMinCgpa());
        }
        if (backlogs > d.getMaxBacklogs()) {
            return new EligibilityResponse(false, "Backlogs above allowed " + d.getMaxBacklogs());
        }
        boolean branchOk = Arrays.stream(d.getAllowedBranches().split(","))
                .anyMatch(b -> b.trim().equalsIgnoreCase(branch));
        if (!branchOk) {
            return new EligibilityResponse(false, "Branch not allowed");
        }
        if (graduationYear != d.getGraduationYear()) {
            return new EligibilityResponse(false, "Graduation year must be " + d.getGraduationYear());
        }
        return new EligibilityResponse(true, "Eligible");
    }

    private DriveResponse toResponse(Drive d) {
        return new DriveResponse(d.getId(), d.getCompany().getId(), d.getCompany().getName(),
                d.getRole(), d.getPackageLpa(), d.getMinCgpa(), d.getMaxBacklogs(),
                d.getAllowedBranches(), d.getGraduationYear(), d.getDeadline(), d.getStatus());
    }
}
