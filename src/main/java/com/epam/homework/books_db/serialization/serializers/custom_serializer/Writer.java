package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Writer {

    static private Map<Object, Integer> serializationMap;
    static private int objectCounter;
    static private String indent;

    static void write(Dataset dataset, PrintWriter out) {
        serializationMap = new HashMap<>();
        objectCounter = 0;
        indent = "";

        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        printAuthors(authors, out);
        printBooks(books, out);
        printPublishers(publishers, out);
    }

    private static void printAuthors(List<Author> authors, PrintWriter out) {
        out.println(indent + StringContainer.AUTHORS_START);
        indent += "  ";
        for (Author author : authors) {
            printAuthor(author, out);
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.AUTHORS_END);
    }

    private static void printAuthor(Author author, PrintWriter out) {
        out.println(indent + StringContainer.AUTHOR_START);
        indent += "  ";
        if (serializationMap.containsKey(author)) {
            out.println(indent + StringContainer.CODE + serializationMap.get(author));
        } else {
            out.println(indent + StringContainer.CODE + objectCounter);
            out.println(indent + StringContainer.NAME + author.getName());
            out.println(indent + StringContainer.BIRTH + author.getDateOfBirth());
            out.println(indent + StringContainer.DEATH + author.getDateOfDeath().orElse(null));
            out.println(indent + StringContainer.GENDER + author.getGender());
            serializationMap.put(author, objectCounter);
            objectCounter++;
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.AUTHOR_END);
    }

    private static void printBooks(List<Book> books, PrintWriter out) {
        out.println(indent + StringContainer.BOOKS_START);
        indent += "  ";
        for (Book book : books) {
            printBook(book, out);
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.BOOKS_END);
    }

    private static void printBook(Book book, PrintWriter out) {
        out.println(indent + StringContainer.BOOK_START);
        indent += "  ";
        if (serializationMap.containsKey(book)) {
            out.println(indent + StringContainer.CODE + serializationMap.get(book));
        } else {
            out.println(indent + StringContainer.CODE + objectCounter);
            out.println(indent + StringContainer.NAME + book.getName());
            out.println(indent + StringContainer.YEAR + book.getYearOfPublication());
            printAuthors(book.getAuthors(), out);
            serializationMap.put(book, objectCounter);
            objectCounter++;
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.BOOK_END);
    }

    private static void printPublishers(List<Publisher> publishers, PrintWriter out) {
        out.println(indent + StringContainer.PUBLISHERS_START);
        indent += "  ";
        for (Publisher publisher : publishers) {
            printPublisher(publisher, out);
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.PUBLISHERS_END);
    }

    private static void printPublisher(Publisher publisher, PrintWriter out) {
        out.println(indent + StringContainer.PUBLISHER_START);
        indent += "  ";
        if (serializationMap.containsKey(publisher)) {
            out.println(indent + StringContainer.CODE + serializationMap.get(publisher));
        } else {
            out.println(indent + StringContainer.CODE + objectCounter);
            out.println(indent + StringContainer.NAME + publisher.getName());
            printBooks(publisher.getPublishedBooks(), out);
            serializationMap.put(publisher, objectCounter);
            objectCounter++;
        }
        indent = indent.substring(2);
        out.println(indent + StringContainer.PUBLISHER_END);
    }
}
