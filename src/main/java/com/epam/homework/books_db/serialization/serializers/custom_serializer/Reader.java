package com.epam.homework.books_db.serialization.serializers.custom_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;
import com.epam.homework.books_db.serialization.serializers.SerializerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

class Reader {

    private static final String FILE_CORRUPTED = "File corrupted";

    private static Map<Integer, Object> deserializationMap;

    static Dataset read(BufferedReader in) throws SerializerException, IOException {
        deserializationMap = new HashMap<>();

        List<Author> authors = readAuthors(in);
        List<Book> books = readBooks(in);
        List<Publisher> publishers = readPublishers(in);

        return new Dataset(authors, books, publishers);
    }

    private static List<Author> readAuthors(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), StringContainer.AUTHORS_START);
        List<Author> authors = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(StringContainer.AUTHORS_END)) {
            authors.add(readAuthor(line, in));
        }

        return authors;
    }

    private static Author readAuthor(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, StringContainer.AUTHOR_START);
        Author author;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, StringContainer.CODE));
        } catch (NumberFormatException e) {
            throw new SerializerException(FILE_CORRUPTED, e);
        }
        if (deserializationMap.containsKey(code)) {
            author = (Author) deserializationMap.get(code);
        } else {
            String name = getField(in, StringContainer.NAME);
            LocalDate birth = LocalDate.parse(getField(in, StringContainer.BIRTH));
            String deathStr = getField(in, StringContainer.DEATH);
            Gender gender = Gender.valueOf(getField(in, StringContainer.GENDER));
            if (deathStr.equals("null")) {
                author = new Author(name, birth, gender);
            } else {
                LocalDate death = LocalDate.parse(deathStr);
                author = new Author(name, birth, death, gender);
            }
            deserializationMap.put(code, author);
        }

        checkBoundary(in.readLine().trim(), StringContainer.AUTHOR_END);
        return author;
    }

    private static List<Book> readBooks(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), StringContainer.BOOKS_START);
        List<Book> books = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(StringContainer.BOOKS_END)) {
            books.add(readBook(line, in));
        }

        return books;
    }

    private static Book readBook(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, StringContainer.BOOK_START);
        Book book;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, StringContainer.CODE));
        } catch (NumberFormatException e) {
            throw new SerializerException(FILE_CORRUPTED, e);
        }
        if (deserializationMap.containsKey(code)) {
            book = (Book) deserializationMap.get(code);
        } else {
            String name = getField(in, StringContainer.NAME);
            Year year = Year.parse(getField(in, StringContainer.YEAR));
            List<Author> bookAuthors = readAuthors(in);
            book = new Book(name, year, bookAuthors.toArray(new Author[bookAuthors.size()]));
            deserializationMap.put(code, book);
        }

        checkBoundary(in.readLine().trim(), StringContainer.BOOK_END);
        return book;
    }

    private static List<Publisher> readPublishers(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), StringContainer.PUBLISHERS_START);
        List<Publisher> publishers = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(StringContainer.PUBLISHERS_END)) {
            publishers.add(readPublisher(line, in));
        }

        return publishers;
    }

    private static Publisher readPublisher(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, StringContainer.PUBLISHER_START);
        Publisher publisher;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, StringContainer.CODE));
        } catch (NumberFormatException e) {
            throw new SerializerException(FILE_CORRUPTED, e);
        }
        if (deserializationMap.containsKey(code)) {
            publisher = (Publisher) deserializationMap.get(code);
        } else {
            String name = getField(in, StringContainer.NAME);
            List<Book> publisherBooks = readBooks(in);
            publisher = new Publisher(name, publisherBooks.toArray(new Book[publisherBooks.size()]));
            deserializationMap.put(code, publisher);
        }

        checkBoundary(in.readLine().trim(), StringContainer.PUBLISHER_END);
        return publisher;
    }

    private static SerializerException corruptionException() {
        return new SerializerException(FILE_CORRUPTED);
    }

    private static void checkBoundary(String line, String requiredBoundary) throws SerializerException {
        if (!line.equals(requiredBoundary)) throw corruptionException();
    }

    private static String getField(BufferedReader in, String fieldName) throws SerializerException, IOException {
        String line = in.readLine().trim();
        if (!line.startsWith(fieldName)) throw corruptionException();
        return line.substring(fieldName.length());
    }
}
