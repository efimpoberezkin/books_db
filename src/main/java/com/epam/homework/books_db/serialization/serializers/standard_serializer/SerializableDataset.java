package com.epam.homework.books_db.serialization.serializers.standard_serializer;

import com.epam.homework.books_db.serialization.entity_model.AuthorEntity;
import com.epam.homework.books_db.serialization.entity_model.BookEntity;
import com.epam.homework.books_db.serialization.entity_model.PublisherEntity;

import java.io.Serializable;
import java.util.List;

class SerializableDataset implements Serializable {

    private List<AuthorEntity> authors;
    private List<BookEntity> books;
    private List<PublisherEntity> publishers;

    SerializableDataset(List<AuthorEntity> authors, List<BookEntity> books, List<PublisherEntity> publishers) {
        this.authors = authors;
        this.books = books;
        this.publishers = publishers;
    }

    List<AuthorEntity> getAuthors() {
        return authors;
    }

    List<BookEntity> getBooks() {
        return books;
    }

    List<PublisherEntity> getPublishers() {
        return publishers;
    }
}
