package com.epam.homework.books_db.postgresql;

public class DatabaseServiceException extends RuntimeException {

    public DatabaseServiceException(String message) {
        super(message);
    }

    public DatabaseServiceException(String message, Exception e) {
        super(message, e);
    }
}
