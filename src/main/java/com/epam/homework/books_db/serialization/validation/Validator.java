package com.epam.homework.books_db.serialization.validation;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    private List<Author> validatedAuthors;
    private List<Book> validatedBooks;

    public void validateDataset(Dataset dataset) throws ValidationException {
        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        validatedAuthors = new ArrayList<>();
        validatedBooks = new ArrayList<>();

        authors.forEach(this::validateAuthor);
        books.forEach(this::validateBook);
        publishers.forEach(this::validatePublisher);
    }

    private void validateAuthor(Author author) throws ValidationException {
        if ("".equals(author.getName())) {
            throw new ValidationException("Author has no name");
        }

        if (author.getDateOfDeath().isPresent()) {
            if (!author.getDateOfDeath().get().isBefore(LocalDate.now())) {
                throw new ValidationException("Author " + author.getName()
                        + " - date of death has to be prior to the current date");
            }

            if (!author.getDateOfBirth().isBefore(author.getDateOfDeath().get())) {
                throw new ValidationException("Author " + author.getName()
                        + " - date of birth has to be prior to the date of death");
            }
        }

        if (!author.getDateOfBirth().isBefore(LocalDate.now())) {
            throw new ValidationException("Author " + author.getName()
                    + " - date of birth has to be prior to the current date");
        }

        validatedAuthors.add(author);
    }

    private void validateBook(Book book) throws ValidationException {
        if ("".equals(book.getName())) {
            throw new ValidationException("Book has no name");
        }

        if (book.getYearOfPublication().isAfter(Year.now())) {
            throw new ValidationException("Book " + book.getName()
                    + " - publication year cannot be greater than the current year");
        }

        if (book.getAuthors().isEmpty()) {
            throw new ValidationException("Book " + book.getName()
                    + " - book has no authors");
        }

        for (Author author : book.getAuthors()) {
            if (!(author.getDateOfBirth().getYear() <= book.getYearOfPublication().getValue())) {
                throw new ValidationException("Book " + book.getName() + " - year of birth of author "
                        + author.getName() + " cannot be greater than the year of the book's publication");
            }

            if (!validatedAuthors.contains(author)) {
                validateAuthor(author);
            }
        }

        validatedBooks.add(book);
    }

    private void validatePublisher(Publisher publisher) throws ValidationException {
        if ("".equals(publisher.getName())) {
            throw new ValidationException("Publisher has no name");
        }

        for (Book book : publisher.getPublishedBooks()) {
            if (!validatedBooks.contains(book)) {
                validateBook(book);
            }
        }
    }
}
