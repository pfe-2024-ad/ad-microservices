package com.eai.openfeignservice.contact;

import com.eai.openfeignservice.relanche.RelancheRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ContactClient {

    @PostMapping("/set-status-none")
    void changeRelaunchStatusToNone(@RequestBody RelancheRequest relaunchRequest);
}
