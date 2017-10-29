package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.dataset.DatasetPrinter;
import com.epam.homework.books_db.serialization.serializers.xml.dom.DomParser;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.crypto.Data;
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

    public static void main(String[] args) {
        validateXml();

        System.out.println("\n*** Dataset loaded by DOM Parser ***");
        Dataset dataset = new DomParser().load(XML_PATH);
        DatasetPrinter.customPrint(dataset);

        if (DatasetInitializer.getAnotherExampleDataset().equals(dataset)) {
            System.out.println("\nDataset is correct");
        } else {
            System.out.println("\nDataset is not correct");
        }
    }

    private static void validateXml() {
        try {
            File schemaFile = Paths.get(XSD_PATH).toFile();
            File dataFile = Paths.get(XML_PATH).toFile();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new FileInputStream(dataFile)));
        } catch(SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
