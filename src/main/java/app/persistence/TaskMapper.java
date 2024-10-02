package app.persistence;

import app.entities.Task;
import app.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TaskMapper
{

    public static List<Task> getTasks(boolean status, User user, ConnectionPool pool)
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
            throw new RuntimeException(e);
        }

        return tasks;
    }

    public static boolean toggleTask(Task task, ConnectionPool pool) {
        String sql = "UPDATE task SET done = ? WHERE task_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(task.isDone()) {
                stmt.setBoolean(1, false);
            } else {
                stmt.setBoolean(1, true);
            }
            stmt.setInt(2, task.getTaskId());
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0) {
                return false;
            }
            return true;

        } catch (SQLException e)
        {
            throw new RuntimeException(e);

        }
    }

    public static int addTask(Task task, ConnectionPool pool)
    {
        String sql = "INSERT INTO task (task_name, task_description, user_id) VALUES (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskName());
            stmt.setString(2, task.getTaskDescription());
            stmt.setInt(3, task.getUser().getUserId());
            stmt.executeUpdate();
            var keySet = stmt.getGeneratedKeys();
            if (keySet.next()) {
                return keySet.getInt(1);
            }
             else return -1;
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
