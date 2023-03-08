package com.example.diosoft_test_task.builders;

import com.example.diosoft_test_task.entities.PersonEntity;
import org.springframework.stereotype.Component;

//Собирает person без id.
@Component
public class PersonBuilder {

    public PersonEntity build(String first_name, String last_name, String middle_name){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(first_name);
        personEntity.setLastName(last_name);
        personEntity.setMiddleName(middle_name);
        return personEntity;
    }

}
