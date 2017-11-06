package com.epam.homework.books_db;

import com.epam.homework.books_db.serialization.serializers.postgresql.DatabaseInitializer;
import org.apache.log4j.Logger;

public class DatabaseApp {

    private static final Logger log = Logger.getRootLogger();

    private static final boolean dbInitializationFlag = true;

    public static void main(String[] args) {
        if (dbInitializationFlag) {
            DatabaseInitializer.initialize();
        }
        testDatabase();
    }

    private static void testDatabase() {

    }
}
