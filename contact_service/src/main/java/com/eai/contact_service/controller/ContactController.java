package com.eai.contact_service.controller;

import com.eai.contact_service.model.Contact;
import com.eai.contact_service.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> ajouterContact(@RequestBody Contact contact) {
        Contact nouveauContact = contactService.enregistrerContact(contact);
        return new ResponseEntity<>(nouveauContact, HttpStatus.CREATED);
    }
}
