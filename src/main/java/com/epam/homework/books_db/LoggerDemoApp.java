package com.epam.homework.books_db;

import org.apache.log4j.Logger;

public class LoggerDemoApp {

    private static final Logger log = Logger.getRootLogger();

    public static void main(String[] args) {
        testLogger();
    }

    private static void testLogger() {
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message");
        log.warn("warn message");
        log.error("error message");
        log.fatal("fatal message");
    }
}
