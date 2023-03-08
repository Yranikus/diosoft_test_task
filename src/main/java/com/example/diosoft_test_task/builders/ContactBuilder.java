package com.example.diosoft_test_task.builders;


import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import org.springframework.stereotype.Component;


//Класс который собирвет гототвый для сохранения контакт.
@Component
public class ContactBuilder {

    public ContactEntity build(PersonEntity personEntity, ContactEntity contactEntity){
        contactEntity.setPerson(personEntity);
        return contactEntity;
    }



}
