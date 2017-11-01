package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.validation.ValidationException;
import com.epam.homework.books_db.serialization.validation.Validator;

import java.io.*;

public class CustomSerializer implements Serializer {

    @Override
    public void save(Dataset dataset, String filename) throws SerializerException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            new Writer().write(dataset, out);
        } catch (IOException e) {
            throw new SerializerException("Could not save dataset", e);
        }
    }

    @Override
    public Dataset load(String filename) throws SerializerException {
        Dataset loadedDataset;
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            loadedDataset = new Reader().read(in);
            new Validator().validateDataset(loadedDataset);
        } catch (IOException e) {
            throw new SerializerException("Could not load dataset", e);
        } catch (ValidationException e) {
            throw new SerializerException("Could not validate loaded dataset, reason: " + e.getMessage(), e);
        }
        return loadedDataset;
    }
}
