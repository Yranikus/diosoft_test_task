package com.example.diosoft_test_task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "person")
//Ограничение уникальности , что бы не было дубликатов person
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name","last_name","middle_name"})})
public class PersonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", length = 80, nullable = false)
    private String firstName;
    @Column(name = "last_Name", length = 80, nullable = false)
    private String lastName;
    @Column(name = "middle_name", length = 80, nullable = false)
    private String middleName;
    @Column(name = "position", length = 255, nullable = false)
    private String position;
    @OneToOne(mappedBy = "person", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private ContactEntity contactEntity;


    public PersonEntity() {
    }

    public ContactEntity getContactEntity() {
        return contactEntity;
    }

    public void setContactEntity(ContactEntity contactEntity) {
        this.contactEntity = contactEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position_name) {
        this.position = position_name;
    }
}
