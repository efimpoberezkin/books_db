package com.epam.homework.books_db.postgresql;

import com.epam.homework.books_db.model.Author;
import com.epam.homework.books_db.model.Gender;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.epam.homework.books_db.postgresql.ConstantsContainer.*;

public class AuthorsDAO {

    /**
     * @return generated id
     */
    public int add(Author author) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            return baseAdd(author, con);
        } catch (SQLException e) {
            throw new DAOException("Failed to add author", e);
        }
    }

    private int baseAdd(Author author, Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createSqlForAdd(author), Statement.RETURN_GENERATED_KEYS);
            int id = -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
            return id;
        }
    }

    private String createSqlForAdd(Author author) {
        String name = "\'" + author.getName() + "\'";
        String dateOfBirth = "\'" + author.getDateOfBirth().toString() + "\'";
        String dateOfDeath = author.getDateOfDeath().isPresent()
                ? "\'" + author.getDateOfDeath().get().toString() + "\'"
                : "NULL";
        int gender = author.getGender() == Gender.MALE ? MALE_ID : FEMALE_ID;

        // updating so that RETURNING works
        // adding ;-- so that Statement doesn't break RETURNING option
        String format = "INSERT INTO %s (%s, %s, %s, %s) VALUES (%s, %s, %s, %s)"
                + "ON CONFLICT ON CONSTRAINT %s DO UPDATE SET %s=%s RETURNING %s;--";

        return String.format(format, AUTHOR, NAME, DATE_OF_BIRTH, DATE_OF_DEATH, GENDER_ID,
                name, dateOfBirth, dateOfDeath, gender, AUTHOR_UQ, NAME, name, ID);
    }

    /**
     * @return list of generated ids
     */
    public List<Integer> addAll(List<Author> authors) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD)) {
            List<Integer> generatedIds = new ArrayList<>();
            for (Author author : authors) {
                int id = baseAdd(author, con);
                generatedIds.add(id);
            }
            return generatedIds;
        } catch (SQLException e) {
            throw new DAOException("Failed to add authors", e);
        }
    }

    public Author get(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGet(con, id);
             ResultSet rs = stmt.executeQuery()) {
            Author author = null;
            if (rs.next()) {
                String name = rs.getString(NAME);
                LocalDate dateOfBirth = LocalDate.parse(rs.getDate(DATE_OF_BIRTH).toString());
                Date date = rs.getDate(DATE_OF_DEATH);
                LocalDate dateOfDeath = date != null
                        ? LocalDate.parse(date.toString())
                        : null;
                Gender gender = rs.getInt(GENDER_ID) == MALE_ID
                        ? Gender.MALE
                        : Gender.FEMALE;

                author = new Author(name, dateOfBirth, dateOfDeath, gender);
            }
            return author;
        } catch (SQLException e) {
            throw new DAOException("Failed to get author", e);
        }
    }

    private PreparedStatement createPreparedStatementForGet(Connection con, int id) throws SQLException {
        String format = "SELECT %s, %s, %s, %s FROM %s WHERE %s=%s";
        String sql = String.format(format, NAME, DATE_OF_BIRTH, DATE_OF_DEATH, GENDER_ID, AUTHOR, ID, id);
        return con.prepareStatement(sql);
    }

    public List<Author> getAll() throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             PreparedStatement stmt = createPreparedStatementForGetAll(con);
             ResultSet rs = stmt.executeQuery()) {
            List<Author> authors = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString(NAME);
                LocalDate dateOfBirth = LocalDate.parse(rs.getDate(DATE_OF_BIRTH).toString());
                Date date = rs.getDate(DATE_OF_DEATH);
                LocalDate dateOfDeath = date != null
                        ? LocalDate.parse(date.toString())
                        : null;
                Gender gender = rs.getInt(GENDER_ID) == MALE_ID
                        ? Gender.MALE
                        : Gender.FEMALE;

                Author author = new Author(name, dateOfBirth, dateOfDeath, gender);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DAOException("Failed to get authors", e);
        }
    }

    private PreparedStatement createPreparedStatementForGetAll(Connection con) throws SQLException {
        String format = "SELECT %s, %s, %s, %s FROM %s";
        String sql = String.format(format, NAME, DATE_OF_BIRTH, DATE_OF_DEATH, GENDER_ID, AUTHOR);
        return con.prepareStatement(sql);
    }

    public void update(int id, String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {
            String sql = createSqlForUpdate(id, name, dateOfBirth, dateOfDeath, gender);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DAOException("Failed to update author", e);
        }
    }

    private String createSqlForUpdate(int id, String name, LocalDate dateOfBirth, LocalDate dateOfDeath, Gender gender) {
        String nameStr = "\'" + name + "\'";
        String dateOfBirthStr = "\'" + dateOfBirth.toString() + "\'";
        String dateOfDeathStr = dateOfDeath != null
                ? "\'" + dateOfDeath.toString() + "\'"
                : "NULL";
        int genderInt = gender == Gender.MALE ? MALE_ID : FEMALE_ID;

        String format = "UPDATE %s SET %s=%s, %s=%s, %s=%s, %s=%s WHERE %s=%s";

        return String.format(format, AUTHOR, NAME, nameStr, DATE_OF_BIRTH, dateOfBirthStr,
                DATE_OF_DEATH, dateOfDeathStr, GENDER_ID, genderInt, ID, id);
    }

    public void delete(int id) throws DAOException {
        try (Connection con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
             Statement stmt = con.createStatement()) {

            String format = "DELETE FROM %s WHERE %s=%s";
            String sql = String.format(format, BOOK_AUTHOR, AUTHOR_ID, id);
            stmt.executeUpdate(sql);

            format = "DELETE FROM %s WHERE %s=%s";
            sql = String.format(format, AUTHOR, ID, id);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DAOException("Failed to delete author", e);
        }
    }
}
