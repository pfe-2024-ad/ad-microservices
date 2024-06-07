package com.eai.openfeignservice.administrateur;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "administrateur-service", path = "/agd/administrateur-service")
public interface AdminClient {

    @PostMapping("/api/get-admin-for-security")
    AdminResponseForSecurity getAdminForSecurity(@RequestBody String email);

    @PostMapping("/api/get-available-agents")
    List<Integer> getAvailableAgents(@RequestBody DateRequest dateRequest);
}
