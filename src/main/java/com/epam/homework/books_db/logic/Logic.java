package com.epam.homework.books_db.logic;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Logic {

    public static double calculateAverageAuthorAge(List<Author> authors) {

        double averageAge =
                authors.stream()
                        .mapToLong(author -> ChronoUnit.YEARS.between(
                                author.getDateOfBirth(),
                                author.getDateOfDeath().orElse(LocalDate.now())))
                        .average()
                        .getAsDouble();

        return averageAge;
    }

    public static List<Author> getAuthorsSortedByAge(List<Author> authors) {
        List<Author> authorsToSort =
                authors.stream()
                        .sorted(Comparator.comparing(author -> ChronoUnit.DAYS.between(
                                author.getDateOfBirth(),
                                author.getDateOfDeath().orElse(LocalDate.now()))))
                        .collect(Collectors.toList());

        return authorsToSort;
    }

    public static List<Author> getRetiredAuthors(List<Author> authors) {
        Predicate<Author> maleRetired = author -> (
                author.getGender() == Gender.MALE
                        && ChronoUnit.YEARS.between(author.getDateOfBirth(), LocalDate.now()) >= 65
        );

        Predicate<Author> femaleRetired = author -> (
                author.getGender() == Gender.FEMALE
                        && ChronoUnit.YEARS.between(author.getDateOfBirth(), LocalDate.now()) >= 63
        );

        List<Author> retired =
                authors.stream()
                        .filter(author -> !author.getDateOfDeath().isPresent())
                        .filter(maleRetired.or(femaleRetired))
                        .collect(Collectors.toList());

        return retired;
    }

    public static Map<String, Long> getAgesOfBooks(List<Book> books) {
        Map<String, Long> agesOfBooksMap =
                books.stream()
                        .collect(Collectors.toMap(
                                Book::getName,
                                (Book b) -> ChronoUnit.YEARS.between(b.getYearOfPublication(), Year.now()))
                        );

        return agesOfBooksMap;
    }

    public static List<Author> getAuthorsWhoCooped(List<Book> books) {
        List<Author> authorsWhoCooped =
                books.stream()
                        .filter(book -> book.getAuthors().size() > 1)
                        .flatMap(book -> book.getAuthors().stream())
                        .distinct()
                        .collect(Collectors.toList());

        return authorsWhoCooped;
    }

    public static Map<String, List<String>> getBooksByAuthors(List<Book> books) {
        Map<String, List<String>> booksByAuthor =
                books.stream()
                        .flatMap(book ->
                                book.getAuthors().stream()
                                        .map(author ->
                                                new AbstractMap.SimpleEntry<>(
                                                        author.getName(),
                                                        book.getName())))
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.mapping(
                                        Map.Entry::getValue,
                                        Collectors.toList())));

        return booksByAuthor;
    }
}
