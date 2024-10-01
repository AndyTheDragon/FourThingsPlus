package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{


    public static User login(String name, String password, ConnectionPool connectionPool) throws DatabaseException
    {

        String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                return new User(user_id, name, password);
            } else {
                throw new DatabaseException("Invalid username/password combo.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
