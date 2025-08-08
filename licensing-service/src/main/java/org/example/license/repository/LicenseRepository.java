package org.example.license.repository;

import org.example.license.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends CrudRepository<License, String> {
    List<License> findByOrganizationId(String organizationId);

    Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
