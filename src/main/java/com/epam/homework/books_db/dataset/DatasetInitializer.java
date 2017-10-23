package com.epam.homework.books_db.dataset;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatasetInitializer {

    public static Dataset getExampleDataset() {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        initializeExampleDataset(authors, books, publishers);

        return new Dataset(authors, books, publishers);
    }

    public static Dataset getAnotherExampleDataset() {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        initializeAnotherExampleDataset(authors, books, publishers);

        return new Dataset(authors, books, publishers);
    }

    public static Dataset getSmallExampleDataset() {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        initializeSmallExampleDataset(authors, books, publishers);

        return new Dataset(authors, books, publishers);
    }

    private static void initializeExampleDataset(List<Author> authors, List<Book> books, List<Publisher> publishers) {
        Author authorCervantes
                = new Author("Cervantes", LocalDate.parse("1980-01-13"), Gender.MALE);
        Author authorChristie
                = new Author("Christie", LocalDate.parse("1942-05-24"), Gender.FEMALE);
        Author authorTolstoy
                = new Author("Tolstoy", LocalDate.parse("1979-12-05"), Gender.MALE);
        Author authorShakespeare
                = new Author("Shakespeare", LocalDate.parse("1958-08-15"), LocalDate.parse("2012-03-15"), Gender.MALE);
        Author authorHomer
                = new Author("Homer", LocalDate.parse("1951-03-12"), LocalDate.parse("2016-07-24"), Gender.MALE);
        Author authorAkhmatova
                = new Author("Akhmatova", LocalDate.parse("1946-09-18"), LocalDate.parse("2014-11-07"), Gender.FEMALE);
        Author authorTwain
                = new Author("Twain", LocalDate.parse("1953-03-07"), Gender.MALE);
        Author authorTsvetaeva
                = new Author("Tsvetaeva", LocalDate.parse("1953-04-25"), Gender.FEMALE);
        Author authorYesenin
                = new Author("Yesenin", LocalDate.parse("1937-01-26"), Gender.MALE);
        Author authorRowling
                = new Author("Rowling", LocalDate.parse("1955-10-15"), LocalDate.parse("2007-12-03"), Gender.FEMALE);

        authors.addAll(new ArrayList<>(Arrays.asList(
                authorCervantes, authorChristie, authorTolstoy, authorShakespeare, authorHomer,
                authorAkhmatova, authorTwain, authorTsvetaeva, authorYesenin, authorRowling
        )));

        Book bookDonQuixote
                = new Book("Don Quixote", Year.of(2015), authorCervantes);
        Book bookOrientExpress
                = new Book("Murder on the Orient Express", Year.of(2003), authorChristie, authorAkhmatova);
        Book bookWarAndPeace
                = new Book("War and Peace", Year.of(2004), authorTolstoy);
        Book bookHamlet
                = new Book("Hamlet", Year.of(1985), authorShakespeare);
        Book bookOdyssey
                = new Book("The Odyssey", Year.of(1993), authorHomer);
        Book bookPoems
                = new Book("Poems", Year.of(1973), authorAkhmatova, authorTsvetaeva, authorYesenin);
        Book bookHuckleberryFinn
                = new Book("The Adventures of Huckleberry Finn", Year.of(2007), authorTwain);
        Book bookHarryPotter
                = new Book("Harry Potter", Year.of(1997), authorRowling);
        Book bookMacbeth
                = new Book("Macbeth", Year.of(2012), authorShakespeare);
        Book bookTenLittleNiggers
                = new Book("Ten Little Niggers", Year.of(2009), authorCervantes, authorChristie);

        books.addAll(new ArrayList<>(Arrays.asList(
                bookDonQuixote, bookOrientExpress, bookWarAndPeace, bookHamlet, bookOdyssey,
                bookPoems, bookHuckleberryFinn, bookHarryPotter, bookMacbeth, bookTenLittleNiggers
        )));

        Publisher publisherHarperCollins
                = new Publisher("HarperCollins", bookDonQuixote, bookOrientExpress, bookHamlet, bookTenLittleNiggers);
        Publisher publisherMacmillan
                = new Publisher("Pan Macmillan", bookHamlet, bookOdyssey, bookPoems, bookHuckleberryFinn);
        Publisher publisherPearson
                = new Publisher("Pearson Education", bookPoems, bookHarryPotter, bookMacbeth);
        Publisher publisherOxford
                = new Publisher("Oxford University Press", bookWarAndPeace, bookHamlet, bookDonQuixote, bookMacbeth);

        publishers.addAll(new ArrayList<>(Arrays.asList(
                publisherHarperCollins, publisherMacmillan, publisherPearson, publisherOxford
        )));
    }

    private static void initializeAnotherExampleDataset(List<Author> authors, List<Book> books, List<Publisher> publishers) {
        Author authorCervantes
                = new Author("Cervantes", LocalDate.parse("1980-01-13"), Gender.MALE);
        Author authorChristie
                = new Author("Christie", LocalDate.parse("1942-05-24"), Gender.FEMALE);
        Author authorTolstoy
                = new Author("Tolstoy", LocalDate.parse("1979-12-05"), Gender.MALE);
        Author authorShakespeare
                = new Author("Shakespeare", LocalDate.parse("1958-08-15"), LocalDate.parse("2012-03-15"), Gender.MALE);
        Author authorHomer
                = new Author("Homer", LocalDate.parse("1951-03-12"), LocalDate.parse("2016-07-24"), Gender.MALE);
        Author authorTsvetaeva
                = new Author("Tsvetaeva", LocalDate.parse("1953-04-25"), Gender.FEMALE);
        Author authorYesenin
                = new Author("Yesenin", LocalDate.parse("1937-01-26"), Gender.MALE);
        Author authorRowling
                = new Author("Rowling", LocalDate.parse("1955-10-15"), LocalDate.parse("2007-12-03"), Gender.FEMALE);

        authors.addAll(new ArrayList<>(Arrays.asList(
                authorCervantes, authorChristie, authorTolstoy, authorShakespeare, authorHomer,
                authorTsvetaeva, authorYesenin, authorRowling
        )));

        Author authorTwain
                = new Author("Twain", LocalDate.parse("1953-03-07"), Gender.MALE);

        Book bookDonQuixote
                = new Book("Don Quixote", Year.of(2015), authorCervantes);
        Book bookWarAndPeace
                = new Book("War and Peace", Year.of(2004), authorTolstoy);
        Book bookOdyssey
                = new Book("The Odyssey", Year.of(1993), authorHomer);
        Book bookHuckleberryFinn
                = new Book("The Adventures of Huckleberry Finn", Year.of(2007), authorTwain);
        Book bookHarryPotter
                = new Book("Harry Potter", Year.of(1997), authorRowling);
        Book bookMacbeth
                = new Book("Macbeth", Year.of(2012), authorShakespeare);
        Book bookTenLittleNiggers
                = new Book("Ten Little Niggers", Year.of(2009), authorCervantes, authorChristie);

        books.addAll(new ArrayList<>(Arrays.asList(
                bookDonQuixote, bookWarAndPeace, bookOdyssey,
                bookHuckleberryFinn, bookHarryPotter, bookMacbeth, bookTenLittleNiggers
        )));

        Author authorAkhmatova
                = new Author("Akhmatova", LocalDate.parse("1946-09-18"), LocalDate.parse("2014-11-07"), Gender.FEMALE);

        Book bookOrientExpress
                = new Book("Murder on the Orient Express", Year.of(2003), authorChristie, authorAkhmatova);
        Book bookHamlet
                = new Book("Hamlet", Year.of(1985), authorShakespeare);
        Book bookPoems
                = new Book("Poems", Year.of(1973), authorAkhmatova, authorTsvetaeva, authorYesenin);

        Publisher publisherHarperCollins
                = new Publisher("HarperCollins", bookDonQuixote, bookOrientExpress, bookHamlet, bookTenLittleNiggers);
        Publisher publisherMacmillan
                = new Publisher("Pan Macmillan", bookHamlet, bookOdyssey, bookPoems, bookHuckleberryFinn);
        Publisher publisherPearson
                = new Publisher("Pearson Education", bookPoems, bookHarryPotter, bookMacbeth);
        Publisher publisherOxford
                = new Publisher("Oxford University Press", bookWarAndPeace, bookHamlet, bookDonQuixote, bookMacbeth);

        publishers.addAll(new ArrayList<>(Arrays.asList(
                publisherHarperCollins, publisherMacmillan, publisherPearson, publisherOxford
        )));
    }

    private static void initializeSmallExampleDataset(List<Author> authors, List<Book> books, List<Publisher> publishers) {
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
