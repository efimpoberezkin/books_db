package com.epam.homework.books_db.dataset;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;
import org.apache.log4j.Logger;

import java.util.List;

public class DatasetPrinter {

    private static Logger log = Logger.getRootLogger();

    public void basicPrint(Dataset dataset) {
        log.info("Printing dataset via basic print...");

        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        System.out.println("\n-- Authors --");
        authors.forEach(System.out::println);

        System.out.println("\n-- Books --");
        books.forEach(System.out::println);

        System.out.println("\n-- Publishers --");
        publishers.forEach(System.out::println);

        System.out.println();

        log.info("Dataset printed");
    }

    public void customPrint(Dataset dataset) {
        log.info("Printing dataset via custom print...");

        String indent = "";

        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        System.out.println("\n-- Authors --\n");
        authors.forEach(author -> printAuthor(author, indent));

        System.out.println("\n-- Books --\n");
        books.forEach(book -> printBook(book, indent));

        System.out.println("\n-- Publishers --\n");
        publishers.forEach(publisher -> printPublisher(publisher, indent));

        System.out.println();

        log.info("Dataset printed");
    }

    private void printAuthor(Author author, String indent) {
        System.out.println(indent + "author");
        indent = increaseIndent(indent);
        System.out.println(indent + "name: " + author.getName());
        System.out.println(indent + "date of birth: " + author.getDateOfBirth());
        if (author.getDateOfDeath().isPresent()) {
            System.out.println(indent + "date of death: " + author.getDateOfDeath());
        }
        System.out.println(indent + "gender: " + author.getGender());
        indent = decreaseIndent(indent);
    }

    private void printBook(Book book, String indent) {
        System.out.println(indent + "book");
        indent = increaseIndent(indent);
        System.out.println(indent + "name: " + book.getName());
        System.out.println(indent + "year of publication: " + book.getYearOfPublication());
        for (Author author : book.getAuthors()) {
            printAuthor(author, indent);
        }
        indent = decreaseIndent(indent);
    }

    private void printPublisher(Publisher publisher, String indent) {
        System.out.println(indent + "publisher");
        indent = increaseIndent(indent);
        System.out.println(indent + "name: " + publisher.getName());
        for (Book book : publisher.getPublishedBooks()) {
            printBook(book, indent);
        }
        indent = decreaseIndent(indent);
    }

    private String increaseIndent(String indent) {
        return indent + "  ";
    }

    private String decreaseIndent(String indent) {
        return indent.substring(2);
    }
}
