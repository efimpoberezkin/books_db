package com.epam.homework.books_db.serialization.serializers.standard_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StandardSerializerTest {

    private static final String PATH_TO_SAVE = "dataset.ser";
    private static final String NORMAL_FILE = "src\\test\\resources\\standard_serializer\\dataset_normal.ser";
    private static final String CORRUPTED_FILE = "src\\test\\resources\\standard_serializer\\dataset_corrupted.ser";

    @Test
    public void loadNormalTest() {
        Dataset loadedDataset = new StandardSerializer().load(NORMAL_FILE);
        Dataset expectedDataset = DatasetInitializer.getSmallExampleDataset();

        assertEquals(loadedDataset, expectedDataset);
    }

    @Test(expected = SerializerException.class)
    public void loadCorruptedTest() {
        new StandardSerializer().load(CORRUPTED_FILE);
    }

    @Test
    public void saveTest() {
        Serializer standardSerializer = new StandardSerializer();

        Dataset dataset = DatasetInitializer.getSmallExampleDataset();
        standardSerializer.save(dataset, PATH_TO_SAVE);

        Dataset savedDataset = standardSerializer.load(PATH_TO_SAVE);
        Dataset expectedDataset = standardSerializer.load(NORMAL_FILE);

        assertEquals(savedDataset, expectedDataset);
    }
}
