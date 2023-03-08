package com.example.diosoft_test_task;

import com.example.diosoft_test_task.builders.PersonBuilder;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.repositories.PersonRepository;
import com.example.diosoft_test_task.services.implementations.PersontService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonServiceTests {

    @Autowired
    private static PersonBuilder personBuilder = new PersonBuilder();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersontService persontService;
    private static PersonEntity correctPerson1;
    private static PersonEntity correctPerson2;
    private static PersonEntity incorrectPerson1;
    private static PersonEntity incorrectPerson2;
    private static PersonEntity incorrectPerson3;


    @Value("#{propertiesfilemapping['validation_failed']}")
    private String fail;
    @Value("#{propertiesfilemapping['success']}")
    private String success;

    //Создаем тестовые данные
    @BeforeClass
    public static void createPersons() {
        correctPerson1 = personBuilder.build("Vasya", "Vasev", "Vasevich");
        correctPerson1.setPosition("mid");
        correctPerson2 = personBuilder.build("Vanya", "Petrov", "Vasevich");
        correctPerson2.setPosition("jun");
        incorrectPerson1 = personBuilder.build("Vovaa", "Vasev", "Vasevich");
        incorrectPerson1.setPosition(null);
        incorrectPerson2 = personBuilder.build("Vas5646ya", "Vasev", "Vasevich");
        incorrectPerson2.setPosition("senior pomidor");
        incorrectPerson3 = personBuilder.build(null, "Vasev", "Vasevich");
        incorrectPerson3.setPosition("cleaner");
    }


    @Before
    public void savePersonsForTests(){
        personRepository.save(correctPerson1);
        personRepository.save(correctPerson2);
    }

    @After
    public void clearDb(){
        personRepository.deleteAll();
    }

    //Тестируем что сервис не сохранит person при наличии полей null или некоректных полей.
    @Test
    public void nullPositionTest() {
        Assert.assertEquals(persontService.save(incorrectPerson1).getBody().toString(), fail);
        Assert.assertEquals(persontService.save(incorrectPerson3).getBody().toString(), fail);
        Assert.assertEquals(persontService.save(incorrectPerson2).getBody().toString(), fail);
        Assert.assertTrue(personRepository.
                findByFirstNameAndLastNameAndMiddleName(incorrectPerson1.getFirstName(),
                        incorrectPerson1.getLastName(),
                        incorrectPerson1.getMiddleName()).isEmpty());
    }

    //Тестируем что при поптыке сохранить дубликат вылетит ошибка
    @Test
    public void noDublicateTest() {
        try {
            persontService.save(correctPerson1);
            Assert.assertTrue(false);
        }
        catch (DataAccessException e){
            Assert.assertTrue(true);
        }
    }

    //Проверяем что сущности с коректными данными сохраняются. Они же будут использоваться в слудующих тестах.
    @Test
    public void saveTest() {
        Assert.assertEquals(persontService.save(correctPerson2).getBody().toString(), success);
        PersonEntity savedPerson = personRepository.findByFirstNameAndLastNameAndMiddleName(correctPerson2.getFirstName(),
                correctPerson2.getLastName(),
                correctPerson2.getMiddleName()
        ).get();
        Assert.assertNotNull(savedPerson);
        Assert.assertEquals(savedPerson.getFirstName(), correctPerson2.getFirstName());
        Assert.assertEquals(savedPerson.getLastName(), correctPerson2.getLastName());
        Assert.assertEquals(savedPerson.getMiddleName(), correctPerson2.getMiddleName());
    }


    //Проверяем что сохранная в тесте 3 сущность находится в базе данных.
    @Test
    public void getByFullNameTest(){
        Assert.assertFalse(persontService.getByFullname(correctPerson1.getFirstName(),
                correctPerson1.getLastName(), correctPerson1.getMiddleName()).isEmpty());
    }


    //Проверяем то что сущность удаляется
    @Test
    public void deleteTest() {
        PersonEntity savedPerson = personRepository.findByFirstNameAndLastNameAndMiddleName(correctPerson1.getFirstName(),
                correctPerson1.getLastName(),
                correctPerson1.getMiddleName()
        ).get();
        Assert.assertNotNull(savedPerson);
        Assert.assertEquals(savedPerson.getFirstName(), correctPerson1.getFirstName());
        Assert.assertEquals(savedPerson.getLastName(), correctPerson1.getLastName());
        Assert.assertEquals(savedPerson.getMiddleName(), correctPerson1.getMiddleName());
        Assert.assertEquals(persontService.delete(correctPerson1).getBody().toString(), success);
        Assert.assertTrue(personRepository.findByFirstNameAndLastNameAndMiddleName(correctPerson1.getFirstName(),
                correctPerson1.getLastName(),
                correctPerson1.getMiddleName()).isEmpty());
    }

    @Test
    public void getAllTest(){
        Assert.assertTrue(persontService.getAll().size() > 0);
    }



}



