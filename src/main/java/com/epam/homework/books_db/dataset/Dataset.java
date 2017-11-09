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

        if (getAuthors() != null
                ? !(authors.size() == dataset.authors.size() && authors.containsAll(dataset.authors))
                : dataset.getAuthors() != null)
            return false;
        if (getBooks() != null
                ? !(books.size() == dataset.books.size() && books.containsAll(dataset.books))
                : dataset.getBooks() != null)
            return false;
        return getPublishers() != null
                ? (publishers.size() == dataset.publishers.size() && publishers.containsAll(dataset.publishers))
                : dataset.getPublishers() == null;
    }

    @Override
    public int hashCode() {
        int result = getAuthors() != null ? getAuthors().hashCode() : 0;
        result = 31 * result + (getBooks() != null ? getBooks().hashCode() : 0);
        result = 31 * result + (getPublishers() != null ? getPublishers().hashCode() : 0);
        return result;
    }
}
