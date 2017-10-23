package com.epam.homework.books_db.serialization.serializers;

import com.epam.homework.books_db.dataset.Dataset;

public interface Serializer {

    void save(Dataset dataset, String filename);

    Dataset load(String filename);
}
