package com.example.diosoft_test_task;

import com.example.diosoft_test_task.builders.PersonBuilder;
import com.example.diosoft_test_task.entities.ContactEntity;
import com.example.diosoft_test_task.entities.ContactTypeEntity;
import com.example.diosoft_test_task.entities.PersonEntity;
import com.example.diosoft_test_task.services.implementations.DataValidationService;
import com.example.diosoft_test_task.services.interfaces.ValidationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class ValidationServiceTests {

    private ValidationService validationService = new DataValidationService();
    private PersonBuilder personBuilder = new PersonBuilder();


    //Проверяет строку на наличие не буквенных символов
    @Test
    public void stringWithoutNubersTest() {
        PersonEntity test1 = personBuilder.build("555 fd 0", "555j", "dffe\\rgr");
        PersonEntity test2 = personBuilder.build("Fedya", "gg55fdg5", "dffe --gr");
        PersonEntity test4 = personBuilder.build("Jora", "Andreich", "Ololoev");
        Assert.assertFalse(validationService.validateFullName(test1));
        Assert.assertFalse(validationService.validateFullName(test2));
        Assert.assertTrue(validationService.validateFullName(test4));

    }

    //Проверяет что бы не было null и превышения длины
    @Test
    public void stringLenhgtAndNullTest(){
        PersonEntity test1 = personBuilder.build("Jora", "Andreich", "Ololoev");
        PersonEntity test3 = personBuilder.build("Jora", null, "Ololoev");
        PersonEntity test2 = personBuilder.build("Jo", "And", "Ololoevfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        Assert.assertTrue(validationService.validateFullName(test1));
        Assert.assertFalse(validationService.validateFullName(test2));
        Assert.assertFalse(validationService.validateFullName(test3));
    }

    //Проверяет что бы в номере были только цифры
    @Test
    public void onlyDigitInNumberTest(){
        ContactEntity test1 = new ContactEntity();
        test1.setNumber("1654866");
        test1.setContactTypeEntity(new ContactTypeEntity());
        ContactEntity test2 = new ContactEntity();
        test2.setNumber("1654fdg866");
        test2.setContactTypeEntity(new ContactTypeEntity());
        ContactEntity test3 = new ContactEntity();
        test3.setNumber("16548---/66");
        test3.setContactTypeEntity(new ContactTypeEntity());
        Assert.assertTrue(validationService.validateContactNumber(test1));
        Assert.assertFalse(validationService.validateContactNumber(test2));
        Assert.assertFalse(validationService.validateContactNumber(test3));
    }

    //Проверяет длину номера и null
    @Test
    public void lenghtAndNulltest(){
        ContactEntity test1 = new ContactEntity();
        test1.setNumber("1654866");
        test1.setContactTypeEntity(new ContactTypeEntity());
        ContactEntity test2 = new ContactEntity();
        test2.setNumber("1654fdg866");
        test2.setContactTypeEntity(new ContactTypeEntity());
        ContactEntity test3 = new ContactEntity();
        test3.setContactTypeEntity(new ContactTypeEntity());
        test3.setNumber("16548---/66");
        Assert.assertTrue(validationService.validateContactNumber(test1));
        Assert.assertFalse(validationService.validateContactNumber(test2));
        Assert.assertFalse(validationService.validateContactNumber(test3));
        Assert.assertFalse(validationService.validateContactNumber(new ContactEntity()));
    }




}
