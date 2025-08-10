package org.example.license.service.client;

import org.example.license.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
    @GetMapping("/v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable String organizationId);
}
