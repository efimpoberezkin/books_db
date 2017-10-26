package com.epam.homework.books_db.serialization.entity_model;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BookEntity implements Serializable {

    private String name;
    private Year yearOfPublication;
    private List<AuthorEntity> authors;

    public BookEntity(String name, Year yearOfPublication, List <AuthorEntity> authors) {
        this.name = name;
        this.yearOfPublication = yearOfPublication;
        this.authors = new ArrayList<>(authors);
    }

    public String getName() {
        return name;
    }

    public Year getYearOfPublication() {
        return yearOfPublication;
    }

    public List<AuthorEntity> getAuthors() {
        return authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookEntity book = (BookEntity) o;

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
}
