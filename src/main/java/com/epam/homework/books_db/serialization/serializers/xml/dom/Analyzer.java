package com.epam.homework.books_db.serialization.serializers.xml.dom;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Gender;
import com.epam.homework.books_db.model.Publisher;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Analyzer {

    private Map<String, Object> objectMap;

    Dataset buildDataset(Element root) throws SAXException, IOException {
        objectMap = new HashMap<>();

        List<Author> authors = getAuthors(getBaby(root, "authors"));
        List<Book> books = getBooks(getBaby(root, "books"));
        List<Publisher> publishers = getPublishers(getBaby(root, "publishers"));

        return new Dataset(authors, books, publishers);
    }

    private List<Author> getAuthors(Element root) {
        List<Author> authors = new ArrayList<>();
        NodeList authorElements = root.getElementsByTagName("author");

        for (int i = 0; i < authorElements.getLength(); i++) {
            Element authorElement = (Element) authorElements.item(i);
            String id = authorElement.getAttribute("id");

            Author author;
            if (objectMap.containsKey(id)) {
                author = (Author) objectMap.get(id);
            } else {
                author = getAuthor(authorElement);
                objectMap.put(id, author);
            }
            authors.add(author);
        }

        return authors;
    }

    private Author getAuthor(Element author) {
        String name = getBabyValue(author, "name");
        LocalDate birth = LocalDate.parse(getBabyValue(author, "birth"));
        LocalDate death = null;
        if (hasBaby(author, "death")) {
            death = LocalDate.parse(getBabyValue(author, "death"));
        }
        Gender gender = Gender.valueOf(getBabyValue(author, "gender"));

        return new Author(name, birth, death, gender);
    }

    private List<Book> getBooks(Element root) {
        List<Book> books = new ArrayList<>();
        NodeList bookElements = root.getElementsByTagName("book");

        for (int i = 0; i < bookElements.getLength(); i++) {
            Element bookElement = (Element) bookElements.item(i);
            String id = bookElement.getAttribute("id");

            Book book;
            if (objectMap.containsKey(id)) {
                book = (Book) objectMap.get(id);
            } else {
                book = getBook(bookElement);
                objectMap.put(id, book);
            }
            books.add(book);
        }

        return books;
    }

    private Book getBook(Element book) {
        String name = getBabyValue(book, "name");
        Year year = Year.parse(getBabyValue(book, "year"));
        List<Author> bookAuthors = getAuthors(getBaby(book, "authors"));

        return new Book(name, year, bookAuthors.toArray(new Author[bookAuthors.size()]));
    }

    private List<Publisher> getPublishers(Element root) {
        List<Publisher> publishers = new ArrayList<>();
        NodeList publisherElements = root.getElementsByTagName("publisher");

        for (int i = 0; i < publisherElements.getLength(); i++) {
            Element publisherElement = (Element) publisherElements.item(i);
            String id = publisherElement.getAttribute("id");

            Publisher publisher;
            if (objectMap.containsKey(id)) {
                publisher = (Publisher) objectMap.get(id);
            } else {
                publisher = getPublisher(publisherElement);
                objectMap.put(id, publisher);
            }
            publishers.add(publisher);
        }

        return publishers;
    }

    private Publisher getPublisher(Element publisher) {
        String name = getBabyValue(publisher, "name");
        List<Book> publisherBooks = getBooks(getBaby(publisher, "books"));

        return new Publisher(name, publisherBooks.toArray(new Book[publisherBooks.size()]));
    }

    private String getBabyValue(Element parent, String childName) {
        Element child = getBaby(parent, childName);
        Node node = child.getFirstChild();
        return node.getNodeValue();
    }

    private Element getBaby(Element parent, String childName) {
        NodeList nodes = parent.getElementsByTagName(childName);
        return (Element) nodes.item(0);
    }

    private boolean hasBaby(Element parent, String childName) {
        NodeList nodes = parent.getElementsByTagName(childName);
        return nodes.getLength() != 0;
    }
}
