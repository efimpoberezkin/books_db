package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Book;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

public class BooksDAOImpl implements BooksDAO {

    /**
     * @return generated id
     */
    @Override
    public int add(Book book) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            return baseAdd(book, con);
        } catch (SQLException e) {
            throw new DAOException("Failed to add book", e);
        }
    }

    /**
     * @return list of generated ids
     */
    @Override
    public List<Integer> addAll(List<Book> books) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            List<Integer> generatedIds = new ArrayList<>();
            for (Book book : books) {
                int id = baseAdd(book, con);
                generatedIds.add(id);
            }
            return generatedIds;
        } catch (SQLException e) {
            throw new DAOException("Failed to add authors", e);
        }
    }

    @Override
    public Book get(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGet(con, id);
             ResultSet rs = stmt.executeQuery()) {

            Book book = null;
            if (rs.next()) {
                String name = rs.getString(NAME);
                Year yearOfPublication = Year.of(rs.getInt(YEAR_OF_PUBLICATION));
                List<Author> authors = getBookAuthors(con, id);

                book = new Book(name, yearOfPublication, authors.toArray(new Author[authors.size()]));
            }
            return book;

        } catch (SQLException e) {
            throw new DAOException("Failed to get book", e);
        }
    }

    @Override
    public List<Book> getAll() throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGetAll(con);
             ResultSet rs = stmt.executeQuery()) {

            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(ID);
                String name = rs.getString(NAME);
                Year yearOfPublication = Year.of(rs.getInt(YEAR_OF_PUBLICATION));
                List<Author> authors = getBookAuthors(con, id);

                Book book = new Book(name, yearOfPublication, authors.toArray(new Author[authors.size()]));
                books.add(book);
            }
            return books;

        } catch (SQLException e) {
            throw new DAOException("Failed to get books", e);
        }
    }

    @Override
    public void update(int id, String name, Year yearOfPublication) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            String sql = createSqlForUpdate(id, name, yearOfPublication);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DAOException("Failed to update book", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            String format = "DELETE FROM %s WHERE %s=%s";
            String sql = String.format(format, BOOK_AUTHOR, BOOK_ID, id);
            stmt.executeUpdate(sql);

            format = "DELETE FROM %s WHERE %s=%s";
            sql = String.format(format, PUBLISHER_BOOK, BOOK_ID, id);
            stmt.executeUpdate(sql);

            format = "DELETE FROM %s WHERE %s=%s";
            sql = String.format(format, BOOK, ID, id);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DAOException("Failed to delete book", e);
        }
    }

    private int baseAdd(Book book, Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createSqlForAdd(book), Statement.RETURN_GENERATED_KEYS);
            int id = -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

            List<Integer> generatedKeys = new AuthorsDAOImpl().addAll(book.getAuthors());
            String format = "INSERT INTO %s (%s, %s) VALUES (%s, ?)"
                    + "ON CONFLICT ON CONSTRAINT %s DO NOTHING";
            String sql = String.format(format, BOOK_AUTHOR, BOOK_ID, AUTHOR_ID, id, BOOK_AUTHOR_UQ);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (int key : generatedKeys) {
                    ps.setInt(1, key);
                    ps.executeUpdate();
                }
            }

            return id;
        }
    }

    private String createSqlForAdd(Book book) {
        String name = "\'" + book.getName() + "\'";
        Year yearOfPublication = book.getYearOfPublication();

        // updating so that RETURNING works
        // adding ;-- so that Statement doesn't break RETURNING option
        String format = "INSERT INTO %s (%s, %s) VALUES (%s, %s)"
                + "ON CONFLICT ON CONSTRAINT %s DO UPDATE SET %s=%s RETURNING %s;--";

        return String.format(format, BOOK, NAME, YEAR_OF_PUBLICATION,
                name, yearOfPublication, BOOK_UQ, NAME, name, ID);
    }

    private PreparedStatement createPreparedStatementForGet(Connection con, int id) throws SQLException {
        String format = "SELECT %s, %s FROM %s WHERE %s=%s";
        String sql = String.format(format, NAME, YEAR_OF_PUBLICATION, BOOK, ID, id);
        return con.prepareStatement(sql);
    }

    private List<Author> getBookAuthors(Connection con, int bookId) throws SQLException {
        try (PreparedStatement stmt = createPreparedStatementForGetBookAuthors(con, bookId);
             ResultSet rs = stmt.executeQuery()) {

            List<Integer> keys = new ArrayList<>();
            while (rs.next()) {
                int key = rs.getInt(ID);
                keys.add(key);
            }

            List<Author> authors = new ArrayList<>();
            for (Integer key : keys) {
                Author author = new AuthorsDAOImpl().get(key);
                authors.add(author);
            }
            return authors;

        }
    }

    private PreparedStatement createPreparedStatementForGetBookAuthors(Connection con, int bookId) throws SQLException {
        String format = "SELECT a.%s FROM %s AS a JOIN %s AS ba ON a.%s=ba.%s WHERE ba.%s=%s ";
        String sql = String.format(format, ID, AUTHOR, BOOK_AUTHOR, ID, AUTHOR_ID, BOOK_ID, bookId);
        return con.prepareStatement(sql);
    }

    private PreparedStatement createPreparedStatementForGetAll(Connection con) throws SQLException {
        String format = "SELECT %s, %s, %s FROM %s";
        String sql = String.format(format, ID, NAME, YEAR_OF_PUBLICATION, BOOK);
        return con.prepareStatement(sql);
    }

    private String createSqlForUpdate(int id, String name, Year yearOfPublication) {
        String nameStr = "\'" + name + "\'";
        String format = "UPDATE %s SET %s=%s, %s=%s WHERE %s=%s";

        return String.format(format, BOOK, NAME, nameStr, YEAR_OF_PUBLICATION, yearOfPublication, ID, id);
    }
}
