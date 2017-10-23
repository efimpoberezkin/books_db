package com.epam.homework.books_db.serialization.serializers;

public class SerializerException extends RuntimeException {

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Exception e) {
        super(message, e);
    }
}
