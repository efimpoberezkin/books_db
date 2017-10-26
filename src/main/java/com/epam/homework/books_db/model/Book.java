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

        if (!name.equals(book.name)) return false;
        if (!yearOfPublication.equals(book.yearOfPublication)) return false;
        return authors.size() == book.authors.size() && authors.containsAll(book.authors);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + yearOfPublication.hashCode();
        result = 31 * result + authors.hashCode();
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