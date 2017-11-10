package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Book;

import java.time.Year;
import java.util.List;

interface BooksDAO extends DAO<Book> {

    @Override
    int add(Book book) throws DAOException;

    @Override
    List<Integer> addAll(List<Book> books) throws DAOException;

    @Override
    Book get(int id) throws DAOException;

    @Override
    List<Book> getAll() throws DAOException;

    void update(int id, String name, Year yearOfPublication) throws DAOException;

    @Override
    void delete(int id) throws DAOException;
}
