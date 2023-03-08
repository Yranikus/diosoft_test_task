package com.example.diosoft_test_task.repositories;

import com.example.diosoft_test_task.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByFirstNameAndLastNameAndMiddleName(String firstName, String lastName, String middleName);
    void deleteByFirstNameAndLastNameAndMiddleName(String firstName, String lastName, String middleName);
}
