package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.serializers.custom_serializer.CustomSerializer;
import com.epam.homework.books_db.serialization.serializers.standard_serializer.StandardSerializer;
import org.apache.log4j.Logger;

public class App {

    private static final String STANDARD_SERIALIZER_FILENAME = "dataset.ser";
    private static final String CUSTOM_SERIALIZER_FILENAME = "dataset.txt";

    private static Logger log = Logger.getRootLogger();

    public static void main(String[] args) {
        performTests();
    }

    private static void performTests() {
        Dataset exampleDataset = DatasetInitializer.getAnotherExampleDataset();
        new DatasetPrinter().basicPrint(exampleDataset);

        log.info("*** Testing standard serializer ***");
        testSerializer(exampleDataset, new StandardSerializer(), STANDARD_SERIALIZER_FILENAME);

        log.info("*** Testing custom serializer ***");
        testSerializer(exampleDataset, new CustomSerializer(), CUSTOM_SERIALIZER_FILENAME);
    }

    private static void testSerializer(Dataset dataset, Serializer serializer, String filename) {
        log.info("Serializing dataset...");
        try {
            serializer.save(dataset, filename);
            log.info("Dataset serialized");
        } catch (SerializerException e) {
            log.error("Dataset serialization failed", e);
        }

        log.info("Deserializing dataset...");
        try {
            Dataset loadedDataset = serializer.load(filename);
            log.info("Dataset deserialized");

            log.info("Comparing deserialized dataset with dataset that was serialized...");
            if (loadedDataset.equals(dataset)) {
                log.info("Deserialized dataset is correct");
            } else {
                log.info("Deserialized dataset is not correct");
            }

            new DatasetPrinter().basicPrint(loadedDataset);
        } catch (SerializerException e) {
            log.error("Dataset deserialization failed", e);
        }
    }
}