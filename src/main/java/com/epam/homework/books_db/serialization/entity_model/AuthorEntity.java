package com.epam.homework.books_db.serialization.entity_model;

import com.epam.homework.books_db.model.Gender;

import java.io.Serializable;
import java.time.LocalDate;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorEntity author = (AuthorEntity) o;

        if (!name.equals(author.name)) return false;
        if (!dateOfBirth.equals(author.dateOfBirth)) return false;
        if (dateOfDeath != null ? !dateOfDeath.equals(author.dateOfDeath) : author.dateOfDeath != null) return false;
        return gender == author.gender;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + (dateOfDeath != null ? dateOfDeath.hashCode() : 0);
        result = 31 * result + gender.hashCode();
        return result;
    }
}
