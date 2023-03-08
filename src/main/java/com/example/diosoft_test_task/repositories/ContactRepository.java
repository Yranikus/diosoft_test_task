package com.example.diosoft_test_task.repositories;

import com.example.diosoft_test_task.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    Integer removeByPersonId(Long id);
    Optional<ContactEntity> findByPersonId(Long id);
    Optional<ContactEntity> findByPersonFirstNameAndPersonLastNameAndPersonMiddleName(String firstName, String lastName, String middleName);

}
