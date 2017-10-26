package com.epam.homework.books_db.dataset;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.util.List;

public class Dataset {

    private List<Author> authors;
    private List<Book> books;
    private List<Publisher> publishers;

    public Dataset(List<Author> authors, List<Book> books, List<Publisher> publishers) {
        this.authors = authors;
        this.books = books;
        this.publishers = publishers;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dataset dataset = (Dataset) o;

        if (!(authors.size() == dataset.authors.size() && authors.containsAll(dataset.authors))) return false;
        if (!(books.size() == dataset.books.size() && books.containsAll(dataset.books))) return false;
        return publishers.size() == dataset.publishers.size() && publishers.containsAll(dataset.publishers);
    }

    @Override
    public int hashCode() {
        int result = authors.hashCode();
        result = 31 * result + books.hashCode();
        result = 31 * result + publishers.hashCode();
        return result;
    }
}
