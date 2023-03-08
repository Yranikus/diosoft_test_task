package com.example.diosoft_test_task.services.implementations;

import com.example.diosoft_test_task.builders.ContactBuilder;
import com.example.diosoft_test_task.builders.PersonBuilder;
import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.repositories.ContactRepository;
import com.example.diosoft_test_task.repositories.PersonRepository;
import com.example.diosoft_test_task.services.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService implements DataService<ContactEntity> {

    @Autowired
    private ContactBuilder contactBuilder;
    @Autowired
    private PersonBuilder personBuilder;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DataValidationService dataValidationService;

    @Value("#{propertiesfilemapping['validation_failed']}")
    private String fail;
    @Value("#{propertiesfilemapping['position_missing']}")
    private String positionMissing;
    @Value("#{propertiesfilemapping['success']}")
    private String success;
    @Value("#{propertiesfilemapping['added_new']}")
    private String added_new_person;
    @Value("#{propertiesfilemapping['linked']}")
    private String linked_with_exist;


    //Выдаст все контакты и их person.
    @Override
    public List<ContactEntity> getAll() {
        return contactRepository.findAll();
    }

    @Transactional
    public HttpEntity updateContact(ContactEntity contactEntity) {
        if (dataValidationService.validateFullName(contactEntity.getPerson())
                && dataValidationService.validateContactNumber(contactEntity)) {
            Optional<ContactEntity> finded = contactRepository
                    .findByPersonFirstNameAndPersonLastNameAndPersonMiddleName(
                            contactEntity.getPerson().getFirstName(),
                            contactEntity.getPerson().getLastName(),
                            contactEntity.getPerson().getMiddleName());
            finded.ifPresent(e -> {
                System.out.println(e.getId());
                e.setNumber(contactEntity.getNumber());
                contactRepository.save(e);
            });
            return new ResponseEntity(success, HttpStatus.ACCEPTED);
        } else return new ResponseEntity(fail, HttpStatus.BAD_REQUEST);
    }


    //Удалит только контакт
    @Transactional
    @Override
    public HttpEntity delete(PersonEntity entity) {
        if (dataValidationService.validateFullName(entity)) {
            personRepository.findByFirstNameAndLastNameAndMiddleName(entity.getFirstName(),
                    entity.getLastName(),
                    entity.getMiddleName()).ifPresent(personEntity -> contactRepository.removeByPersonId(personEntity.getId()));
            return new ResponseEntity(success, HttpStatus.ACCEPTED);
        } else return new ResponseEntity(fail, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public HttpEntity save(ContactEntity entity) {
        if (dataValidationService.validateFullName(entity.getPerson())
                && dataValidationService.validateContactNumber(entity)) {
            //Проверяет создан ли такой person уже
            Optional<PersonEntity> personEntity = personRepository.findByFirstNameAndLastNameAndMiddleName(entity.getPerson().getFirstName(),
                    entity.getPerson().getLastName(),
                    entity.getPerson().getMiddleName());
            if (personEntity.isPresent()) {
                //Если создан прикрипляем ссылку на объект , который был возвращен к контакту и хибер создает контакт привязанный к существующему person
                contactRepository.save(contactBuilder.build(personEntity.get(), entity));
                return new ResponseEntity(linked_with_exist, HttpStatus.ACCEPTED);
            } else {
                //Если не создан то надо создать, при условии что все данные указаны (position конкретно)
                if (entity.getPerson().getPosition() == null)
                    return new ResponseEntity(positionMissing, HttpStatus.BAD_REQUEST);
                //Создает из исходного класса и контакт и person
                contactRepository.save(entity);
                return new ResponseEntity(added_new_person, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity(fail, HttpStatus.ACCEPTED);
    }

    @Override
    public Optional getByFullname(String firstName, String lastName, String middleName) {
        if (dataValidationService.validateFullName(personBuilder.build(firstName, lastName, middleName))) {
            return contactRepository.findByPersonFirstNameAndPersonLastNameAndPersonMiddleName(firstName,
                    lastName,
                    middleName);
        }
        return Optional.of(fail);
    }


}
