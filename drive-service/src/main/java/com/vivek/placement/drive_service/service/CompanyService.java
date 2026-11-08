package com.vivek.placement.drive_service.service;

import com.vivek.placement.drive_service.dto.CompanyRequest;
import com.vivek.placement.drive_service.dto.CompanyResponse;
import com.vivek.placement.drive_service.entity.Company;
import com.vivek.placement.drive_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    @Transactional
    public CompanyResponse create(CompanyRequest req) {
        Company c = new Company();
        c.setName(req.name());
        c.setIndustry(req.industry());
        c.setWebsite(req.website());
        return toResponse(repository.save(c));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(c.getId(), c.getName(), c.getIndustry(), c.getWebsite());
    }
}
