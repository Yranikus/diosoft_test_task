package com.example.diosoft_test_task;


import com.example.diosoft_test_task.builders.PersonBuilder;
import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.ContactTypeEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.repositories.ContactRepository;
import com.example.diosoft_test_task.repositories.PersonRepository;
import com.example.diosoft_test_task.services.implementations.ContactService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ContactServiceTests {

    @Autowired
    private ContactService contactService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ContactRepository contactRepository;

    @Value("#{propertiesfilemapping['validation_failed']}")
    private String failValidation;
    @Value("#{propertiesfilemapping['success']}")
    private String success;
    @Value("#{propertiesfilemapping['added_new']}")
    private String addedNew;
    @Value("#{propertiesfilemapping['linked']}")
    private String linkedWithPerson;
    private static PersonBuilder personBuilder = new PersonBuilder();
    private static ContactTypeEntity contactType;
    private static PersonEntity correctPerson1;
    private static PersonEntity correctPerson2;
    private static PersonEntity correctPerson3;
    private static PersonEntity incorrectPerson;
    private static ContactEntity correctContact;
    private static ContactEntity correctContact2;
    private static ContactEntity correctContact3;
    private static ContactEntity correctContact4;
    private static ContactEntity inorrectContact;


    @BeforeClass
    public static void createData(){
        contactType = new ContactTypeEntity();
        contactType.setId(1);
        //проходящие валидацию person
        correctPerson1 = personBuilder.build("Vanya", "Vasev", "Vasevich");
        correctPerson1.setPosition("mid");
        correctPerson2 = personBuilder.build("Vanya", "Petrov", "Vasevich");
        correctPerson2.setPosition("jun");
        correctPerson3 = personBuilder.build("Marat", "Vasev", "Vasevich");
        correctPerson3.setPosition("cleaner");
        //не проходящие валидацию person
        incorrectPerson = personBuilder.build("Vova///6a", "Vasev", "Vasevich");
        incorrectPerson.setPosition("mid");
        //Сущность для saveCorrectContactWithCorrectPersonTest теста с проходящим валидацию person и contact
        correctContact = new ContactEntity();
        correctContact.setNumber("5875497");
        correctContact.setPerson(correctPerson1);
        correctContact.setContactTypeEntity(contactType);

        //Сущность для saveIncorrectContactWithCorrectPersonTest с проходящим валидацию person и не проходящим валидацию к contact
        inorrectContact = new ContactEntity();
        inorrectContact.setNumber("dsf5555");
        inorrectContact.setContactTypeEntity(contactType);
        inorrectContact.setPerson(correctPerson1);

        //Сущность для saveCorrectContactWithInCorrectPersonTest с проходящим валидацию person и проходящим валидацию к contact
        correctContact2 = new ContactEntity();
        correctContact2.setContactTypeEntity(contactType);
        correctContact2.setPerson(incorrectPerson);
        //Сущность для saveCorrectContactToExsistPersonTest с проходящим валидацию person и проходящим валидацию к contact
        correctContact3 = new ContactEntity();
        correctContact3.setPerson(correctPerson3);
        correctContact3.setNumber("2894568566");
        correctContact3.setContactTypeEntity(contactType);
        //Сущность для updateContactTest с проходящим валидацию person и проходящим валидацию к contact
        correctContact4 = new ContactEntity();
        correctContact4.setPerson(correctPerson2);
        correctContact4.setNumber("28957674568");
        correctContact4.setContactTypeEntity(contactType);
    }

    @Before
    public void saveTestData(){
        contactRepository.save(correctContact4);
        personRepository.save(correctPerson3);
    }

    //Чистим базу после тестов
    @After
    public void clearDb(){
        personRepository.deleteAll();
    }

    //Сохраняем корректные данные
    @Test
    public void saveCorrectContactWithCorrectPersonTest(){
        Assert.assertEquals(addedNew,contactService.save(correctContact).getBody().toString());
        Optional<PersonEntity> savedPerson = personRepository.
                findByFirstNameAndLastNameAndMiddleName(correctContact.getPerson().getFirstName(),
                        correctContact.getPerson().getLastName(),
                        correctContact.getPerson().getMiddleName());
        Assert.assertTrue(savedPerson.isPresent());
        Optional<ContactEntity> savedContact = contactRepository.findByPersonId(savedPerson.get().getId());
        Assert.assertEquals(savedPerson.get().getId(),savedContact.get().getPerson().getId());
    }

    //Проверям что работает валидаци для контакта
    @Test
    public void saveIncorrectContactWithCorrectPersonTest(){
        Assert.assertEquals(failValidation,contactService.save(inorrectContact).getBody().toString());
    }

    //Проверям что работает валидаци для person
    @Test
    public void saveCorrectContactWithInCorrectPersonTest(){
        Assert.assertEquals(failValidation,contactService.save(correctContact2).getBody().toString());
    }

    //Добавялем контакт к person , которого добавили в аннотации Before без контакта.
    @Test
    public void saveCorrectContactToExsistPersonTest(){
        Assert.assertEquals(linkedWithPerson,contactService.save(correctContact3).getBody().toString());
    }

    //Обновляем контакт , добавленный в аннотации Before
    @Test
    public void updateContactTest(){
        Assert.assertEquals(success,contactService.updateContact(correctContact4).getBody().toString());
    }

    //В аннотации Before добляется 1 контакт , поэтому метод должен вернуть не пустой лист
    @Test
    public void getAllContactTest(){
        Assert.assertTrue(contactService.getAll().size() > 0);
    }

    //Удаляет только контакт. Person остается.
    @Test
    public void deleteOnlyContactTest(){
        Assert.assertEquals(success,contactService.delete(correctPerson2).getBody().toString());
        Optional<PersonEntity> person = personRepository.findByFirstNameAndLastNameAndMiddleName(correctPerson2.getFirstName(),
                correctPerson2.getLastName(),
                correctPerson2.getMiddleName());
        Assert.assertTrue(person.isPresent());
        Assert.assertNull(person.get().getContactEntity());
    }

    @Test
    public void getByFullNameTest(){
        Optional<ContactEntity> gettedContact = contactService.getByFullname(correctPerson2.getFirstName(),
                correctPerson2.getLastName(),
                correctPerson2.getMiddleName());
        Assert.assertTrue(gettedContact.isPresent());
        Assert.assertEquals(correctContact4.getNumber(),gettedContact.get().getNumber());
        Assert.assertEquals(correctContact4.getPerson().getFirstName(),gettedContact.get().getPerson().getFirstName());
    }

}
