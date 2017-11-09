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
        return Optional.ofNullable(dateOfDeath);
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (getName() != null ? !getName().equals(author.getName()) : author.getName() != null) return false;
        if (getDateOfBirth() != null ? !getDateOfBirth().equals(author.getDateOfBirth()) : author.getDateOfBirth() != null)
            return false;
        if (getDateOfDeath() != null ? !getDateOfDeath().equals(author.getDateOfDeath()) : author.getDateOfDeath() != null)
            return false;
        return getGender() == author.getGender();
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getDateOfBirth() != null ? getDateOfBirth().hashCode() : 0);
        result = 31 * result + (getDateOfDeath() != null ? getDateOfDeath().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        return result;
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