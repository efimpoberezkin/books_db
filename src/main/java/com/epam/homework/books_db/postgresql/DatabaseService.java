package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseService {

    /**
     * @return generated id
     */
    public int writeAuthor(Author author) throws DatabaseServiceException {
        try {
            return new AuthorsDAOImpl().add(author);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write author", e);
        }
    }

    /**
     * @return list of generated ids
     */
    public List<Integer> writeAllAuthors(List<Author> authors) throws DatabaseServiceException {
        try {
            return new AuthorsDAOImpl().addAll(authors);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write authors", e);
        }
    }

    public Author readAuthor(int id) throws DatabaseServiceException {
        try {
            return new AuthorsDAOImpl().get(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read author", e);
        }
    }

    public List<Author> readAllAuthors() throws DatabaseServiceException {
        try {
            return new AuthorsDAOImpl().getAll();
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read authors", e);
        }
    }

    public void updateAuthor(int id, String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender)
            throws DatabaseServiceException {
        try {
            new AuthorsDAOImpl().update(id, name, dateOfBirth, dateOfDeath, gender);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to update author", e);
        }
    }

    public void deleteAuthor(int id) throws DatabaseServiceException {
        try {
            new AuthorsDAOImpl().delete(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to delete author", e);
        }
    }

    /**
     * @return generated id
     */
    public int writeBook(Book book) throws DatabaseServiceException {
        try {
            return new BooksDAOImpl().add(book);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write book", e);
        }
    }

    /**
     * @return list of generated ids
     */
    public List<Integer> writeAllBooks(List<Book> books) throws DatabaseServiceException {
        try {
            return new BooksDAOImpl().addAll(books);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write books", e);
        }
    }

    public Book readBook(int id) throws DatabaseServiceException {
        try {
            return new BooksDAOImpl().get(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read book", e);
        }
    }

    public List<Book> readAllBooks() throws DatabaseServiceException {
        try {
            return new BooksDAOImpl().getAll();
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read books", e);
        }
    }

    public void updateBook(int id, String name, Year yearOfPublication) throws DatabaseServiceException {
        try {
            new BooksDAOImpl().update(id, name, yearOfPublication);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to update book", e);
        }
    }

    public void deleteBook(int id) throws DatabaseServiceException {
        try {
            new BooksDAOImpl().delete(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to delete book", e);
        }
    }

    /**
     * @return generated id
     */
    public int writePublisher(Publisher publisher) throws DatabaseServiceException {
        try {
            return new PublishersDAOImpl().add(publisher);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write publisher", e);
        }
    }

    /**
     * @return list of generated ids
     */
    public List<Integer> writeAllPublishers(List<Publisher> publishers) throws DatabaseServiceException {
        try {
            return new PublishersDAOImpl().addAll(publishers);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to write publishers", e);
        }
    }

    public Publisher readPublisher(int id) throws DatabaseServiceException {
        try {
            return new PublishersDAOImpl().get(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read publisher", e);
        }
    }

    public List<Publisher> readAllPublishers() throws DatabaseServiceException {
        try {
            return new PublishersDAOImpl().getAll();
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to read publishers", e);
        }
    }

    public void updatePublisher(int id, String name) throws DatabaseServiceException {
        try {
            new PublishersDAOImpl().update(id, name);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to update publisher", e);
        }
    }

    public void deletePublisher(int id) throws DatabaseServiceException {
        try {
            new PublishersDAOImpl().delete(id);
        } catch (DAOException e) {
            throw new DatabaseServiceException("Database service failed to delete publisher", e);
        }
    }

    public void writeDataset(Dataset dataset) throws DatabaseServiceException {
        List<Publisher> publishers = dataset.getPublishers();
        List<Book> books = dataset.getBooks();
        List<Author> authors = dataset.getAuthors();

        Set<Book> savedBooks = new HashSet<>();
        Set<Author> savedAuthors = new HashSet<>();

        DAO<Publisher> publishersDAO = new PublishersDAOImpl();
        DAO<Book> booksDAO = new BooksDAOImpl();
        DAO<Author> authorsDAO = new AuthorsDAOImpl();

        for (Publisher publisher : publishers) {
            writePublisher(publisher);

            List<Book> publisherBooks = publisher.getPublishedBooks();
            savedBooks.addAll(publisherBooks);
            publisherBooks.forEach(book -> savedAuthors.addAll(book.getAuthors()));
        }

        for (Book book : books) {
            if (!savedBooks.contains(book)) {
                writeBook(book);
                savedAuthors.addAll(book.getAuthors());
            }
        }

        for (Author author : authors) {
            if (!savedAuthors.contains(author)) {
                writeAuthor(author);
            }
        }
    }

    public Dataset readDataset() throws DatabaseServiceException {
        List<Author> authors = readAllAuthors();
        List<Book> books = readAllBooks();
        List<Publisher> publishers = readAllPublishers();

        return new Dataset(authors, books, publishers);
    }
}
