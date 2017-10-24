package com.epam.homework.books_db;

import com.epam.homework.books_db.dataset.DatasetInitializer;
import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.custom_serializer.CustomSerializer;
import com.epam.homework.books_db.serialization.serializers.standard_serializer.StandardSerializer;

public class Test {

    private static final String STANDARD_SERIALIZER_FILENAME = "dataset.ser";
    private static final String CUSTOM_SERIALIZER_FILENAME = "dataset.txt";

    public static void main(String[] args) {
        performTests();
    }

    private static void performTests() {
        Dataset exampleDataset = DatasetInitializer.getExampleDataset();
        //DatasetPrinter.print(exampleDataset);

        System.out.println("\n*** Testing standard serializer ***");
        testSerializer(exampleDataset, new StandardSerializer(), STANDARD_SERIALIZER_FILENAME);

        System.out.println("\n*** Testing custom serializer ***");
        testSerializer(exampleDataset, new CustomSerializer(), CUSTOM_SERIALIZER_FILENAME);
    }

    private static void testSerializer (Dataset dataset, Serializer serializer, String filename) {
        System.out.println("\nSerializing dataset...");
        serializer.save(dataset, filename);
        System.out.println("Dataset serialized");

        System.out.println("\nDeserializing dataset...");
        Dataset loadedDataset = serializer.load(filename);
        System.out.println("Dataset deserialized");

        System.out.println("\nComparing deserialized dataset with dataset that was serialized...");
        if (loadedDataset.equals(dataset)) {
            System.out.println("Deserialized dataset is correct");
        } else {
            System.out.println("Deserialized dataset is not correct");
        }
    }
}