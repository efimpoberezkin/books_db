package com.epam.homework.books_db.serialization.entity_model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublisherEntity implements Serializable {

    private String name;
    private List<BookEntity> books;

    public PublisherEntity(String name, List<BookEntity> books) {
        this.name = name;
        this.books = new ArrayList<>(books);
    }

    public String getName() {
        return name;
    }

    public List<BookEntity> getPublishedBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublisherEntity publisher = (PublisherEntity) o;

        if (!name.equals(publisher.name)) return false;
        return books.size() == publisher.books.size() && books.containsAll(publisher.books);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + books.hashCode();
        return result;
    }
}
