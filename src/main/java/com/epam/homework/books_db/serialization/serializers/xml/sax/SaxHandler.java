package com.epam.homework.books_db.serialization.serializers.xml.sax;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.AUTHORS;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.BOOKS;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.PUBLISHERS;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.AUTHOR;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.BOOK;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.PUBLISHER;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.ID;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.NAME;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.BIRTH;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.DEATH;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.GENDER;
import static com.epam.homework.books_db.serialization.serializers.xml.XmlElementNameContainer.YEAR;

class SaxHandler extends DefaultHandler {

    private Dataset dataset;

    private List<Author> authors;
    private List<Book> books;
    private List<Publisher> publishers;

    private List<Author> bookAuthors;
    private List<Book> publisherBooks;

    private String authorId;
    private String bookId;
    private String publisherId;

    private String authorName;
    private String bookName;
    private String publisherName;
    private LocalDate birth;
    private LocalDate death;
    private Gender gender;
    private Year year;

    private boolean readingBooksStarted;
    private boolean readingPublishersStarted;
    private String lastObjectTypeStarted;

    private boolean bAuthorName;
    private boolean bBookName;
    private boolean bPublisherName;
    private boolean bBirth;
    private boolean bDeath;
    private boolean bGender;
    private boolean bYear;

    private Map<String, Object> objectMap;

    public Dataset getDataset() {
        return dataset;
    }

    @Override
    public void startDocument() throws SAXException {
        objectMap = new HashMap<>();

        authors = new ArrayList<>();
        books = new ArrayList<>();
        publishers = new ArrayList<>();

        readingBooksStarted = false;
        readingPublishersStarted = false;
    }

    @Override
    public void endDocument() throws SAXException {
        dataset = new Dataset(authors, books, publishers);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (AUTHORS.equals(qName)) {
            if (readingBooksStarted) {
                bookAuthors = new ArrayList<>();
            }

        } else if (BOOKS.equals(qName)) {
            if (readingPublishersStarted) {
                publisherBooks = new ArrayList<>();
            } else {
                readingBooksStarted = true;
            }

        } else if (PUBLISHERS.equals(qName)) {
            readingPublishersStarted = true;

        } else if (AUTHOR.equals(qName)) {
            authorId = attributes.getValue(ID);
            lastObjectTypeStarted = AUTHOR;

        } else if (BOOK.equals(qName)) {
            bookId = attributes.getValue(ID);
            lastObjectTypeStarted = BOOK;

        } else if (PUBLISHER.equals(qName)) {
            publisherId = attributes.getValue(ID);
            lastObjectTypeStarted = PUBLISHER;

        } else if (NAME.equals(qName)) {
            switch (lastObjectTypeStarted) {
                case AUTHOR:
                    bAuthorName = true;
                    break;
                case BOOK:
                    bBookName = true;
                    break;
                case PUBLISHER:
                    bPublisherName = true;
                    break;
                default:
                    break;
            }
            lastObjectTypeStarted = null;

        } else if (BIRTH.equals(qName)) {
            bBirth = true;

        } else if (DEATH.equals(qName)) {
            bDeath = true;

        } else if (GENDER.equals(qName)) {
            bGender = true;

        } else if (YEAR.equals(qName)) {
            bYear = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (AUTHOR.equals(qName)) {
            Author author;
            if (objectMap.containsKey(authorId)) {
                author = (Author) objectMap.get(authorId);
            } else {
                author = new Author(authorName, birth, death, gender);
                objectMap.put(authorId, author);

                authorName = null;
                birth = null;
                death = null;
                gender = null;
            }
            authorId = null;
            if (!readingBooksStarted) {
                authors.add(author);
            } else {
                bookAuthors.add(author);
            }

        } else if (BOOK.equals(qName)) {
            Book book;
            if (objectMap.containsKey(bookId)) {
                book = (Book) objectMap.get(bookId);
            } else {
                book = new Book(bookName, year, bookAuthors.toArray(new Author[bookAuthors.size()]));
                objectMap.put(bookId, book);

                bookName = null;
                year = null;
            }
            bookId = null;
            if (!readingPublishersStarted) {
                books.add(book);
            } else {
                publisherBooks.add(book);
            }

        } else if (PUBLISHER.equals(qName)) {
            Publisher publisher;
            if (objectMap.containsKey(publisherId)) {
                publisher = (Publisher) objectMap.get(publisherId);
            } else {
                publisher = new Publisher(publisherName, publisherBooks.toArray(new Book[publisherBooks.size()]));
                objectMap.put(publisherId, publisher);

                publisherName = null;
            }
            publisherId = null;
            publishers.add(publisher);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bAuthorName) {
            authorName = new String(ch, start, length);
            bAuthorName = false;

        } else if (bBirth) {
            birth = LocalDate.parse(new String(ch, start, length));
            bBirth = false;

        } else if (bDeath) {
            death = LocalDate.parse(new String(ch, start, length));
            bDeath = false;

        } else if (bGender) {
            gender = Gender.valueOf(new String(ch, start, length));
            bGender = false;

        } else if (bBookName) {
            bookName = new String(ch, start, length);
            bBookName = false;

        } else if (bYear) {
            year = Year.parse(new String(ch, start, length));
            bYear = false;

        } else if (bPublisherName) {
            publisherName = new String(ch, start, length);
            bPublisherName = false;
        }
    }
}
