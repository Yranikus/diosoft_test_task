package com.example.diosoft_test_task.services.implementations;

import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.services.interfaces.ValidationService;
import org.springframework.stereotype.Component;

@Component
public class DataValidationService implements ValidationService {


    //проверяет что бы в имени не было не буквенных символов и обрезает лишние пробелы , если былли
    private boolean validateName(String name) {
        if (name == null) return false;
        if (name.matches("^\\W*.*[a-zА-ЯA-Zа-я]\\W+[a-zА-ЯA-Zа-я].*\\W*$")) return false;
        name = name.replaceAll(" ", "");
        if (name.length() > 80) return false;
        if (name.matches("^.*[0-9]+.*$")) return false;
        return true;
    }

    //проверяет что бы в номере были только числа
    private boolean validateNumber(ContactEntity contactEntity) {
        if (contactEntity.getNumber() == null || contactEntity.getContactTypeEntity() == null) return false;
        contactEntity.getNumber().replaceAll(" ", "");
        if (contactEntity.getNumber().length() > 20) return false;
        return contactEntity.getNumber().matches("^\\s*\\d+\\s*$");
    }

    @Override
    public boolean validateFullName(PersonEntity personEntity) {
        return validateName(personEntity.getFirstName()) &&
                validateName(personEntity.getLastName()) &&
                validateName(personEntity.getMiddleName());
    }

    @Override
    public boolean validateContactNumber(ContactEntity contact) {
        return validateNumber(contact);
    }
}
