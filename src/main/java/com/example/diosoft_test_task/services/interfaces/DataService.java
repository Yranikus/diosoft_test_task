package com.example.diosoft_test_task.services.interfaces;

import com.example.diosoft_test_task.entities.PersonEntity;
import org.springframework.http.HttpEntity;


import java.util.List;
import java.util.Optional;

public interface DataService<E> {

    //Получить можно отдельно список person без контактов , можно целиком все таблицы
    List<E> getAll();
    //По скольку я допустил возможность отсутсвие у person контакта удаление происходит только по айдишникам person.
    HttpEntity delete(PersonEntity entity) ;
    //Сохрнаить можно контакт с person , а можно только person
    HttpEntity save(E entity) ;
    //Получаем сущность по иммени
    Optional getByFullname(String firstName, String lastName, String middleName);


}
