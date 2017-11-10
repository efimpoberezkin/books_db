package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Publisher;

import java.util.List;

interface PublishersDAO extends DAO<Publisher> {

    @Override
    int add(Publisher publisher) throws DAOException;

    @Override
    List<Integer> addAll(List<Publisher> publishers) throws DAOException;

    @Override
    Publisher get(int id) throws DAOException;

    @Override
    List<Publisher> getAll() throws DAOException;

    void update(int id, String name) throws DAOException;

    @Override
    void delete(int id) throws DAOException;
}
