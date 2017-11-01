package com.epam.homework.books_db.logic;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class LogicTest {

    private static List<Author> authors;
    private static List<Book> books;

    @BeforeClass
    public static void initializeData() {
        authors = new ArrayList<>();
        books = new ArrayList<>();

        formData(authors, books);
    }

    @Test
    public void tstAverageAuthorAge() {
        assertEquals(59.5, Logic.calculateAverageAuthorAge(authors), 0);
    }

    @Test
    public void tstAuthorsSortedByAge() {
        Author[] expected = new Author[]{
                authors.get(0), authors.get(2), authors.get(9), authors.get(3), authors.get(7),
                authors.get(6), authors.get(4), authors.get(5), authors.get(1), authors.get(8)
        };
        Author[] actual = Logic.getAuthorsSortedByAge(authors).toArray(new Author[authors.size()]);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void tstRetiredAuthors() {
        List<Author> expected = new ArrayList<>(Arrays.asList(authors.get(1), authors.get(7), authors.get(8)));
        expected.sort(Comparator.comparing(Author::getName));

        List<Author> actual = Logic.getRetiredAuthors(authors);
        actual.sort(Comparator.comparing(Author::getName));

        assertEquals(expected, actual);
    }

    @Test
    public void tstAgesOfBooks() {
        Map<String, Long> expected = new HashMap<>();
        books.forEach(book -> expected.put(
                book.getName(),
                ChronoUnit.YEARS.between(book.getYearOfPublication(), Year.now()))
        );

        assertEquals(expected, Logic.getAgesOfBooks(books));
    }

    @Test
    public void tstAuthorsWhoCooped() {
        List<Author> expected = new ArrayList<>(Arrays.asList(
                authors.get(0), authors.get(1), authors.get(5), authors.get(7), authors.get(8))
        );
        expected.sort(Comparator.comparing(Author::getName));

        List<Author> actual = Logic.getAuthorsWhoCooped(books);
        actual.sort(Comparator.comparing(Author::getName));

        assertEquals(expected, actual);
    }

    @Test
    public void tstBooksByAuthors() {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put(authors.get(0).getName(), Arrays.asList(books.get(0).getName(), books.get(9).getName()));
        expected.put(authors.get(1).getName(), Arrays.asList(books.get(1).getName(), books.get(9).getName()));
        expected.put(authors.get(2).getName(), Collections.singletonList(books.get(2).getName()));
        expected.put(authors.get(3).getName(), Arrays.asList(books.get(3).getName(), books.get(8).getName()));
        expected.put(authors.get(4).getName(), Collections.singletonList(books.get(4).getName()));
        expected.put(authors.get(5).getName(), Arrays.asList(books.get(1).getName(), books.get(5).getName()));
        expected.put(authors.get(6).getName(), Collections.singletonList(books.get(6).getName()));
        expected.put(authors.get(7).getName(), Collections.singletonList(books.get(5).getName()));
        expected.put(authors.get(8).getName(), Collections.singletonList(books.get(5).getName()));
        expected.put(authors.get(9).getName(), Collections.singletonList(books.get(7).getName()));

        assertEquals(expected, Logic.getBooksByAuthors(books));
    }

    @AfterClass
    public static void destroyExampleDataset() {
        authors = null;
        books = null;
    }

    private static void formData(List<Author> authors, List<Book> books) {
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
    }
}
