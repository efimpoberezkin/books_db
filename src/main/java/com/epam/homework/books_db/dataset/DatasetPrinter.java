package com.epam.homework.books_db.dataset;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.util.List;

public class DatasetPrinter {

    private static String indent;

    public static void basicPrint(Dataset dataset) {
        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        System.out.println("\n-- Authors --");
        authors.forEach(System.out::println);

        System.out.println("\n-- Books --");
        books.forEach(System.out::println);

        System.out.println("\n-- Publishers --");
        publishers.forEach(System.out::println);
    }

    public static void customPrint(Dataset dataset) {
        indent = "";

        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        System.out.println("\n-- Authors --");
        authors.forEach(DatasetPrinter::printAuthor);

        System.out.println("\n-- Books --");
        books.forEach(DatasetPrinter::printBook);

        System.out.println("\n-- Publishers --");
        publishers.forEach(DatasetPrinter::printPublisher);
    }

    private static void printAuthor(Author author) {
        System.out.println(indent + "author");
        indent += "  ";
        System.out.println(indent + "name: " + author.getName());
        System.out.println(indent + "date of birth: " + author.getDateOfBirth());
        author.getDateOfDeath().ifPresent(date -> System.out.println(indent + "date of death:" + date));
        System.out.println(indent + "gender: " + author.getGender());
        indent = indent.substring(2);
    }

    private static void printBook(Book book) {
        System.out.println(indent + "book");
        indent += "  ";
        System.out.println(indent + "name: " + book.getName());
        System.out.println(indent + "year of publication: " + book.getYearOfPublication());
        book.getAuthors().forEach(DatasetPrinter::printAuthor);
        indent = indent.substring(2);
    }

    private static void printPublisher(Publisher publisher) {
        System.out.println(indent + "publisher");
        indent += "  ";
        System.out.println(indent + "name: " + publisher.getName());
        publisher.getPublishedBooks().forEach(DatasetPrinter::printBook);
        indent = indent.substring(2);
    }
}
