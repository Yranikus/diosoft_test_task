package com.example.diosoft_test_task.controllers;

import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.services.implementations.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;


    @GetMapping("contacts/getAll")
    public List<ContactEntity> getAllContactsWithoutPerson() {
        return contactService.getAll();
    }

    @GetMapping("contacts/get")
    public HttpEntity getByFullName(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String middleName) {
        return new ResponseEntity(contactService.getByFullname(firstName, lastName, middleName).get(), HttpStatus.ACCEPTED);
    }

    //Удаляет по ФИО person
    //{
    //   "firstName" : "sanya",
    //   "lastName" : "sanya",
    //  "middleName" : "sanya",
    // }
    @DeleteMapping("contacts/deleteByPerson")
    public HttpEntity deleteContactByPerson(@RequestBody PersonEntity personEntity) {
        return contactService.delete(personEntity);
    }

    //Обновить контакт
    @PatchMapping("contacts/unpdateContact")
    public HttpEntity updateContact(@RequestBody ContactEntity contactEntity) {
        return contactService.updateContact(contactEntity);
    }


    //Сохраняет контакт. Если указаны только ФИО то добавит к существующему , если такой найдется. Если указана Position и связка ФИО уникальная
    //создаст person и свяжет с ним контакт.
    //{
    //	"person" : {
    //		"firstName" : "sanya",
    //		"lastName" : "sanya",
    //		"middleName" : "sanya",
    //		"position" : "mid"
    //	},
    //	"number" : "48668715",
    //	"contactTypeEntity" : {
    //		"id" : 1,
    //		"type" : "дом"
    //	}
    //}
    @PostMapping("contacts/save")
    public HttpEntity saveByPerson(@RequestBody ContactEntity contactEntity) {
        try {
            return contactService.save(contactEntity);
        } catch (DataAccessException e) {
            return new ResponseEntity(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
