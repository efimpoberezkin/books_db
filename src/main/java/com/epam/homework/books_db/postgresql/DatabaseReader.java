package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

class DatabaseReader {

    private Map<Integer, Author> authorsMap;
    private Map<Integer, Book> booksMap;
    private Map<Integer, Publisher> publishersMap;

    private final Connection conn;

    DatabaseReader(Connection conn) {
        this.conn = conn;
    }

    Dataset read(int id) throws SQLException {
        authorsMap = new HashMap<>();
        booksMap = new HashMap<>();
        publishersMap = new HashMap<>();

        List<Author> authors;
        List<Book> books;
        List<Publisher> publishers;

        String selectDatasetAuthorsSql = "SELECT * FROM " + AUTHOR
                + " JOIN " + DATASETS_AUTHORS
                + " ON " + AUTHOR + "." + ID + "=" + DATASETS_AUTHORS + "." + AUTHOR_ID
                + " WHERE " + DATASETS_AUTHORS + "." + DATASET_ID + "=" + id;
        authors = getAuthors(selectDatasetAuthorsSql);

        String selectDatasetBooksSql = "SELECT * FROM " + BOOK
                + " JOIN " + DATASETS_BOOKS
                + " ON " + BOOK + "." + ID + "=" + DATASETS_BOOKS + "." + BOOK_ID
                + " WHERE " + DATASETS_BOOKS + "." + DATASET_ID + "=" + id;
        books = getBooks(selectDatasetBooksSql);

        String selectDatasetPublishersSql = "SELECT * FROM " + PUBLISHER
                + " JOIN " + DATASETS_PUBLISHERS
                + " ON " + PUBLISHER + "." + ID + "=" + DATASETS_PUBLISHERS + "." + PUBLISHER_ID
                + " WHERE " + DATASETS_PUBLISHERS + "." + DATASET_ID + "=" + id;
        publishers = getPublishers(selectDatasetPublishersSql);

        return new Dataset(authors, books, publishers);
    }

    private List<Author> getAuthors(String sql) throws SQLException {
        List<Author> authors = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Author author;
                int id = rs.getInt(ID);

                if (authorsMap.containsKey(id)) {
                    author = authorsMap.get(id);
                } else {
                    String name = rs.getString(NAME);
                    LocalDate dateOfBirth = LocalDate.parse(rs.getDate(DATE_OF_BIRTH).toString());
                    Date date = rs.getDate(DATE_OF_DEATH);
                    LocalDate dateOfDeath = date != null
                            ? LocalDate.parse(date.toString())
                            : null;
                    Gender gender = rs.getInt(GENDER_ID) == MALE_ID
                            ? Gender.MALE
                            : Gender.FEMALE;

                    author = new Author(name, dateOfBirth, dateOfDeath, gender);
                    authorsMap.put(id, author);
                }

                authors.add(author);
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return authors;
    }

    private List<Book> getBooks(String sql) throws SQLException {
        List<Book> books = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Book book;
                int id = rs.getInt(ID);

                if (booksMap.containsKey(id)) {
                    book = booksMap.get(id);
                } else {
                    String name = rs.getString(NAME);
                    Year yearOfPublication = Year.of(rs.getInt(YEAR_OF_PUBLICATION));

                    String selectBookAuthorsSql = "SELECT * FROM " + AUTHOR
                            + " JOIN " + BOOK_AUTHOR
                            + " ON " + AUTHOR + "." + ID + "=" + BOOK_AUTHOR + "." + AUTHOR_ID
                            + " WHERE " + BOOK_AUTHOR + "." + BOOK_ID + "=" + id;
                    List<Author> bookAuthors = getAuthors(selectBookAuthorsSql);

                    book = new Book(name, yearOfPublication, bookAuthors.toArray(new Author[bookAuthors.size()]));
                    booksMap.put(id, book);
                }

                books.add(book);
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return books;
    }

    private List<Publisher> getPublishers(String sql) throws SQLException {
        List<Publisher> publishers = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Publisher publisher;
                int id = rs.getInt(ID);

                if (publishersMap.containsKey(id)) {
                    publisher = publishersMap.get(id);
                } else {
                    String name = rs.getString(NAME);

                    String selectPublisherBooksSql = "SELECT * FROM " + BOOK
                            + " JOIN " + PUBLISHER_BOOK
                            + " ON " + BOOK + "." + ID + "=" + PUBLISHER_BOOK + "." + BOOK_ID
                            + " WHERE " + PUBLISHER_BOOK + "." + PUBLISHER_ID + "=" + id;
                    List<Book> publisherBooks = getBooks(selectPublisherBooksSql);

                    publisher = new Publisher(name, publisherBooks.toArray(new Book[publisherBooks.size()]));
                    publishersMap.put(id, publisher);
                }

                publishers.add(publisher);
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return publishers;
    }
}
