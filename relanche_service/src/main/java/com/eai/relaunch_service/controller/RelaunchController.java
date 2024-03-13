package com.eai.relaunch_service.controller;

import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.relanche.RelancheRequest;
import com.eai.relaunch_service.service.RelaunchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RelaunchController {
    private final RelaunchService relaunchService;

    @GetMapping("/process-relaunch")
    public void processRelaunch() {
        relaunchService.processRelaunch();
    }

    @PostMapping("/set-status-none")
    public void changeRelaunchStatusToNone(@RequestBody RelancheRequest relancheRequest){
        relaunchService.changeRelaunchStatusToNone(relancheRequest.getIdClient());
    }
}
