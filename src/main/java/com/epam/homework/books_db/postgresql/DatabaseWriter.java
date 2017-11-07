package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

class DatabaseWriter {

    private static final Logger log = Logger.getRootLogger();

    private final Connection conn;

    private Map<Author, Integer> authorsMap;
    private Map<Book, Integer> booksMap;
    private Map<Publisher, Integer> publishersMap;

    DatabaseWriter(Connection conn) {
        this.conn = conn;
    }

    void write(Dataset dataset) {
        authorsMap = new HashMap<>();
        booksMap = new HashMap<>();
        publishersMap = new HashMap<>();

        PreparedStatement stmt = null;

        try {
            String insertDatasetSql = "INSERT INTO " + DATASET + "(" + ID + ") VALUES(DEFAULT)";
            stmt = conn.prepareStatement(insertDatasetSql, Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();

            int datasetKey = -1;
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                datasetKey = rs.getInt(1);
            }

            writeAuthors(dataset.getAuthors(), stmt);

            String insertDatasetsAuthorsSql = "INSERT INTO " + DATASETS_AUTHORS
                    + "(" + DATASET_ID + ", " + AUTHOR_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsAuthorsSql);

            for (Author author : dataset.getAuthors()) {
                stmt.setInt(1, authorsMap.get(author));
                stmt.executeUpdate();
            }

            writeBooks(dataset.getBooks(), stmt);

            String insertDatasetsBooksSql = "INSERT INTO " + DATASETS_BOOKS
                    + "(" + DATASET_ID + ", " + BOOK_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsBooksSql);

            for (Book book : dataset.getBooks()) {
                stmt.setInt(1, booksMap.get(book));
                stmt.executeUpdate();
            }

            writePublishers(dataset.getPublishers(), stmt);

            String insertDatasetsPublishersSql = "INSERT INTO " + DATASETS_PUBLISHERS
                    + "(" + DATASET_ID + ", " + PUBLISHER_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsPublishersSql);

            for (Publisher publisher : dataset.getPublishers()) {
                stmt.setInt(1, publishersMap.get(publisher));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Failed to save dataset to database", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                log.error("Failed to close statement", e);
            }
        }
    }

    private void writeAuthors(List<Author> authors, PreparedStatement stmt) throws SQLException {
        for (Author author : authors) {
            if (!authorsMap.containsKey(author)) {
                String name = "\'" + author.getName() + "\'";
                String birth = "\'" + author.getDateOfBirth().toString() + "\'";
                String death = author.getDateOfDeath().isPresent()
                        ? "\'" + author.getDateOfDeath().get().toString() + "\'"
                        : "NULL";
                int gender = author.getGender() == Gender.MALE ? MALE_ID : FEMALE_ID;

                String insertAuthorSql = "INSERT INTO " + AUTHOR
                        + "(" + NAME + ", " + DATE_OF_BIRTH + ", " + DATE_OF_DEATH + ", " + GENDER_ID + ") VALUES"
                        + "(" + name + "," + birth + "," + death + "," + gender + ") "
                        + "ON CONFLICT ON CONSTRAINT author_uq "
                        + "DO UPDATE SET " + NAME + "=" + name + " "
                        + "RETURNING " + ID + ";--"; // adding ;-- so PreparedStatement doesn't break returning option
                stmt = conn.prepareStatement(insertAuthorSql, Statement.RETURN_GENERATED_KEYS);

                stmt.executeUpdate();

                int key = -1;
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    key = rs.getInt(1);
                }

                authorsMap.put(author, key);
            }
        }
    }

    private void writeBooks(List<Book> books, PreparedStatement stmt) throws SQLException {
        for (Book book : books) {
            if (!booksMap.containsKey(book)) {
                String name = "\'" + book.getName() + "\'";
                Year year = book.getYearOfPublication();

                String insertBookSql = "INSERT INTO " + BOOK
                        + "(" + NAME + ", " + YEAR_OF_PUBLICATION + ") VALUES"
                        + "(" + name + "," + year + ") "
                        + "ON CONFLICT ON CONSTRAINT book_uq "
                        + "DO UPDATE SET " + NAME + "=" + name + " "
                        + "RETURNING " + ID + ";--"; // adding ;-- so PreparedStatement doesn't break returning option
                stmt = conn.prepareStatement(insertBookSql, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();

                int key = -1;
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    key = rs.getInt(1);
                }

                writeAuthors(book.getAuthors(), stmt);

                String insertBooksAuthorsSql = "INSERT INTO " + BOOKS_AUTHORS
                        + "(" + BOOK_ID + ", " + AUTHOR_ID + ") VALUES"
                        + "(" + key + ",?)";
                stmt = conn.prepareStatement(insertBooksAuthorsSql, Statement.RETURN_GENERATED_KEYS);

                for (Author author : book.getAuthors()) {
                    stmt.setInt(1, authorsMap.get(author));
                    stmt.executeUpdate();
                }

                booksMap.put(book, key);
            }
        }
    }

    private void writePublishers(List<Publisher> publishers, PreparedStatement stmt) throws SQLException {
        for (Publisher publisher : publishers) {
            if (!publishersMap.containsKey(publisher)) {
                String name = "\'" + publisher.getName() + "\'";

                String insertPublisherSql = "INSERT INTO " + PUBLISHER
                        + "(" + NAME + ") VALUES"
                        + "(" + name + ")"
                        + "ON CONFLICT ON CONSTRAINT publisher_uq "
                        + "DO UPDATE SET " + NAME + "=" + name + " "
                        + "RETURNING " + ID + ";--"; // adding ;-- so PreparedStatement doesn't break returning option
                stmt = conn.prepareStatement(insertPublisherSql, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();

                int key = -1;
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    key = rs.getInt(1);
                }

                writeBooks(publisher.getPublishedBooks(), stmt);

                String insertPublishersBooksSql = "INSERT INTO " + PUBLISHERS_BOOKS
                        + "(" + PUBLISHER_ID + ", " + BOOK_ID + ") VALUES"
                        + "(" + key + ",?)";
                stmt = conn.prepareStatement(insertPublishersBooksSql, Statement.RETURN_GENERATED_KEYS);

                for (Book book : publisher.getPublishedBooks()) {
                    stmt.setInt(1, booksMap.get(book));
                    stmt.executeUpdate();
                }

                publishersMap.put(publisher, key);
            }
        }
    }
}
