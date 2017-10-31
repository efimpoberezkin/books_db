package com.epam.homework.books_db.serialization.serializers.standard_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;
import com.epam.homework.books_db.serialization.serializers.Serializer;
import com.epam.homework.books_db.serialization.serializers.SerializerException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StandardSerializerTest {

    private static final String PATH_TO_SAVE = "dataset.ser";
    private static final String NORMAL_FILE = "src\\test\\resources\\standard_serializer\\dataset_normal.ser";
    private static final String CORRUPTED_FILE = "src\\test\\resources\\standard_serializer\\dataset_corrupted.ser";

    private static Dataset exampleDataset;

    @BeforeClass
    public static void initializeExampleDataset() {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        formDataForExampleDataset(authors, books, publishers);

        exampleDataset = new Dataset(authors, books, publishers);
    }

    @Test
    public void tstLoadNormal() {
        Dataset loadedDataset = new StandardSerializer().load(NORMAL_FILE);
        assertEquals(exampleDataset, loadedDataset);
    }

    @Test(expected = SerializerException.class)
    public void tstLoadCorrupted() {
        new StandardSerializer().load(CORRUPTED_FILE);
    }

    @Test
    public void tstSave() {
        Serializer standardSerializer = new StandardSerializer();
        standardSerializer.save(exampleDataset, PATH_TO_SAVE);
        Dataset savedDataset = standardSerializer.load(PATH_TO_SAVE);
        Dataset expectedDataset = standardSerializer.load(NORMAL_FILE);

        assertEquals(expectedDataset, savedDataset);
    }

    @AfterClass
    public static void destroyExampleDataset() {
        exampleDataset = null;
    }

    private static void formDataForExampleDataset(List<Author> authors, List<Book> books, List<Publisher> publishers) {
        Author authorCervantes
                = new Author("Cervantes", LocalDate.parse("1980-01-13"), Gender.MALE);
        Author authorChristie
                = new Author("Christie", LocalDate.parse("1942-05-24"), Gender.FEMALE);
        Author authorShakespeare
                = new Author("Shakespeare", LocalDate.parse("1958-08-15"), LocalDate.parse("2012-03-15"), Gender.MALE);

        authors.addAll(new ArrayList<>(Arrays.asList(
                authorCervantes, authorChristie, authorShakespeare
        )));

        Author authorTwain
                = new Author("Twain", LocalDate.parse("1953-03-07"), Gender.MALE);

        Book bookDonQuixote
                = new Book("Don Quixote", Year.of(2015), authorCervantes);
        Book bookHuckleberryFinn
                = new Book("The Adventures of Huckleberry Finn", Year.of(2007), authorTwain);
        Book bookMacbeth
                = new Book("Macbeth", Year.of(2012), authorShakespeare);
        Book bookTenLittleNiggers
                = new Book("Ten Little Niggers", Year.of(2009), authorCervantes, authorChristie);

        books.addAll(new ArrayList<>(Arrays.asList(
                bookDonQuixote, bookHuckleberryFinn, bookMacbeth, bookTenLittleNiggers
        )));

        Author authorAkhmatova
                = new Author("Akhmatova", LocalDate.parse("1946-09-18"), LocalDate.parse("2014-11-07"), Gender.FEMALE);

        Book bookOrientExpress
                = new Book("Murder on the Orient Express", Year.of(2003), authorChristie, authorAkhmatova);

        Publisher publisherHarperCollins
                = new Publisher("HarperCollins", bookDonQuixote, bookOrientExpress, bookTenLittleNiggers);
        Publisher publisherPearson
                = new Publisher("Pearson Education", bookMacbeth, bookHuckleberryFinn, bookOrientExpress);

        publishers.addAll(new ArrayList<>(Arrays.asList(
                publisherHarperCollins, publisherPearson
        )));
    }
}
