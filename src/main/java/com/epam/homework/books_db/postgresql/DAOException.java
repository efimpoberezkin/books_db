package com.epam.homework.books_db.postgresql;

class DAOException extends RuntimeException {

    DAOException(String message) {
        super(message);
    }

    DAOException(String message, Exception e) {
        super(message, e);
    }
}
