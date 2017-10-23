package com.epam.homework.books_db.serialization.entity_model;

import com.epam.homework.books_db.model.Book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
}
