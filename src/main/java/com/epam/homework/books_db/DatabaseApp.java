package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.postgresql.DaoException;
import com.epam.homework.books_db.postgresql.DatabaseDao;
import com.epam.homework.books_db.postgresql.DatabaseInitializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import org.apache.log4j.Logger;

public class DatabaseApp {

    private static final String XML_PATH = "src\\main\\resources\\xml\\example_dataset.xml";
    private static final Logger log = Logger.getRootLogger();
    private static final boolean dbInitializationFlag = true;
    private static final int DATASET_ID_TO_LOAD = 1;

    public static void main(String[] args) {
        if (dbInitializationFlag) {
            DatabaseInitializer.initialize(true);
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

            new DatabaseDao().save(exampleDataset);
        } catch (SerializerException e) {
            log.error("Failed to load dataset from xml", e);
        } catch (DaoException e) {
            log.error("Failed to save dataset to database", e);
        }

        log.info("*** Reading dataset from database ***");
        Dataset loadedDataset = null;
        try {
            loadedDataset = new DatabaseDao().load(DATASET_ID_TO_LOAD);
        } catch (DaoException e) {
            log.error("Failed to load dataset from database", e);
        }

        new DatasetPrinter().customPrint(loadedDataset);

        if (loadedDataset.equals(exampleDataset)) {
            System.out.println("\nLoaded dataset is correct");
        } else {
            System.out.println("\nLoaded dataset is not correct");
        }
    }
}
