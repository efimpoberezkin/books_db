package com.epam.homework.books_db.postgresql;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String INITIALIZATION_SCRIPT = "src\\main\\resources\\sql\\db_initialization.sql";

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

            // drops database to perform a clean start
            try {
                stmt.executeUpdate("DROP DATABASE " + DB_NAME);
            } catch (SQLException e) { // ignore
            }

            String sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);
            log.debug("Database created");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to create database", e);
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
        try {
            log.debug("Connecting to database...");
            conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);

            log.debug("Creating tables...");
            ScriptRunner sr = new ScriptRunner(conn);
            sr.setLogWriter(null);
            Reader reader = new BufferedReader(new FileReader(INITIALIZATION_SCRIPT));
            sr.runScript(reader);
            reader.close();

            log.debug("Tables created");
        } catch (IOException | SQLException e) {
            log.error("Failed to execute database initialization script", e);
        } finally {
            try {
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
