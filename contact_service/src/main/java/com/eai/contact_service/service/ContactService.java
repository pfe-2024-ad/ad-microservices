package com.eai.contact_service.service;

import com.eai.contact_service.model.Contact;
import com.eai.contact_service.repository.ContactRepository;

import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final NotificationClient notificationClient;

    @Autowired
    public ContactService(ContactRepository contactRepository, NotificationClient notificationClient) {
        this.contactRepository = contactRepository;
        this.notificationClient = notificationClient;
    }

    public Contact enregistrerContact(Contact contact) {
        EmailSender emailSender = EmailSender.builder()
                .email("ibtissame.arrai@gmail.com")
                .build();
        notificationClient.sendContactEmail(emailSender);
        return contactRepository.save(contact);
    }
}
