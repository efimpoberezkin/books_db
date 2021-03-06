package com.epam.homework.books_db.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Publisher {

    private String name;
    private List<Book> books;

    public Publisher(String name, Book... books) {
        this.name = name;
        this.books = new ArrayList<>(Arrays.asList(books));
    }

    public String getName() {
        return name;
    }

    public List<Book> getPublishedBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher publisher = (Publisher) o;

        if (getName() != null ? !getName().equals(publisher.getName()) : publisher.getName() != null) return false;
        return getPublishedBooks() != null
                ? (getPublishedBooks().size() == publisher.getPublishedBooks().size() && getPublishedBooks().containsAll(publisher.getPublishedBooks()))
                : publisher.getPublishedBooks() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (books != null ? books.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
