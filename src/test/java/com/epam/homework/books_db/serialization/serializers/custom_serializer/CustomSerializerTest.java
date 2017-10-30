package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomSerializerTest {

    private static final String PATH_TO_SAVE = "dataset.txt";
    private static final String NORMAL_FILE = "src\\test\\resources\\custom_serializer\\dataset_normal.txt";
    private static final String CORRUPTED_FILE = "src\\test\\resources\\custom_serializer\\dataset_corrupted.txt";
    private static final String NON_VALID_FILE = "src\\test\\resources\\custom_serializer\\dataset_non_valid.txt";

    @Test
    public void loadNormalTest() {
        Dataset loadedDataset = new CustomSerializer().load(NORMAL_FILE);
        Dataset expectedDataset = DatasetInitializer.getSmallExampleDataset();

        assertEquals(loadedDataset, expectedDataset);
    }

    @Test(expected = SerializerException.class)
    public void loadCorruptedTest() {
        new CustomSerializer().load(CORRUPTED_FILE);
    }

    @Test(expected = SerializerException.class)
    public void loadNonValidTest() {
        new CustomSerializer().load(NON_VALID_FILE);
    }

    @Test
    public void saveTest() {
        Serializer customSerializer = new CustomSerializer();

        Dataset dataset = DatasetInitializer.getSmallExampleDataset();
        customSerializer.save(dataset, PATH_TO_SAVE);

        Dataset savedDataset = customSerializer.load(PATH_TO_SAVE);
        Dataset expectedDataset = customSerializer.load(NORMAL_FILE);

        assertEquals(savedDataset, expectedDataset);
    }
}
