package com.example.diosoft_test_task.controllers;


import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.services.implementations.PersontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersontService persontService;


    //Отдает всех person без контактов
    @GetMapping("person/getall")
    public List<PersonEntity> getAll() {
        return persontService.getAll();
    }

    //Можно найти по имени
    //Принимает
    //{
    //   "firstName" : "Sasha",
    //   "lastName" : "Sashev",
    //  "middleName" : "Sashevich",
    //  "position" : "mid" //можно и без позиции
    // },
    @GetMapping("person/get")
    public HttpEntity getByFullName(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String middleName) {
        return new ResponseEntity(persontService.getByFullname(firstName, lastName, middleName).get(), HttpStatus.ACCEPTED);
    }

    //Удалит person и все его контакты.
    //Принимает
    //{
    //   "firstName" : "Sasha",
    //   "lastName" : "Sashev",
    //  "middleName" : "Sashevich",
    //  "position" : "mid" //можно и без позиции
    // },
    @DeleteMapping("person/delete")
    public HttpEntity delete(@RequestBody PersonEntity personEntity) {
        return persontService.delete(personEntity);
    }


    //Сохранит person без контактов.
    //Принимает
    //{
    //   "firstName" : "Sasha",
    //   "lastName" : "Sashev",
    //  "middleName" : "Sashevich",
    //  "position" : "mid"
    // },
    @PostMapping("person/saveOnlyPerson")
    public HttpEntity saveWithoutContacts(@RequestBody PersonEntity personEntity) {
        try {
            return persontService.save(personEntity);
        } catch (DataAccessException e) {
            return new ResponseEntity(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
