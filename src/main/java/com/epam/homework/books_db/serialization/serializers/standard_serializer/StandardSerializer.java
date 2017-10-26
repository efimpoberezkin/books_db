package com.epam.homework.books_db.serialization.serializers.standard_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import com.epam.homework.books_db.serialization.validation.ValidationException;
import com.epam.homework.books_db.serialization.validation.Validator;

import java.io.*;

public class StandardSerializer implements Serializer {

    @Override
    public void save(Dataset dataset, String filename) throws SerializerException {
        SerializableDataset serializableDataset = new DatasetTransformer().transformIntoSerializable(dataset);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
            out.writeObject(serializableDataset);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new SerializerException("Could not save dataset", e);
        }
    }

    @Override
    public Dataset load(String filename) throws SerializerException {
        SerializableDataset serializableDataset;
        try {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
            serializableDataset = (SerializableDataset) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializerException("Could not load dataset", e);
        }
        Dataset loadedDataset = new DatasetTransformer().transformIntoDomain(serializableDataset);
        try {
            Validator.validateDataset(loadedDataset);
        } catch (ValidationException e) {
            throw new SerializerException("Could not validate loaded dataset, reason: " + e.getMessage(), e);
        }
        return loadedDataset;
    }
}
