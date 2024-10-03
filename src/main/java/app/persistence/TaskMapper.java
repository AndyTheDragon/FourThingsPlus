package app.persistence;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TaskMapper
{

    public static List<Task> getTasks(boolean status, User user, ConnectionPool pool) throws DatabaseException
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE user_id = ? AND done= ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getUserId());
            stmt.setBoolean(2, status);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int taskId = rs.getInt("task_id");
                String taskName = rs.getString("task_name");
                String taskDescription = rs.getString("task_description");
                boolean done = rs.getBoolean("done");
                LocalDate lastChanged = rs.getDate("last_changed").toLocalDate();
                tasks.add(new Task(taskId, taskName, taskDescription, user, lastChanged, done));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }

        return tasks;
    }

    public static void toggleTask(int taskId, ConnectionPool pool) throws DatabaseException
    {
        String sql = "UPDATE task SET last_changed = now(), " +
                "done = CASE WHEN done=true THEN false ELSE true END" +
                " WHERE task_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();

        } catch (SQLException e)
        {
            throw new DatabaseException("Toggle task: " + e.getMessage());

        }
    }

    public static int addTask(String taskName, String taskDesc, User user, ConnectionPool pool) throws DatabaseException
    {
        String sql = "INSERT INTO task (task_name, task_description, user_id) VALUES (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, taskName);
            stmt.setString(2, taskDesc);
            stmt.setInt(3, user.getUserId());
            stmt.executeUpdate();
            var keySet = stmt.getGeneratedKeys();
            if (keySet.next()) {
                return keySet.getInt(1);
            }
             else return -1;
        } catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}
