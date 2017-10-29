package com.epam.homework.books_db.serialization.serializers.xml.sax;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.validation.ValidationException;
import com.epam.homework.books_db.serialization.validation.Validator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class SaxParser implements Serializer {

    @Override
    public void save(Dataset dataset, String xml) {
        throw new UnsupportedOperationException("Save not supported");
    }

    @Override
    public Dataset load(String xml) throws SerializerException {
        Dataset loadedDataset;
        try {
            SaxHandler saxHandler = new SaxHandler();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(saxHandler);
            reader.parse(new InputSource(new FileInputStream(xml)));

            loadedDataset = saxHandler.getDataset();
        } catch (SAXException | IOException e) {
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
