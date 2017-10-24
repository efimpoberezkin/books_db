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
import java.time.format.DateTimeParseException;
import java.util.*;

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

class Reader {

    private static final String FILE_CORRUPTED = "File was corrupted";

    private static Map<Integer, Object> deserializationMap;

    static Dataset read(BufferedReader in) throws SerializerException, IOException {
        deserializationMap = new HashMap<>();

        List<Author> authors = readAuthors(in);
        List<Book> books = readBooks(in);
        List<Publisher> publishers = readPublishers(in);

        if (in.readLine() != null) throw corruptionException();

        return new Dataset(authors, books, publishers);
    }

    private static List<Author> readAuthors(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), AUTHORS_START);
        List<Author> authors = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(AUTHORS_END)) {
            authors.add(readAuthor(line, in));
        }

        return authors;
    }

    private static Author readAuthor(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, AUTHOR_START);
        Author author;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, CODE));
        } catch (NumberFormatException e) {
            throw corruptionException(e);
        }
        if (deserializationMap.containsKey(code)) {
            author = (Author) deserializationMap.get(code);
        } else {
            try {
                String name = getField(in, NAME);
                LocalDate birth = LocalDate.parse(getField(in, BIRTH));
                String deathStr = getField(in, DEATH);
                Gender gender = Gender.valueOf(getField(in, GENDER));
                if (deathStr.equals("null")) {
                    author = new Author(name, birth, gender);
                } else {
                    LocalDate death = LocalDate.parse(deathStr);
                    author = new Author(name, birth, death, gender);
                }
                deserializationMap.put(code, author);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw corruptionException(e);
            }
        }

        checkBoundary(in.readLine().trim(), AUTHOR_END);
        return author;
    }

    private static List<Book> readBooks(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), BOOKS_START);
        List<Book> books = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(BOOKS_END)) {
            books.add(readBook(line, in));
        }

        return books;
    }

    private static Book readBook(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, BOOK_START);
        Book book;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, CODE));
        } catch (NumberFormatException e) {
            throw corruptionException(e);
        }
        if (deserializationMap.containsKey(code)) {
            book = (Book) deserializationMap.get(code);
        } else {
            String name = getField(in, NAME);
            Year year;
            try {
                year = Year.parse(getField(in, YEAR));
            } catch (DateTimeParseException e) {
                throw corruptionException(e);
            }
            List<Author> bookAuthors = readAuthors(in);
            book = new Book(name, year, bookAuthors.toArray(new Author[bookAuthors.size()]));
            deserializationMap.put(code, book);
        }

        checkBoundary(in.readLine().trim(), BOOK_END);
        return book;
    }

    private static List<Publisher> readPublishers(BufferedReader in) throws SerializerException, IOException {
        checkBoundary(in.readLine().trim(), PUBLISHERS_START);
        List<Publisher> publishers = new ArrayList<>();

        String line;
        while (!(line = in.readLine().trim()).equals(PUBLISHERS_END)) {
            publishers.add(readPublisher(line, in));
        }

        return publishers;
    }

    private static Publisher readPublisher(String firstLine, BufferedReader in) throws SerializerException, IOException {
        checkBoundary(firstLine, PUBLISHER_START);
        Publisher publisher;

        Integer code;
        try {
            code = Integer.parseInt(getField(in, CODE));
        } catch (NumberFormatException e) {
            throw corruptionException(e);
        }
        if (deserializationMap.containsKey(code)) {
            publisher = (Publisher) deserializationMap.get(code);
        } else {
            String name = getField(in, NAME);
            List<Book> publisherBooks = readBooks(in);
            publisher = new Publisher(name, publisherBooks.toArray(new Book[publisherBooks.size()]));
            deserializationMap.put(code, publisher);
        }

        checkBoundary(in.readLine().trim(), PUBLISHER_END);
        return publisher;
    }

    private static SerializerException corruptionException() {
        return new SerializerException(FILE_CORRUPTED);
    }

    private static SerializerException corruptionException(Exception e) {
        return new SerializerException(FILE_CORRUPTED, e);
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
