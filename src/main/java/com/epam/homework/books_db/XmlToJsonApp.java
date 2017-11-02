package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetToJsonConverter;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

public class XmlToJsonApp {

    private static final String JSON_PATH = "dataset.json";
    private static final String XML_PATH = "src\\main\\resources\\xml\\small_dataset.xml";

    private static Logger log = Logger.getRootLogger();

    public static void main(String[] args) {
        transformXmlIntoJson();
    }

    private static void transformXmlIntoJson() {
        log.info("*** Transforming xml into json ***");
        try {
            log.info("Loading dataset from xml...");
            Dataset dataset = new DomParser().load(XML_PATH);
            log.info("Dataset loaded from xml");

            log.info("Tranforming dataset into json...");
            String json = new DatasetToJsonConverter().toJson(dataset);
            log.info("Dataset transformed into json");

            log.info("Saving json to file...");
            try (FileWriter writer = new FileWriter(JSON_PATH)) {
                writer.write(json);
                log.info("Json saved to file");
            } catch (IOException e) {
                log.error("Failed to save json to file", e);
            }
        } catch (SerializerException e) {
            log.error("Failed to load dataset from xml", e);
        }
    }
}
