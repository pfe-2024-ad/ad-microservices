package com.eai.relaunch_service.service;

import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientResponseForRelanche;
import com.eai.openfeignservice.user.UserClient;
import com.eai.relaunch_service.model.Relaunch;
import com.eai.relaunch_service.outils.enums.RelaunchMovementStatus;
import com.eai.relaunch_service.repository.RelaunchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelaunchService {

    private final RelaunchRepository relaunchRepository;

    private final UserClient clientServiceFeignClient;

    private final NotificationClient notificationClient;

    @Scheduled(cron = "0 0 9 * * ?") //  9h mat tous les jours
    public void scheduledProcessRelaunch() {
        processRelaunch();
    }

    @Transactional
    public void processRelaunch() {

        List<ClientResponseForRelanche> clients = clientServiceFeignClient.getClientForRelanche();

        for (ClientResponseForRelanche client : clients) {
            Optional<Relaunch> optionalRelaunch = relaunchRepository.findByIdClient(client.getIdClient());

            if (optionalRelaunch.isPresent()) {
                Relaunch relaunch = optionalRelaunch.get();

                if (RelaunchMovementStatus.NO_LONGER_CONCERNED.equals(relaunch.getRelaunchStatus())) {
                    continue;
                } else if (RelaunchMovementStatus.NONE.equals(relaunch.getRelaunchStatus())) {
                    relaunch.setRelaunchStatus(RelaunchMovementStatus.FIRST_RELAUNCH);
                } else if (RelaunchMovementStatus.FIRST_RELAUNCH.equals(relaunch.getRelaunchStatus())) {
                    relaunch.setRelaunchStatus(RelaunchMovementStatus.SECOND_RELAUNCH);
                } else if (RelaunchMovementStatus.SECOND_RELAUNCH.equals(relaunch.getRelaunchStatus())) {
                    relaunch.setRelaunchStatus(RelaunchMovementStatus.NO_LONGER_CONCERNED);
                }

                relaunchRepository.save(relaunch);

                EmailSender emailSender = EmailSender.builder()
                        .email(client.getEmail())
                        .build();
                notificationClient.sendRelaunchEmail(emailSender);


            } else {
                Relaunch newRelaunch = new Relaunch();
                newRelaunch.setIdClient(client.getIdClient());
                newRelaunch.setRelaunchStatus(RelaunchMovementStatus.FIRST_RELAUNCH);
                relaunchRepository.save(newRelaunch);
                EmailSender emailSender = EmailSender.builder()
                        .email(client.getEmail())
                        .build();
                notificationClient.sendRelaunchEmail(emailSender);

            }
        }
    }

    public void changeRelaunchStatusToNone(Integer idClient) {
        Optional<Relaunch> optionalRelaunch = relaunchRepository.findByIdClient(idClient);

        if (optionalRelaunch.isPresent()) {
            Relaunch relaunch = optionalRelaunch.get();
            relaunch.setRelaunchStatus(RelaunchMovementStatus.NONE);
            relaunchRepository.save(relaunch);
        }
    }

}
