package org.example.license.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.agreement.jpake.JPAKEPrimeOrderGroup;
import org.example.license.model.License;
import org.example.license.model.Organization;
import org.example.license.repository.LicenseRepository;
import org.example.license.service.client.OrganizationFeignClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {
    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final OrganizationFeignClient organizationFeignClient;

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(messages.getMessage(
                        "license.search.error.message", null, null
                ), licenseId, organizationId)));

        Organization org = getOrganization(organizationId);

        if (org != null) {
            license.setOrganizationName(org.getName());
            license.setContactName(org.getContactName());
            license.setContactEmail(org.getContactEmail());
            license.setContactPhone(org.getContactPhone());
        }

        return license;
    }

    @CircuitBreaker(name = "organizationService")
    private Organization getOrganization(String organizationId) {
        return organizationFeignClient.getOrganization(organizationId);
    }

    @CircuitBreaker(name = "licenseService",
            fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService",
            fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService",
            fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService",
            type = Bulkhead.Type.THREADPOOL,
            fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganizationId(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName(
                "Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    private void randomlyRunLog() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
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
