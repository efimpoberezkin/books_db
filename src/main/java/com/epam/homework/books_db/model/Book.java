package com.epam.homework.books_db.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book {

    private String name;
    private Year yearOfPublication;
    private List<Author> authors;

    public Book(String name, Year yearOfPublication, Author... authors) {
        this.name = name;
        this.yearOfPublication = yearOfPublication;
        this.authors = new ArrayList<>(Arrays.asList(authors));
    }

    public String getName() {
        return name;
    }

    public Year getYearOfPublication() {
        return yearOfPublication;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (getName() != null ? !getName().equals(book.getName()) : book.getName() != null) return false;
        if (getYearOfPublication() != null ? !getYearOfPublication().equals(book.getYearOfPublication()) : book.getYearOfPublication() != null)
            return false;
        return getAuthors() != null
                ? (getAuthors().size() == book.getAuthors().size() && getAuthors().containsAll(book.getAuthors()))
                : book.getAuthors() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getYearOfPublication() != null ? getYearOfPublication().hashCode() : 0);
        result = 31 * result + (getAuthors() != null ? getAuthors().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", authors=" + authors +
                '}';
    }
}