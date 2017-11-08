package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseService {

    public void writeDataset(Dataset dataset) {
        List<Publisher> publishers = dataset.getPublishers();
        List<Book> books = dataset.getBooks();
        List<Author> authors = dataset.getAuthors();

        Set<Book> savedBooks = new HashSet<>();
        Set<Author> savedAuthors = new HashSet<>();

        PublishersDAO publishersDAO = new PublishersDAO();
        BooksDAO booksDAO = new BooksDAO();
        AuthorsDAO authorsDAO = new AuthorsDAO();

        for (Publisher publisher : publishers) {
            publishersDAO.add(publisher);

            List<Book> publisherBooks = publisher.getPublishedBooks();
            savedBooks.addAll(publisherBooks);
            publisherBooks.forEach(book -> savedAuthors.addAll(book.getAuthors()));
        }

        for (Book book : books) {
            if (!savedBooks.contains(book)) {
                booksDAO.add(book);
                savedAuthors.addAll(book.getAuthors());
            }
        }

        for (Author author : authors) {
            if (!savedAuthors.contains(author)) {
                authorsDAO.add(author);
            }
        }
    }

    public Dataset readDataset() {
        List<Author> authors = new AuthorsDAO().getAll();
        List<Book> books = new BooksDAO().getAll();
        List<Publisher> publishers = new PublishersDAO().getAll();

        return new Dataset(authors, books, publishers);
    }
}
