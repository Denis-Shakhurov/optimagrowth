package org.example.license.service;

import lombok.RequiredArgsConstructor;
import org.example.license.model.License;
import org.example.license.repository.LicenseRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseService {
    private final MessageSource messages;
    private final LicenseRepository licenseRepository;

    public License getLicense(String licenseId, String organizationId) {
        return licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(messages.getMessage(
                        "license.search.error.message", null, null
                ), licenseId, organizationId)));
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license;
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license;
    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);

        responseMessage = String.format(
                messages.getMessage("license.delete.message", null, null),
                licenseId
        );
        return responseMessage;
    }
}
