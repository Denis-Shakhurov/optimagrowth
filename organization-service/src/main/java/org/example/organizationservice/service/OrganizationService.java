package org.example.organizationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.organizationservice.model.Organization;
import org.example.organizationservice.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;

    public Organization findById(String organizationId) {
        return repository.findById(organizationId)
                .orElse(null);
    }

    public Organization create(Organization organization){
        organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
    }

    public void delete(Organization organization){
        repository.deleteById(organization.getId());
    }
}
