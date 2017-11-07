package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.validation.ValidationException;
import com.epam.homework.books_db.serialization.validation.Validator;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDao {

    private static final Logger log = Logger.getRootLogger();

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/booksdb";

    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public void save(Dataset dataset) {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);

            log.debug("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            log.debug("Saving dataset to database...");
            new DatabaseWriter(conn).write(dataset);

            log.debug("Dataset saved");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to save dataset to database", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e) {
                log.error("Failed to close connection", e);
            }
        }
    }

    public Dataset load(int id) {
        Connection conn = null;
        Dataset loadedDataset = null;
        try {
            Class.forName(JDBC_DRIVER);

            log.debug("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            log.debug("Loading dataset from database...");
            loadedDataset = new DatabaseReader(conn).read(id);

            log.debug("Dataset loaded");

            new Validator().validateDataset(loadedDataset);
            log.debug("Dataset validated");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to save dataset to database", e);
        } catch (ValidationException e) {
            log.error("Could not validate loaded dataset, reason: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    log.debug("Connection closed");
                }
            } catch (SQLException e) {
                log.error("Failed to close connection", e);
            }
        }
        return loadedDataset;
    }
}
