package com.epam.homework.books_db.serialization.entity_model;

import com.epam.homework.books_db.model.Gender;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

public class AuthorEntity implements Serializable {

    private String name;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private Gender gender;

    public AuthorEntity(String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public Gender getGender() {
        return gender;
    }
}
