package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper
{


    public static User login(String name, String password, ConnectionPool connectionPool) throws DatabaseException
    {

        String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
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

    public static User createUser(String username, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO users (user_name, password) VALUES (?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            var keySet = preparedStatement.getGeneratedKeys();
            if (keySet.next()) {
                return new User(keySet.getInt(1), username, password);
            }
            throw new DatabaseException("Invalid username/password combo.");
        } catch (SQLException e) {
            String msg = e.getMessage();
            if (e.getMessage().startsWith("ERROR: duplicate key value ")){
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg);
        }
    }
}
