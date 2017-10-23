package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;

public class CustomSerializer implements Serializer {

    @Override
    public void save(Dataset dataset, String filename) {

    }

    @Override
    public Dataset load(String filename) {


        Dataset loadedDataset = null;
        return loadedDataset;
    }
}
