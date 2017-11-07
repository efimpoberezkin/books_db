package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;

import java.sql.*;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

class DatabaseWriter {

    private final Connection conn;

    private Map<Author, Integer> authorsMap;
    private Map<Book, Integer> booksMap;
    private Map<Publisher, Integer> publishersMap;

    DatabaseWriter(Connection conn) {
        this.conn = conn;
    }

    void write(Dataset dataset) throws SQLException {
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

            writeAuthors(dataset.getAuthors());

            String insertDatasetsAuthorsSql = "INSERT INTO " + DATASETS_AUTHORS
                    + "(" + DATASET_ID + ", " + AUTHOR_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsAuthorsSql);

            for (Author author : dataset.getAuthors()) {
                stmt.setInt(1, authorsMap.get(author));
                stmt.executeUpdate();
            }

            writeBooks(dataset.getBooks());

            String insertDatasetsBooksSql = "INSERT INTO " + DATASETS_BOOKS
                    + "(" + DATASET_ID + ", " + BOOK_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsBooksSql);

            for (Book book : dataset.getBooks()) {
                stmt.setInt(1, booksMap.get(book));
                stmt.executeUpdate();
            }

            writePublishers(dataset.getPublishers());

            String insertDatasetsPublishersSql = "INSERT INTO " + DATASETS_PUBLISHERS
                    + "(" + DATASET_ID + ", " + PUBLISHER_ID + ") VALUES"
                    + "(" + datasetKey + ",?)";
            stmt = conn.prepareStatement(insertDatasetsPublishersSql);

            for (Publisher publisher : dataset.getPublishers()) {
                stmt.setInt(1, publishersMap.get(publisher));
                stmt.executeUpdate();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private void writeAuthors(List<Author> authors) throws SQLException {
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
                        + "(" + name + "," + birth + "," + death + "," + gender + ")"
                        + " ON CONFLICT ON CONSTRAINT author_uq"
                        + " DO UPDATE SET " + NAME + "=" + name // updating so that RETURNING works
                        + " RETURNING " + ID + ";--"; // adding ;-- so that Statement doesn't break RETURNING option

                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(insertAuthorSql, Statement.RETURN_GENERATED_KEYS);

                    int key = -1;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getInt(1);
                    }

                    authorsMap.put(author, key);
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            }
        }
    }

    private void writeBooks(List<Book> books) throws SQLException {
        for (Book book : books) {
            if (!booksMap.containsKey(book)) {
                String name = "\'" + book.getName() + "\'";
                Year year = book.getYearOfPublication();

                String insertBookSql = "INSERT INTO " + BOOK
                        + "(" + NAME + ", " + YEAR_OF_PUBLICATION + ") VALUES"
                        + "(" + name + "," + year + ")"
                        + " ON CONFLICT ON CONSTRAINT book_uq"
                        + " DO UPDATE SET " + NAME + "=" + name // updating so that RETURNING works
                        + " RETURNING " + ID + ";--"; // adding ;-- so that Statement doesn't break RETURNING option

                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(insertBookSql, Statement.RETURN_GENERATED_KEYS);

                    int key = -1;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getInt(1);
                    }

                    writeAuthors(book.getAuthors());

                    String insertBooksAuthorsSql = "INSERT INTO " + BOOKS_AUTHORS
                            + "(" + BOOK_ID + ", " + AUTHOR_ID + ") VALUES"
                            + "(" + key + ",?)"
                            + "ON CONFLICT ON CONSTRAINT booksAuthors_uq DO NOTHING";

                    PreparedStatement prepStmt = null;
                    try {
                        prepStmt = conn.prepareStatement(insertBooksAuthorsSql, Statement.RETURN_GENERATED_KEYS);

                        for (Author author : book.getAuthors()) {
                            prepStmt.setInt(1, authorsMap.get(author));
                            prepStmt.executeUpdate();
                        }
                    } finally {
                        if (prepStmt != null) {
                            prepStmt.close();
                        }
                    }

                    booksMap.put(book, key);
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            }
        }
    }

    private void writePublishers(List<Publisher> publishers) throws SQLException {
        for (Publisher publisher : publishers) {
            if (!publishersMap.containsKey(publisher)) {
                String name = "\'" + publisher.getName() + "\'";

                String insertPublisherSql = "INSERT INTO " + PUBLISHER
                        + "(" + NAME + ") VALUES"
                        + "(" + name + ")"
                        + " ON CONFLICT ON CONSTRAINT publisher_uq"
                        + " DO UPDATE SET " + NAME + "=" + name // updating so that RETURNING works
                        + " RETURNING " + ID + ";--"; // adding ;-- so that Statement doesn't break RETURNING option

                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(insertPublisherSql, Statement.RETURN_GENERATED_KEYS);

                    int key = -1;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getInt(1);
                    }

                    writeBooks(publisher.getPublishedBooks());

                    String insertPublishersBooksSql = "INSERT INTO " + PUBLISHERS_BOOKS
                            + "(" + PUBLISHER_ID + ", " + BOOK_ID + ") VALUES"
                            + "(" + key + ",?)"
                            + "ON CONFLICT ON CONSTRAINT publishersBooks_uq DO NOTHING";

                    PreparedStatement prepStmt = null;
                    try {
                        prepStmt = conn.prepareStatement(insertPublishersBooksSql, Statement.RETURN_GENERATED_KEYS);

                        for (Book book : publisher.getPublishedBooks()) {
                            prepStmt.setInt(1, booksMap.get(book));
                            prepStmt.executeUpdate();
                        }
                    } finally {
                        if (prepStmt != null) {
                            prepStmt.close();
                        }
                    }

                    publishersMap.put(publisher, key);
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            }
        }
    }
}
