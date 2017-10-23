package com.epam.homework.books_db.model;

import java.time.LocalDate;
import java.util.Optional;

public class Author {

    private String name;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private Gender gender;

    public Author(String name, LocalDate dateOfBirth, Gender gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = null;
        this.gender = gender;
    }

    public Author(String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender) {
        this(name, dateOfBirth, gender);
        this.dateOfDeath = dateOfDeath;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDate> getDateOfDeath() {
        Optional<LocalDate> opt = Optional.ofNullable(dateOfDeath);
        return opt;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (!name.equals(author.name)) return false;
        if (!dateOfBirth.equals(author.dateOfBirth)) return false;
        if (dateOfDeath != null ? !dateOfDeath.equals(author.dateOfDeath) : author.dateOfDeath != null) return false;
        return gender == author.gender;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfDeath=" + dateOfDeath +
                ", gender=" + gender +
                '}';
    }
}