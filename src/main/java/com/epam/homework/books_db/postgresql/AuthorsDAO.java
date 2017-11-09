package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Gender;

import java.time.LocalDate;
import java.util.List;

public interface AuthorsDAO extends DAO<Author> {

    @Override
    int add(Author author) throws DAOException;

    @Override
    List<Integer> addAll(List<Author> authors) throws DAOException;

    @Override
    Author get(int id) throws DAOException;

    @Override
    List<Author> getAll() throws DAOException;

    void update(int id, String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender) throws DAOException;

    @Override
    void delete(int id) throws DAOException;
}
