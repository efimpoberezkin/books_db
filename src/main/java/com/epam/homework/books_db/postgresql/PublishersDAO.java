package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Book;
import com.epam.homework.books_db.model.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

public class PublishersDAO {

    /**
     * @return generated id
     */
    public int add(Publisher publisher) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            return baseAdd(publisher, con);
        } catch (SQLException e) {
            throw new DAOException("Failed to add publisher", e);
        }
    }

    /**
     * @return list of generated ids
     */
    public List<Integer> addAll(List<Publisher> publishers) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            List<Integer> generatedIds = new ArrayList<>();
            for (Publisher publisher : publishers) {
                int id = baseAdd(publisher, con);
                generatedIds.add(id);
            }
            return generatedIds;
        } catch (SQLException e) {
            throw new DAOException("Failed to add authors", e);
        }
    }

    public Publisher get(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGet(con, id);
             ResultSet rs = stmt.executeQuery()) {

            Publisher publisher = null;
            if (rs.next()) {
                String name = rs.getString(NAME);
                List<Book> books = getPublisherBooks(con, id);

                publisher = new Publisher(name, books.toArray(new Book[books.size()]));
            }
            return publisher;

        } catch (SQLException e) {
            throw new DAOException("Failed to get publisher", e);
        }
    }

    public List<Publisher> getAll() throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGetAll(con);
             ResultSet rs = stmt.executeQuery()) {

            List<Publisher> publishers = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(ID);
                String name = rs.getString(NAME);
                List<Book> books = getPublisherBooks(con, id);

                Publisher publisher = new Publisher(name, books.toArray(new Book[books.size()]));
                publishers.add(publisher);
            }
            return publishers;

        } catch (SQLException e) {
            throw new DAOException("Failed to get publishers", e);
        }
    }

    public void update(int id, String name) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            String sql = createSqlForUpdate(id, name);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DAOException("Failed to update publisher", e);
        }
    }

    public void delete(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            String format = "DELETE FROM %s WHERE %s=%s";
            String sql = String.format(format, PUBLISHER_BOOK, PUBLISHER_ID, id);
            stmt.executeUpdate(sql);

            format = "DELETE FROM %s WHERE %s=%s";
            sql = String.format(format, PUBLISHER, ID, id);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DAOException("Failed to delete publisher", e);
        }
    }

    private int baseAdd(Publisher publisher, Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createSqlForAdd(publisher), Statement.RETURN_GENERATED_KEYS);
            int id = -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

            List<Integer> generatedKeys = new BooksDAO().addAll(publisher.getPublishedBooks());
            String format = "INSERT INTO %s (%s, %s) VALUES (%s, ?)"
                    + "ON CONFLICT ON CONSTRAINT %s DO NOTHING";
            String sql = String.format(format, PUBLISHER_BOOK, PUBLISHER_ID, BOOK_ID, id, PUBLISHER_BOOK_UQ);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (int key : generatedKeys) {
                    ps.setInt(1, key);
                    ps.executeUpdate();
                }
            }

            return id;
        }
    }

    private String createSqlForAdd(Publisher publisher) {
        String name = "\'" + publisher.getName() + "\'";

        // updating so that RETURNING works
        // adding ;-- so that Statement doesn't break RETURNING option
        String format = "INSERT INTO %s (%s) VALUES (%s)"
                + "ON CONFLICT ON CONSTRAINT %s DO UPDATE SET %s=%s RETURNING %s;--";

        return String.format(format, PUBLISHER, NAME, name, PUBLISHER_UQ, NAME, name, ID);
    }

    private PreparedStatement createPreparedStatementForGet(Connection con, int id) throws SQLException {
        String format = "SELECT %s FROM %s WHERE %s=%s";
        String sql = String.format(format, NAME, PUBLISHER, ID, id);
        return con.prepareStatement(sql);
    }

    private List<Book> getPublisherBooks(Connection con, int publisherId) throws SQLException {
        try (PreparedStatement stmt = createPreparedStatementForGetPublisherBooks(con, publisherId);
             ResultSet rs = stmt.executeQuery()) {

            List<Integer> keys = new ArrayList<>();
            while (rs.next()) {
                int key = rs.getInt(ID);
                keys.add(key);
            }

            List<Book> books = new ArrayList<>();
            for (Integer key : keys) {
                Book book = new BooksDAO().get(key);
                books.add(book);
            }
            return books;

        }
    }

    private PreparedStatement createPreparedStatementForGetPublisherBooks(Connection con, int publisherId) throws SQLException {
        String format = "SELECT b.%s FROM %s AS b JOIN %s AS pb ON b.%s=pb.%s WHERE pb.%s=%s ";
        String sql = String.format(format, ID, BOOK, PUBLISHER_BOOK, ID, BOOK_ID, PUBLISHER_ID, publisherId);
        return con.prepareStatement(sql);
    }

    private PreparedStatement createPreparedStatementForGetAll(Connection con) throws SQLException {
        String format = "SELECT %s, %s FROM %s";
        String sql = String.format(format, ID, NAME, PUBLISHER);
        return con.prepareStatement(sql);
    }

    private String createSqlForUpdate(int id, String name) {
        String nameStr = "\'" + name + "\'";
        String format = "UPDATE %s SET %s=%s WHERE %s=%s";

        return String.format(format, PUBLISHER, NAME, nameStr, ID, id);
    }
}
