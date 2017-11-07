package com.epam.homework.books_db.postgresql;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final Logger log = Logger.getRootLogger();

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "booksdb";

    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static void initialize() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            log.debug("Connecting to database...");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            log.debug("Creating database...");
            stmt = conn.createStatement();

            //drops database to perform a clean test
            stmt.executeUpdate("DROP DATABASE " + DB_NAME);

            String sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);
            log.debug("Database created");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e); //TODO: log instead
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e) {
                log.error("Failed to close resources", e);
            }
        }

        executeInitializationScript();
    }

    private static void executeInitializationScript() {
        Connection conn = null;
        Statement stmt = null;
        try {
            log.debug("Connecting to database...");
            conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);

            log.debug("Creating tables...");
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE author (id SERIAL NOT NULL, name varchar NOT NULL, dateOfBirth date NOT NULL, dateOfDeath date, genderId int4 NOT NULL, PRIMARY KEY(id))");
            stmt.executeUpdate("CREATE TABLE gender (id SERIAL NOT NULL, name varchar NOT NULL, PRIMARY KEY(id))");
            stmt.executeUpdate("CREATE TABLE book (id SERIAL NOT NULL, name varchar NOT NULL, yearOfPublication int4 NOT NULL, PRIMARY KEY(id))");
            stmt.executeUpdate("CREATE TABLE booksAuthors (id SERIAL NOT NULL, bookId int4 NOT NULL, authorId int4 NOT NULL)");
            stmt.executeUpdate("CREATE TABLE publisher (id SERIAL NOT NULL, name varchar NOT NULL, PRIMARY KEY(id))");
            stmt.executeUpdate("CREATE TABLE publishersBooks (id SERIAL NOT NULL, publisherId int4 NOT NULL, bookId int4 NOT NULL)");
            stmt.executeUpdate("CREATE TABLE dataset (id SERIAL NOT NULL, PRIMARY KEY(id))");
            stmt.executeUpdate("CREATE TABLE datasetsAuthors (id SERIAL NOT NULL, datasetId int4 NOT NULL, authorId int4 NOT NULL)");
            stmt.executeUpdate("CREATE TABLE datasetsBooks (id SERIAL NOT NULL, datasetId int4 NOT NULL, bookId int4 NOT NULL)");
            stmt.executeUpdate("CREATE TABLE datasetsPublishers (id SERIAL NOT NULL, datasetId int4 NOT NULL, publisherId int4 NOT NULL)");
            stmt.executeUpdate("ALTER TABLE author ADD CONSTRAINT author_uq UNIQUE (name, dateOfBirth, genderId)");
            stmt.executeUpdate("ALTER TABLE book ADD CONSTRAINT book_uq UNIQUE (name, yearOfPublication)");
            stmt.executeUpdate("ALTER TABLE publisher ADD CONSTRAINT publisher_uq UNIQUE (name)");
            stmt.executeUpdate("ALTER TABLE author ADD CONSTRAINT Ref_Author_to_Gender FOREIGN KEY (genderId) REFERENCES gender(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE booksAuthors ADD CONSTRAINT Ref_Book_has_Author_to_Book FOREIGN KEY (bookId) REFERENCES book(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE booksAuthors ADD CONSTRAINT Ref_Book_has_Author_to_Author FOREIGN KEY (authorId) REFERENCES author(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE publishersBooks ADD CONSTRAINT Ref_Publisher_has_Book_to_Publisher FOREIGN KEY (publisherId) REFERENCES publisher(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE publishersBooks ADD CONSTRAINT Ref_Publisher_has_Book_to_Book FOREIGN KEY (bookId) REFERENCES book(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsAuthors ADD CONSTRAINT Ref_Dataset_has_Author_to_Dataset FOREIGN KEY (datasetId) REFERENCES dataset(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsAuthors ADD CONSTRAINT Ref_Dataset_has_Author_to_Author FOREIGN KEY (authorId) REFERENCES author(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsBooks ADD CONSTRAINT Ref_Dataset_has_Book_to_Dataset FOREIGN KEY (datasetId) REFERENCES dataset(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsBooks ADD CONSTRAINT Ref_Dataset_has_Book_to_Book FOREIGN KEY (bookId) REFERENCES book(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsPublishers ADD CONSTRAINT Ref_Dataset_has_Publisher_to_Dataset FOREIGN KEY (datasetId) REFERENCES dataset(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("ALTER TABLE datasetsPublishers ADD CONSTRAINT Ref_Dataset_has_Publisher_to_Publisher FOREIGN KEY (publisherId) REFERENCES publisher(id) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE");
            stmt.executeUpdate("INSERT INTO gender(id, name) VALUES (1, \'MALE\'), (2, \'FEMALE\')");

            log.debug("Tables created");
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: log instead
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e) {
                log.error("Failed to close resources", e);
            }
        }
    }
}
