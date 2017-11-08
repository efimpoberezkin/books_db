package com.epam.homework.books_db.postgresql;

final class ConstantsContainer {

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost:5432/";
    static final String DB_NAME = "booksdb";

    static final String USER = "postgres";
    static final String PASSWORD = "123";

    static final String DATASET = "dataset";
    static final String AUTHOR = "author";
    static final String BOOK = "book";
    static final String PUBLISHER = "publisher";
    static final String DATASETS_AUTHORS = "datasetsAuthors";
    static final String DATASETS_BOOKS = "datasetsBooks";
    static final String DATASETS_PUBLISHERS = "datasetsPublishers";
    static final String BOOK_AUTHOR = "book_author";
    static final String PUBLISHER_BOOK = "publisher_book";
    static final String ID = "id";
    static final String DATASET_ID = "datasetId";
    static final String AUTHOR_ID = "author_id";
    static final String BOOK_ID = "book_id";
    static final String PUBLISHER_ID = "publisher_id";
    static final String GENDER_ID = "gender_id";
    static final String NAME = "name";
    static final String DATE_OF_BIRTH = "date_of_birth";
    static final String DATE_OF_DEATH = "date_of_death";
    static final String YEAR_OF_PUBLICATION = "year_of_publication";
    static final int MALE_ID = 1;
    static final int FEMALE_ID = 2;

    private ConstantsContainer() {
    }
}
