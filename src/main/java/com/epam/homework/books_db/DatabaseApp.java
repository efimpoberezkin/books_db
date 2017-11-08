package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.postgresql.DatabaseService;
import com.epam.homework.books_db.postgresql.DatabaseInitializer;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import org.apache.log4j.Logger;

public class DatabaseApp {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String XML_PATH = "src\\main\\resources\\xml\\example_dataset.xml";
    private static final String DB_DEPLOYMENT_PARAMETER = "deployDatabase";
    private static final Logger log = Logger.getRootLogger();

    public static void main(String[] args) {
        deployDatabase(args);
    }

    private static void deployDatabase(String[] args) {
        String parameter = null;
        try {
            parameter = args[0];
        } catch (ArrayIndexOutOfBoundsException e) { // ignore
        }

        if (DB_DEPLOYMENT_PARAMETER.equals(parameter)) {
            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                log.error("Could not load driver class", e);
            }

            log.info("*** Initializing database ***");
            DatabaseInitializer.initialize(true);

            log.info("*** Populating database from xml ***");
            populateDatabaseFromXml();
        }

        //log.info("*** Reading from database ***");
        //readFromDatabase();
    }

    private static void populateDatabaseFromXml() {
        log.debug("Loading dataset from xml...");
        Dataset dataset = new DomParser().load(XML_PATH);
        log.debug("Dataset loaded from xml");

        log.debug("Saving data to database...");
        new DatabaseService().writeDataset(dataset);
        log.debug("Data saved to database");
    }

    private static void readFromDatabase() {
        log.debug("Loading data from database...");
        Dataset dataset = new DatabaseService().readDataset();
        log.debug("Data loaded from database");

        new DatasetPrinter().customPrint(dataset);
    }
}
