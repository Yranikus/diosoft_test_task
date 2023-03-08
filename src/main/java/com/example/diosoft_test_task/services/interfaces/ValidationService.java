package com.example.diosoft_test_task.services.interfaces;

import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.PersonEntity;

public interface ValidationService {

    boolean validateFullName(PersonEntity personEntity);
    boolean validateContactNumber(ContactEntity contact);

}
