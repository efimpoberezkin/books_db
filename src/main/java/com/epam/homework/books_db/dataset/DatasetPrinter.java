package com.epam.homework.books_db.dataset;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.util.List;

public class DatasetPrinter {

    public static void print(Dataset dataset) {
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
}
