package com.example.diosoft_test_task.entities;



import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "contact_type")
public class ContactTypeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type", length = 255, nullable = false,unique = true)
    private String type;


    public ContactTypeEntity() {
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
