package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import com.epam.homework.books_db.serialization.serializers.xml.sax.SaxParser;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class XmlApp {

    private static final String XML_PATH = "src\\main\\resources\\xml\\example_dataset.xml";
    private static final String XSD_PATH = "src\\main\\resources\\xml\\dataset_schema.xsd";

    private static Logger log = Logger.getRootLogger();

    public static void main(String[] args) {
        performTest();
    }

    private static void performTest() {
        if (validateXml(XML_PATH, XSD_PATH)) {
            log.info("*** Loading dataset via DOM parser ***");
            testParser(new DomParser(), XML_PATH);

            log.info("*** Loading dataset via SAX parser ***");
            testParser(new SaxParser(), XML_PATH);
        }
    }

    private static void testParser(Serializer parser, String filename) {
        try {
            Dataset dataset = parser.load(filename);
            log.info("Dataset loaded");
            new DatasetPrinter().customPrint(dataset);
        } catch (SerializerException e) {
            log.error("Failed to load dataset", e);
        }
    }

    private static boolean validateXml(String xml, String xsd) {
        log.info("Validating XML...");
        try {
            File schemaFile = Paths.get(xsd).toFile();
            File dataFile = Paths.get(xml).toFile();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new FileInputStream(dataFile)));
            log.info("XML validated");
            return true;
        } catch (SAXException | IOException e) {
            log.error("XML validation failed", e);
            return false;
        }
    }
}
