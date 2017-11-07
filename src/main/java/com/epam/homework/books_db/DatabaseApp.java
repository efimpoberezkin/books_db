package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.postgresql.DatabaseDao;
import com.epam.homework.books_db.postgresql.DatabaseInitializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import org.apache.log4j.Logger;

public class DatabaseApp {

    private static final String XML_PATH = "src\\main\\resources\\xml\\example_dataset.xml";

    private static final Logger log = Logger.getRootLogger();

    private static final boolean dbInitializationFlag = true;

    public static void main(String[] args) {
        if (dbInitializationFlag) {
            DatabaseInitializer.initialize();
        }
        testDatabase();
    }

    private static void testDatabase() {
        log.info("*** Reading dataset from xml and writing it to database ***");
        Dataset exampleDataset = null;
        try {
            log.debug("Loading dataset from xml...");
            exampleDataset = new DomParser().load(XML_PATH);
            log.debug("Dataset loaded from xml");
        } catch (SerializerException e) {
            log.error("Failed to load dataset from xml", e);
        }
        new DatabaseDao().save(exampleDataset);

        log.info("*** Reading dataset from database ***");
        Dataset loadedDataset = new DatabaseDao().load(1);

        new DatasetPrinter().customPrint(loadedDataset);
    }
}
