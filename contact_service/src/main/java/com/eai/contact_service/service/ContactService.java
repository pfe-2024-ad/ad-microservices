package com.eai.contact_service.service;

import com.eai.contact_service.model.Contact;
import com.eai.contact_service.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact enregistrerContact(Contact contact) {
        return contactRepository.save(contact);
    }
}
