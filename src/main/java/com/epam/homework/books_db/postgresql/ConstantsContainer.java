package com.epam.homework.books_db.postgresql;

final class ConstantsContainer {

    // database specific
    static final String URL = "jdbc:postgresql://localhost:5432/";
    static final String DB_NAME = "booksdb";

    static final String USER = "postgres";
    static final String PASSWORD = "123";

    // tables
    static final String AUTHOR = "author";
    static final String BOOK = "book";
    static final String PUBLISHER = "publisher";
    static final String BOOK_AUTHOR = "book_author";
    static final String PUBLISHER_BOOK = "publisher_book";

    // constraints
    static final String AUTHOR_UQ = "author_uq";
    static final String BOOK_UQ = "book_uq";
    static final String PUBLISHER_UQ = "publisher_uq";
    static final String BOOK_AUTHOR_UQ = "book_author_uq";
    static final String PUBLISHER_BOOK_UQ = "publisher_book_uq";

    // fields
    static final String ID = "id";
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
