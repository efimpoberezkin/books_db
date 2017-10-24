package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.AUTHORS_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.AUTHORS_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.BOOKS_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.BOOKS_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.PUBLISHERS_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.PUBLISHERS_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.AUTHOR_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.AUTHOR_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.BOOK_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.BOOK_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.PUBLISHER_START;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.PUBLISHER_END;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.CODE;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.NAME;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.BIRTH;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.DEATH;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.GENDER;
import static com.epam.homework.books_db.serialization.serializers.custom_serializer.StringContainer.YEAR;

class Writer {

    private static Map<Object, Integer> serializationMap;
    private static int objectCounter;
    private static String indent;

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
        out.println(indent + AUTHORS_START);
        indent += "  ";
        for (Author author : authors) {
            printAuthor(author, out);
        }
        indent = indent.substring(2);
        out.println(indent + AUTHORS_END);
    }

    private static void printAuthor(Author author, PrintWriter out) {
        out.println(indent + AUTHOR_START);
        indent += "  ";
        if (serializationMap.containsKey(author)) {
            out.println(indent + CODE + serializationMap.get(author));
        } else {
            out.println(indent + CODE + objectCounter);
            out.println(indent + NAME + author.getName());
            out.println(indent + BIRTH + author.getDateOfBirth());
            out.println(indent + DEATH + author.getDateOfDeath().orElse(null));
            out.println(indent + GENDER + author.getGender());
            serializationMap.put(author, objectCounter);
            objectCounter++;
        }
        indent = indent.substring(2);
        out.println(indent + AUTHOR_END);
    }

    private static void printBooks(List<Book> books, PrintWriter out) {
        out.println(indent + BOOKS_START);
        indent += "  ";
        for (Book book : books) {
            printBook(book, out);
        }
        indent = indent.substring(2);
        out.println(indent + BOOKS_END);
    }

    private static void printBook(Book book, PrintWriter out) {
        out.println(indent + BOOK_START);
        indent += "  ";
        if (serializationMap.containsKey(book)) {
            out.println(indent + CODE + serializationMap.get(book));
        } else {
            int bucket = objectCounter;
            objectCounter++;
            out.println(indent + CODE + bucket);
            out.println(indent + NAME + book.getName());
            out.println(indent + YEAR + book.getYearOfPublication());
            printAuthors(book.getAuthors(), out);
            serializationMap.put(book, bucket);
        }
        indent = indent.substring(2);
        out.println(indent + BOOK_END);
    }

    private static void printPublishers(List<Publisher> publishers, PrintWriter out) {
        out.println(indent + PUBLISHERS_START);
        indent += "  ";
        for (Publisher publisher : publishers) {
            printPublisher(publisher, out);
        }
        indent = indent.substring(2);
        out.println(indent + PUBLISHERS_END);
    }

    private static void printPublisher(Publisher publisher, PrintWriter out) {
        out.println(indent + PUBLISHER_START);
        indent += "  ";
        if (serializationMap.containsKey(publisher)) {
            out.println(indent + CODE + serializationMap.get(publisher));
        } else {
            int bucket = objectCounter;
            objectCounter++;
            out.println(indent + CODE + bucket);
            out.println(indent + NAME + publisher.getName());
            printBooks(publisher.getPublishedBooks(), out);
            serializationMap.put(publisher, bucket);
        }
        indent = indent.substring(2);
        out.println(indent + PUBLISHER_END);
    }
}
