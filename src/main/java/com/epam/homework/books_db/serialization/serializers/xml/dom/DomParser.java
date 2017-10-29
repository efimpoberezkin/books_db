package com.epam.homework.books_db.serialization.serializers.xml.dom;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.validation.ValidationException;
import com.epam.homework.books_db.serialization.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomParser implements Serializer {

    @Override
    public void save(Dataset dataset, String xml) {
        throw new UnsupportedOperationException("Save not supported");
    }

    @Override
    public Dataset load(String xml) throws SerializerException {
        Dataset loadedDataset;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xml));

            Element root = document.getDocumentElement();
            loadedDataset = new Analyzer().buildDataset(root);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new SerializerException("Could not load dataset", e);
        }
        try {
            new Validator().validateDataset(loadedDataset);
        } catch (ValidationException e) {
            throw new SerializerException("Could not validate loaded dataset, reason: " + e.getMessage(), e);
        }
        return loadedDataset;
    }
}
