package org.example.organizationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.organizationservice.model.Organization;
import org.example.organizationservice.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="v1/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService service;

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getOrganization(
            @PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(service.findById(organizationId));
    }

    @PutMapping("/{organizationId}")
    public void updateOrganization(
            @PathVariable("organizationId") String id,
            @RequestBody Organization organization) {
        organization.setId(id);
        service.update(organization);
    }

    @PostMapping
    public ResponseEntity<Organization> saveOrganization(
            @RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @DeleteMapping("/{organizationId}")
    public void deleteOrganization(
            @PathVariable("organizationId") String id,
            @RequestBody Organization organization) {
        Organization org = service.findById(id);
        if (org != null) {
            service.delete(organization);
        }
    }
}
