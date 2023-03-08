package com.example.diosoft_test_task.entities;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "contacts")
public class ContactEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number", length = 20, nullable = false)
    private String number;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true, nullable = false)
    private PersonEntity person;
    @ManyToOne
    @JoinColumn(name = "contact_type_id", referencedColumnName = "id", nullable = false)
    private ContactTypeEntity contactTypeEntity;


    public ContactEntity() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactTypeEntity getContactTypeEntity() {
        return contactTypeEntity;
    }

    public void setContactTypeEntity(ContactTypeEntity contactTypeEntity) {
        this.contactTypeEntity = contactTypeEntity;
    }
}
