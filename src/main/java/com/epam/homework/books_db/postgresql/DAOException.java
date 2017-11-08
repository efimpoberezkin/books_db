package com.epam.homework.books_db.postgresql;

public class DAOException extends RuntimeException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }
}
