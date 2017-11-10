package com.epam.homework.books_db.postgresql;

import java.util.List;

interface DAO<T> {

    int add(T object) throws DAOException;

    List<Integer> addAll(List<T> objects) throws DAOException;

    T get(int id) throws DAOException;

    List<T> getAll() throws DAOException;

    void delete(int id) throws DAOException;
}
