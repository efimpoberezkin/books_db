package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.custom_serializer.CustomSerializer;
import com.epam.homework.books_db.serialization.serializers.standard_serializer.StandardSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {

    private static final String STANDARD_SERIALIZER_FILENAME = "src\\test\\resources\\standard_serializer\\dataset.ser";
    private static final String CUSTOM_SERIALIZER_FILENAME = "src\\test\\resources\\custom_serializer\\dataset.txt";

    @Test
    public void testStandardSerializer() {
        Dataset exampleDataset = DatasetInitializer.getAnotherExampleDataset();
        Serializer serializer = new StandardSerializer();

        serializer.save(exampleDataset, STANDARD_SERIALIZER_FILENAME);
        Dataset loadedDataset = serializer.load(STANDARD_SERIALIZER_FILENAME);

        assertEquals(loadedDataset, exampleDataset);
    }

    @Test
    public void testCustomSerializer() {
        Dataset exampleDataset = DatasetInitializer.getAnotherExampleDataset();
        Serializer serializer = new CustomSerializer();

        serializer.save(exampleDataset, CUSTOM_SERIALIZER_FILENAME);
        Dataset loadedDataset = serializer.load(CUSTOM_SERIALIZER_FILENAME);

        assertEquals(loadedDataset, exampleDataset);
    }
}
