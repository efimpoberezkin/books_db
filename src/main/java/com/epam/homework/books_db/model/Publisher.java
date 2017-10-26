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

        if (!name.equals(publisher.name)) return false;
        return books.size() == publisher.books.size() && books.containsAll(publisher.books);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + books.hashCode();
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
