package com.epam.homework.books_db.serialization.serializers.standard_serializer;

import com.epam.homework.books_db.dataset.Dataset;
import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;
import com.epam.homework.books_db.serialization.entity_model.AuthorEntity;
import com.epam.homework.books_db.serialization.entity_model.BookEntity;
import com.epam.homework.books_db.serialization.entity_model.PublisherEntity;

import java.util.*;
import java.util.function.Function;

class DatasetTransformer {

    static SerializableDataset transformIntoSerializable(Dataset dataset) {
        List<Author> authors = dataset.getAuthors();
        List<Book> books = dataset.getBooks();
        List<Publisher> publishers = dataset.getPublishers();

        Map<Author, AuthorEntity> authorEntityMap = new HashMap<>();
        Map<Book, BookEntity> bookEntityMap = new HashMap<>();

        Set<AuthorEntity> authorEntities = new LinkedHashSet<>();
        Set<BookEntity> bookEntities = new LinkedHashSet<>();
        Set<PublisherEntity> publisherEntities = new LinkedHashSet<>();

        Function<Author, AuthorEntity> addAuthorToMap = author ->
                new AuthorEntity(
                        author.getName(),
                        author.getDateOfBirth(),
                        author.getDateOfDeath().orElse(null),
                        author.getGender()
                );

        Function<Book, BookEntity> addBookToMap = book -> {
            List<Author> bookAuthors = book.getAuthors();
            Set<AuthorEntity> bookEntityAuthors = new LinkedHashSet<>();

            for (Author author : bookAuthors) {
                authorEntityMap.computeIfAbsent(author, addAuthorToMap);
                bookEntityAuthors.add(authorEntityMap.get(author));
            }

            return new BookEntity(
                    book.getName(),
                    book.getYearOfPublication(),
                    new ArrayList<>(bookEntityAuthors)
            );
        };

        for (Author author : authors) {
            authorEntityMap.computeIfAbsent(author, addAuthorToMap);
            authorEntities.add(authorEntityMap.get(author));
        }

        for (Book book : books) {
            bookEntityMap.computeIfAbsent(book, addBookToMap);
            bookEntities.add(bookEntityMap.get(book));
        }

        for (Publisher publisher : publishers) {
            List<Book> publisherBooks = publisher.getPublishedBooks();
            Set<BookEntity> publisherEntityBooks = new LinkedHashSet<>();

            for (Book book : publisherBooks) {
                bookEntityMap.computeIfAbsent(book, addBookToMap);
                publisherEntityBooks.add(bookEntityMap.get(book));
            }

            PublisherEntity publisherEntity = new PublisherEntity(
                    publisher.getName(),
                    new ArrayList<>(publisherEntityBooks)
            );
            publisherEntities.add(publisherEntity);
        }

        return new SerializableDataset(
                new ArrayList<>(authorEntities),
                new ArrayList<>(bookEntities),
                new ArrayList<>(publisherEntities)
        );
    }

    static Dataset transformIntoDomain(SerializableDataset serializableDataset) {
        List<AuthorEntity> authorEntities = serializableDataset.getAuthors();
        List<BookEntity> bookEntities = serializableDataset.getBooks();
        List<PublisherEntity> publisherEntities = serializableDataset.getPublishers();

        Map<AuthorEntity, Author> authorMap = new HashMap<>();
        Map<BookEntity, Book> bookMap = new HashMap<>();

        Set<Author> authors = new LinkedHashSet<>();
        Set<Book> books = new LinkedHashSet<>();
        Set<Publisher> publishers = new LinkedHashSet<>();

        Function<AuthorEntity, Author> addAuthorToMap = authorEntity ->
                new Author(
                        authorEntity.getName(),
                        authorEntity.getDateOfBirth(),
                        authorEntity.getDateOfDeath(),
                        authorEntity.getGender()
                );

        Function<BookEntity, Book> addBookToMap = bookEntity -> {
            List<AuthorEntity> bookEntityAuthors = bookEntity.getAuthors();
            Set<Author> bookAuthors = new LinkedHashSet<>();

            for (AuthorEntity authorEntity : bookEntityAuthors) {
                authorMap.computeIfAbsent(authorEntity, addAuthorToMap);
                bookAuthors.add(authorMap.get(authorEntity));
            }

            return new Book(
                    bookEntity.getName(),
                    bookEntity.getYearOfPublication(),
                    bookAuthors.toArray(new Author[bookAuthors.size()])
            );
        };

        for (AuthorEntity authorEntity : authorEntities) {
            authorMap.computeIfAbsent(authorEntity, addAuthorToMap);
            authors.add(authorMap.get(authorEntity));
        }

        for (BookEntity bookEntity : bookEntities) {
            bookMap.computeIfAbsent(bookEntity, addBookToMap);
            books.add(bookMap.get(bookEntity));
        }

        for (PublisherEntity publisherEntity : publisherEntities) {
            List<BookEntity> publisherEntityBooks = publisherEntity.getPublishedBooks();
            Set<Book> publisherBooks = new LinkedHashSet<>();

            for (BookEntity bookEntity : publisherEntityBooks) {
                bookMap.computeIfAbsent(bookEntity, addBookToMap);
                publisherBooks.add(bookMap.get(bookEntity));
            }

            Publisher publisher = new Publisher(
                    publisherEntity.getName(),
                    publisherBooks.toArray(new Book[publisherBooks.size()])
            );
            publishers.add(publisher);
        }

        return new Dataset(
                new ArrayList<>(authors),
                new ArrayList<>(books),
                new ArrayList<>(publishers)
        );
    }
}
