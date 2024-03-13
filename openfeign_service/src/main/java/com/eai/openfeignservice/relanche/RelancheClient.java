package com.eai.openfeignservice.relanche;

import com.eai.openfeignservice.relanche.RelancheRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "relaunch-service", path = "/agd/relaunch-service")
public interface RelancheClient {
    @PostMapping("/set-status-none")
    void changeRelaunchStatusToNone(@RequestBody RelancheRequest relaunchRequest);

}