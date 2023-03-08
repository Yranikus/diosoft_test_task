package com.example.diosoft_test_task.services.implementations;

import com.example.diosoft_test_task.builders.PersonBuilder;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.repositories.PersonRepository;
import com.example.diosoft_test_task.services.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class PersontService implements DataService<PersonEntity> {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DataValidationService dataValidationService;
    @Autowired
    private PersonBuilder personBuilder;

    @Value("#{propertiesfilemapping['validation_failed']}")
    private String fail;
    @Value("#{propertiesfilemapping['success']}")
    private String success;


    //Выдаст всех person без контактов
    @Override
    public List<PersonEntity> getAll() {
        return personRepository.findAll();
    }

    //Удаление. Удалит person и его контакт
    @Override
    @Transactional
    public HttpEntity delete(PersonEntity entity) {
        if (dataValidationService.validateFullName(entity)) {
            personRepository.deleteByFirstNameAndLastNameAndMiddleName(entity.getFirstName(), entity.getLastName(), entity.getMiddleName());
            return new ResponseEntity(success, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(fail, HttpStatus.BAD_REQUEST);
    }

    //Получение по имени
    public Optional getByFullname(String firstName, String lastName, String middleName) {
        if (dataValidationService.validateFullName(personBuilder.build(firstName, lastName, middleName))) {
            return personRepository.findByFirstNameAndLastNameAndMiddleName(firstName,
                    lastName,
                    middleName);
        }
        return Optional.of(fail);
    }

    //Сохранит person без контакта
    @Override
    @Transactional
    public HttpEntity save(PersonEntity entity) throws DataAccessException {
        if (dataValidationService.validateFullName(entity)) {
            if (entity.getPosition() != null) {
                personRepository.save(entity);
                return new ResponseEntity(success, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity(fail, HttpStatus.BAD_REQUEST);
    }


}
